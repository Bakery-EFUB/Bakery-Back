package bakery.caker.service;

import bakery.caker.exception.CustomException;
import bakery.caker.exception.ErrorCode;
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
    public Long saveStore(Long memberId, StoreResponseDTO storeResponseDTO) throws IOException {
        Member owner = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND, null));
        storeResponseDTO.updateUser(owner);
        Long storeId = storeRepository.save(storeResponseDTO.toEntity()).getId();
        return storeId;
    }

    @Transactional
    public Long updateStore(Long storeId, MultipartFile mainImg, List<MultipartFile> menuImg) throws IOException {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND, null));
        S3Presigner presigner = createPresigner();
        String fileName = makeFileName(mainImg);
        URL url = ImageUploadService.getS3UploadURL(presigner, this.bucket, fileName);
        ImageUploadService.UploadImage(url, mainImg);

        store.updateMainImage(fileName);
        storeRepository.save(store);

        if (menuImg.size() != 0){ uploadMenuImg(presigner, menuImg, storeId.toString());}
        presigner.close();
        return store.getId();
    }

    @Transactional
    public List<StoreResponseDTO> getStoreList() {
        List <String> menuUrl = new ArrayList<>();
        List<Store> StoreList = storeRepository.findStoresByOwner_DeleteFlag(false);
        List<StoreResponseDTO> storeResponseDTOList = new ArrayList<>();

        for (Store store : StoreList) {
            // 주인장 찾기
            StoreResponseDTO storeResponseDTO = createStoreResponseDTO(store, menuUrl);
            storeResponseDTOList.add(storeResponseDTO);

        }
        return storeResponseDTOList;
    }
    
    @Transactional
    public List<StoreResponseDTO> getStoreRecomendList() {
        List <String> menuUrl = new ArrayList<>();
        List<Store> StoreList = storeRepository.findStoresByOwner_DeleteFlag(false);
        List<StoreResponseDTO> storeResponseDTOList = new ArrayList<>();
        Collections.shuffle(StoreList);
        if (StoreList.size() < 6){return storeResponseDTOList;}
        for (int i = 0; i<6; i++){
            Store store = StoreList.get(i);
            StoreResponseDTO storeResponseDTO = createStoreResponseDTO(store, menuUrl);
            storeResponseDTOList.add(storeResponseDTO);
        }
        return storeResponseDTOList;
    }

    public Member findMemberByOwner(Store store) {
        Long memberId = store.getOwner().getMemberId();
        Member user = memberRepository.findMemberByMemberIdAndDeleteFlagIsFalse(memberId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND, null));
        return user;
    }

    public StoreResponseDTO createStoreResponseDTO(Store store, List<String> menuUrl) {
        Member user = findMemberByOwner(store);
        String ownerName = user.getNickname();
        S3Presigner presigner = createPresigner();
        String imgUrl = findStoreMainImage(presigner,store.getId());
        return new StoreResponseDTO(store, user, ownerName, imgUrl, menuUrl);
    }

    @Transactional
    public List<Store> selectStoreByQuery(String q) {
        List<Store> storeResponseDTOList = storeRepository.findByNameContaining(q);
        return storeResponseDTOList;
    }

    @Transactional
    public StoreResponseDTO getStoreDetail(Long id) {
        Store store = storeRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND, "스토어 id= "+id ));
        Long memberId = store.getOwner().getMemberId();
        Optional<Member> user = memberRepository.findMemberByMemberIdAndDeleteFlagIsFalse(memberId);
        String ownerName = user.get().getNickname();

        S3Presigner presigner = createPresigner();
        String imgUrl = findStoreMainImage(presigner, id);
        List <String> menuUrl = new ArrayList<>();
        for(int i = 1; i < 5; i ++){
            String menus = findStoreMenuImage(presigner, store.getId(), i);
            menuUrl.add(menus);
        }
        presigner.close();
        return new StoreResponseDTO(store,user.get(), ownerName, imgUrl, menuUrl);
        }

    @Transactional
    public StoreResponseDTO getStoreDetailByOwner(Long owner_id) {
        Member owner  = memberRepository.findById(owner_id).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND, null));
        Store store = storeRepository.findStoreByOwner(owner).orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND, "해당 유저의 스토어가 존재하지 않습니다 id= " + owner_id));
        String ownerName = owner.getNickname();
        S3Presigner presigner = createPresigner();
        String imgUrl = findStoreMainImage(presigner,store.getId());
        List <String> menuUrl = new ArrayList<>();
        for(int i = 1; i < 5; i ++){
            String menus = findStoreMenuImage(presigner, store.getId(), i);
            menuUrl.add(menus);
        }
        return new StoreResponseDTO(store,owner, ownerName, imgUrl,menuUrl);
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
    public String findStoreMainImage(S3Presigner presigner, Long storeId) {
        Optional<Store> store = storeRepository.findById(storeId);
        String fileName = store.get().getMainImg();
        String url = ImageUploadService.getS3DownloadURL(presigner, this.bucket, fileName);
        return url;
    }

    @Transactional
    public String findStoreMenuImage(S3Presigner presigner, Long storeId, Number index) {
        String fileName = storeId + "/caker-store-menu-"+ index+".png";
        String url = ImageUploadService.getS3DownloadURL(presigner, this.bucket, fileName);
        return url;
    }

    public static String makeFileName(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        int extension = fileName.lastIndexOf(".");
        String contentType = fileName.substring(extension);

        SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHHmmss");
        return "caker-store-" + date.format(new Date()) + contentType;
    }


    public void uploadMenuImg(S3Presigner presigner, List<MultipartFile> menuImg, String storeId) throws IOException {
        int index = 1;
        for (MultipartFile element : menuImg) {
            String fileName = element.getOriginalFilename();
            int extension = fileName.lastIndexOf(".");
            String contentType = fileName.substring(extension);
            fileName = storeId +"/caker-store-menu-"+ Integer.toString(index)+contentType;
            URL url = ImageUploadService.getS3UploadURL(presigner, this.bucket, fileName);
            ImageUploadService.UploadImage(url, element);
            index = index + 1;
        }
    }
}
