package bakery.caker.domain;

import com.sun.istack.NotNull;
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

    @NotNull
    private String locationGu;
    @NotNull
    private String locationDong;
    @NotNull
    private String type;
    @NotNull
    private String size;
    @NotNull
    private String flavor;

    @Column(columnDefinition= "TEXT")
    @NotNull
    private String description;

    private String image;
    @NotNull
    private LocalDateTime pickupDate;
    @NotNull
    private Integer priceMin;
    @NotNull
    private Integer priceMax;
    private Boolean finishedFlag;

    @ManyToOne()
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Sheet(String locationGu, String locationDong, String type, String size, String flavor, String description, String image, LocalDateTime pickupDate, Integer priceMin, Integer priceMax, Member member){
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
        this.finishedFlag = false;
    }

    public void updatePickup(LocalDateTime pickupDate){
        this.pickupDate = pickupDate;
    }

    public void updateImage(String image){
        this.image = image;
    }

    public void updateFinishedFlag(){
        this.finishedFlag = true;
    }
}

