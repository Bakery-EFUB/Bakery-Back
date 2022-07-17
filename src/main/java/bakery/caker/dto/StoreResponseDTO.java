package bakery.caker.dto;
import lombok.*;

import java.time.LocalDateTime;
import bakery.caker.domain.Store;
import lombok.NoArgsConstructor;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class StoreResponseDTO {
    private Long owner;
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

    @Builder
    public StoreResponseDTO(String ownerName, String name, String mainImg, String readme, String address, String kakaoUrl,
            String instagram, Boolean certifyFlag, String openTime,  String phoneNumber, LocalDateTime createdDate) {
        this.ownerName = ownerName;
        this.name = name;
        this.mainImg = mainImg;
        this.readme = readme;
        this.address = address;
        this.kakaoUrl = kakaoUrl;
        this.instagram = instagram;
        this.certifyFlag = certifyFlag;
        this.openTime = openTime;
        this.phoneNumber = phoneNumber;
        this.createdDate = createdDate;
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
}