package bakery.caker.dto;
import bakery.caker.domain.Member;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Optional;

import bakery.caker.domain.Store;
import lombok.NoArgsConstructor;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class StoreRequestDTO {
    private Member owner;
    private String name;
    private String mainImg;
    private String readme;
    private String address;
    private String kakaoUrl;
    private String instagram;
    private Boolean certifyFlag;
    private String openTime;
    private String phoneNumber;

    @Builder
    public StoreRequestDTO(Member owner, String name, String mainImg,String readme, String address, String kakaoUrl, String instagram, boolean certifyFlag,
                           String openTime, String phoneNumber) {
        this.owner = owner;
        this.name = name;
        this.mainImg = mainImg;
        this.readme = readme;
        this.address =address;
        this.kakaoUrl =kakaoUrl;
        this.instagram = instagram;
        this.certifyFlag = certifyFlag;
        this.openTime = openTime;
        this.phoneNumber = phoneNumber;
    }
}