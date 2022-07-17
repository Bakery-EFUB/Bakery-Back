package bakery.caker.dto;

import bakery.caker.domain.Member;
import bakery.caker.domain.Sheet;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SheetDTO {
    private String locationGu;
    private String locationDong;
    private String type;
    private String flavor;
    private String size;
    private String description;
    private String image;
    private LocalDateTime pickupDate;
    private Integer priceMin;
    private Integer priceMax;
    private String hashtag;

    @Builder
    public SheetDTO(String locationGu, String locationDong, String type, String flavor, String size, String description, String image, LocalDateTime pickupDate, Integer priceMin, Integer priceMax, String hashtag){
        this.description = description;
        this.flavor = flavor;
        this.hashtag = hashtag;
        this.image = image;
        this.locationDong = locationDong;
        this.locationGu = locationGu;
        this.pickupDate = pickupDate;
        this.priceMax = priceMax;
        this.priceMin = priceMin;
        this.size = size;
        this.type = type;
    }

    public Sheet toEntity(Member member){
        return Sheet.builder()
                .description(description)
                .flavor(flavor)
                .hashtag(hashtag)
                .image(image)
                .locationDong(locationDong)
                .locationGu(locationGu)
                .member(member)
                .pickupDate(pickupDate)
                .priceMax(priceMax)
                .priceMin(priceMin)
                .size(size)
                .type(type)
                .build();
    }
}
