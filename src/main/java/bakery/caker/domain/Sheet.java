package bakery.caker.domain;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "sheet")
public class Sheet extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sheetId;

    private String locationGu;
    private String locationDong;
    private String type;
    private String size;
    private String flavor;

    @Column(columnDefinition= "TEXT")
    private String description;

    private String image;
    private LocalDateTime pickupDate;
    private Integer priceMin;
    private Integer priceMax;
    private String hashtag;
    private Boolean finishedFlag;

    @ManyToOne()
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Sheet(String locationGu, String locationDong, String type, String size, String flavor, String description, String image, LocalDateTime pickupDate, Integer priceMin, Integer priceMax, String hashtag, Member member){
        this.locationGu = locationGu;
        this.locationDong = locationDong;
        this.type = type;
        this.size = size;
        this.flavor = flavor;
        this.description = description;
        this.image = image;
        this.pickupDate = pickupDate;
        this.priceMin = priceMin;
        this.priceMax = priceMax;
        this.member = member;
        this.hashtag = hashtag;
        this.finishedFlag = false;
    }

    public void updatePickup(LocalDateTime pickupDate){
        this.pickupDate = pickupDate;
    }

    public void updateFinishedFlag(){
        this.finishedFlag = true;
    }
}
