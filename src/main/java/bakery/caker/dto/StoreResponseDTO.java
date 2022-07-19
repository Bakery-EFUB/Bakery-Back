package bakery.caker.dto;
import bakery.caker.domain.Member;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import bakery.caker.domain.Store;
import lombok.NoArgsConstructor;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class StoreResponseDTO {
    private Member owner;
    private String ownerName;
    private String name;
    private String mainImg;
    private String readme;
    private String address;
    private String kakaoUrl;
    private String instagram;
    private Boolean certifyFlag;
    private String openTime;
    private String phoneNumber;
    private LocalDateTime createdDate;
    private List<String> menuImg;

    @Builder
    public StoreResponseDTO(Store entity,Member owner, String ownerName, String imgUrl, List<String> menuUrl) {
        this.owner = owner;
        this.ownerName = ownerName;
        this.name = entity.getName();
        this.mainImg = imgUrl;
        this.readme = entity.getReadme();
        this.address = entity.getAddress();
        this.kakaoUrl = entity.getKakaoUrl();
        this.instagram = entity.getInstagram();
        this.certifyFlag = entity.getCertifyFlag();
        this.openTime = entity.getOpenTime();
        this.phoneNumber = entity.getPhoneNumber();
        this.createdDate = entity.getCreatedDate();
        this.menuImg = menuUrl;
    }

    public Store toEntity() {
        Store build = Store.builder()
            .owner(owner)
            .name(name)
            .mainImg(mainImg)
            .readme(readme)
            .address(address)
            .kakaoUrl(kakaoUrl)
            .instagram(instagram)
            .certifyFlag(certifyFlag)
            .openTime(openTime)
            .phoneNumber(phoneNumber)
            .createdDate(createdDate)
            .build();

        return build;
    }

    public void updateMainImg(String fileName){
        this.mainImg = fileName;
    }
    public void updateUser(Member owner){
        this.owner = owner;
    }
}