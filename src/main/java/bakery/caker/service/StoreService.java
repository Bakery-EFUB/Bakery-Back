package bakery.caker.service;

import lombok.*;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import bakery.caker.domain.Member;
import bakery.caker.domain.Store;
import bakery.caker.dto.StoreResponseDTO;
import bakery.caker.repository.MemberRepository;
import bakery.caker.repository.StoreRepository;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@RequiredArgsConstructor
public class StoreService {

    // AWS credentials
//    @Value("${cloud.aws.credentials.accessKey}")
//    private String accessKey;
//
//    @Value("${cloud.aws.credentials.secretKey}")
//    private String secretKey;
//
//    @Value("${cloud.aws.s3.bucket}")
//    private String bucket;
    
    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long saveStore(StoreResponseDTO storeResponseDTO) {
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
                StoreResponseDTO storeResponseDTO = StoreResponseDTO.builder()
                        .ownerName(user.get().getNickname())
                        .mainImg(store.getMainImg())
                        .name(store.getName())
                        .readme(store.getReadme())
                        .address(store.getAddress())
                        .kakaoUrl(store.getKakaoUrl())
                        .instagram(store.getInstagram())
                        .menuImg(store.getMenuImg())
                        .certifyFlag(store.getCertifyFlag())
                        .openTime(store.getOpenTime())
                        .phoneNumber(store.getPhoneNumber())
                        .createdDate(store.getCreatedDate())
                        .build();
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
                StoreResponseDTO storeResponseDTO = StoreResponseDTO.builder()
                        .ownerName(user.get().getNickname())
                        .mainImg(store.getMainImg())
                        .name(store.getName())
                        .readme(store.getReadme())
                        .address(store.getAddress())
                        .kakaoUrl(store.getKakaoUrl())
                        .instagram(store.getInstagram())
                        .menuImg(store.getMenuImg())
                        .certifyFlag(store.getCertifyFlag())
                        .openTime(store.getOpenTime())
                        .phoneNumber(store.getPhoneNumber())
                        .createdDate(store.getCreatedDate())
                        .build();
                storeResponseDTOList.add(storeResponseDTO);
            }
        }
        return storeResponseDTOList;
    }

    @Transactional
    public List<StoreResponseDTO> selectStoreByQuery(@RequestParam String q) {
        List<StoreResponseDTO> storeResponseDTOList = new ArrayList<>();
//        return StoreRepository.findAll(q).stream()
//        .map(StoreResponseDTO)
//        .collect(Collectors.toList());
        return storeResponseDTOList;
    }

    @Transactional
    public StoreResponseDTO getStoreDetail(Long id) {
        Optional<Store> store = storeRepository.findById(id);
        Long memberId = store.get().getOwner();
        Optional<Member> user = memberRepository.findMemberByMemberIdAndDeleteFlagIsFalse(memberId);
        StoreResponseDTO storeResponseDTO = StoreResponseDTO.builder()
                .ownerName(user.get().getNickname())
                .name(store.get().getName())
                .mainImg(store.get().getMainImg())
                .readme(store.get().getReadme())
                .address(store.get().getAddress())
                .kakaoUrl(store.get().getKakaoUrl())
                .instagram(store.get().getInstagram())
                .menuImg(store.get().getMenuImg())
                .certifyFlag(store.get().getCertifyFlag())
                .openTime(store.get().getOpenTime())
                .phoneNumber(store.get().getPhoneNumber())
                .createdDate(store.get().getCreatedDate())
                .build();
        return storeResponseDTO;
        }

    @Transactional
    public StoreResponseDTO getStoreDetailByOwner(Long memberId) {
        Optional<Store> store = storeRepository.findStoreByOwner(memberId);
        Optional<Member> user = memberRepository.findMemberByMemberIdAndDeleteFlagIsFalse(memberId);
        StoreResponseDTO storeResponseDTO = StoreResponseDTO.builder()
                .ownerName(user.get().getNickname())
                .name(store.get().getName())
                .mainImg(store.get().getMainImg())
                .readme(store.get().getReadme())
                .address(store.get().getAddress())
                .kakaoUrl(store.get().getKakaoUrl())
                .instagram(store.get().getInstagram())
                .menuImg(store.get().getMenuImg())
                .certifyFlag(store.get().getCertifyFlag())
                .openTime(store.get().getOpenTime())
                .phoneNumber(store.get().getPhoneNumber())
                .createdDate(store.get().getCreatedDate())
                .build();
        return storeResponseDTO;
    }
}
