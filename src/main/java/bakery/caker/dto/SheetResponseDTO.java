package bakery.caker.dto;

import bakery.caker.domain.Member;
import bakery.caker.domain.Sheet;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class SheetResponseDTO {
    private Long sheetId;
    private String locationGu;
    private String locationDong;
    private String type;
    private String size;
    private String flavor;
    private String description;
    private String image;
    private String imageUrl;
    private LocalDateTime pickupDate;
    private Integer priceMin;
    private Integer priceMax;
    private LocalDateTime createdAt;
    private String hashtag;
    private Boolean finishedFlag;
    private Member member;

    @Builder
    public SheetResponseDTO(Sheet sheet, String imageUrl){
        this.sheetId = sheet.getSheetId();
        this.locationGu = sheet.getLocationGu();
        this.locationDong = sheet.getLocationDong();
        this.type = sheet.getType();
        this.size = sheet.getSize();
        this.flavor = sheet.getFlavor();
        this.description = sheet.getDescription();
        this.image = sheet.getImage();
        this.imageUrl = imageUrl;
        this.pickupDate = sheet.getPickupDate();
        this.priceMin = sheet.getPriceMin();
        this.priceMax = sheet.getPriceMax();
        this.member = sheet.getMember();
        this.hashtag = sheet.getHashtag();
        this.createdAt = sheet.getCreatedAt();
        this.finishedFlag = false;
    }
}
