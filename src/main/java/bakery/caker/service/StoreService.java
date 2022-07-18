package bakery.caker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

import bakery.caker.domain.Member;
import bakery.caker.domain.Store;
import bakery.caker.dto.StoreResponseDTO;
import bakery.caker.repository.MemberRepository;
import bakery.caker.repository.StoreRepository;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Service
@RequiredArgsConstructor
public class StoreService {

    // AWS credentials
    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    
    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long saveStore(StoreResponseDTO storeResponseDTO, MultipartFile mainImg, List<MultipartFile> menuImg) throws IOException {
        S3Presigner presigner = createPresigner();
        String fileName = makeFileName(mainImg);

//        if (menuImg.size() != 0){ uploadMenuImg(presigner, menuImg);}

        URL url = ImageUploadService.getS3UploadURL(presigner, this.bucket, fileName);
        ImageUploadService.UploadImage(url, mainImg);
        presigner.close();
        storeResponseDTO.updateMainImg(fileName);

        return storeRepository.save(storeResponseDTO.toEntity()).getId();
    }

    @Transactional
    public List<StoreResponseDTO> getStoreList() {
        List<Store> StoreList = storeRepository.findAll();
        List<StoreResponseDTO> storeResponseDTOList = new ArrayList<>();

        for (Store store : StoreList) {
            // 주인장 찾기
            Long memberId = store.getOwner();
            Optional<Member> user = memberRepository.findMemberByMemberIdAndDeleteFlagIsFalse(memberId);
            if (user.isPresent()) {
                String ownerName = user.get().getNickname();
                String imgUrl = findStoreMainImage(store.getId());
                StoreResponseDTO storeResponseDTO = new StoreResponseDTO(store, ownerName, imgUrl);
                storeResponseDTOList.add(storeResponseDTO);
            }
        }
        return storeResponseDTOList;
    }
    
    @Transactional
    public List<StoreResponseDTO> getStoreRecomendList() {
        List<Store> StoreList = storeRepository.findAll();
        List<StoreResponseDTO> storeResponseDTOList = new ArrayList<>();
        Collections.shuffle(StoreList);
        for (int i = 0; i<6; i++){
            Store store = StoreList.get(i);
            // 주인장 찾기
            Long memberId = store.getOwner();
            Optional<Member> user = memberRepository.findMemberByMemberIdAndDeleteFlagIsFalse(memberId);
            if (user.isPresent()) {
                String ownerName = user.get().getNickname();
                String imgUrl = findStoreMainImage(store.getId());
                StoreResponseDTO storeResponseDTO = new StoreResponseDTO(store, ownerName, imgUrl);
                storeResponseDTOList.add(storeResponseDTO);
            }
        }
        return storeResponseDTOList;
    }

    @Transactional
    public List<StoreResponseDTO> selectStoreByQuery(@RequestParam String q) {
        List<StoreResponseDTO> storeResponseDTOList = storeRepository.findStoreByNameContaining(q);
        return storeResponseDTOList;
    }

    @Transactional
    public StoreResponseDTO getStoreDetail(Long id) {
        Store store = storeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 스토어가 존재하지 않습니다 id= "+id ));;
        Long memberId = store.getOwner();
        Optional<Member> user = memberRepository.findMemberByMemberIdAndDeleteFlagIsFalse(memberId);
        String ownerName = user.get().getNickname();
        String imgUrl = findStoreMainImage(id);
        return new StoreResponseDTO(store, ownerName, imgUrl);
        }

    @Transactional
    public StoreResponseDTO getStoreDetailByOwner(Long memberId) {
        Store store = storeRepository.findStoreByOwner(memberId).orElseThrow(() -> new IllegalArgumentException("해당 유저의 스토어가 존재하지 않습니다 id= "+memberId));
        Optional<Member> user = memberRepository.findMemberByMemberIdAndDeleteFlagIsFalse(memberId);
        String ownerName = user.get().getNickname();
        String imgUrl = findStoreMainImage(store.getId());
        return new StoreResponseDTO(store, ownerName, imgUrl);
    }


    // AWS connection
    public AwsBasicCredentials createCredentials() {
        return AwsBasicCredentials.create(this.accessKey,this.secretKey);
    }

    public S3Presigner createPresigner() {
        return S3Presigner.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(StaticCredentialsProvider.create(createCredentials()))
                .build();
    }

    @Transactional
    public String findStoreMainImage(Long storeId) {
        Optional<Store> store = storeRepository.findById(storeId);
        String fileName = store.get().getMainImg();

        S3Presigner presigner = createPresigner();

        String url = ImageUploadService.getS3DownloadURL(presigner, this.bucket, fileName);
        presigner.close();
        return url;
    }

    public static String makeFileName(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        int extension = fileName.indexOf(".");
        String contentType = fileName.substring(extension);

        SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHHmmss");
        return "caker-store-" + date.format(new Date()) + contentType;
    }

    public static String makeMenuImgName(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        int extension = fileName.indexOf(".");
        String contentType = fileName.substring(extension);

        SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHHmmss");
        return "caker-store-menu-" + date.format(new Date()) + contentType;
    }


//    public static void uploadMenuImg(S3Presigner presigner, List<MultipartFile> menuImg) throws IOException {
//        for (MultipartFile element : menuImg) {
//            String fileName = makeFileName(element);
//            URL url = ImageUploadService.getS3UploadURL(presigner, this.bucket, fileName);
//            ImageUploadService.UploadImage(url, element);
//        }
//
//    }
}
