package bakery.caker.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "storeId")
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "memberId", nullable = false)
    private Member owner;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String mainImg;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String readme;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String address;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String kakaoUrl;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String instagram;

    @Column
    private Boolean certifyFlag;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String openTime;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String phoneNumber;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @Builder
    public Store(Member owner, String name, String mainImg, String readme, String address, String kakaoUrl, String instagram, Boolean certifyFlag,String openTime, String phoneNumber, LocalDateTime createdDate) {
        this.owner = owner;
        this.name = name;
        this.mainImg = mainImg;
        this.readme = readme;
        this.address = address;
        this.kakaoUrl =kakaoUrl;
        this.instagram = instagram;
        this.certifyFlag = certifyFlag;
        this.openTime = openTime;
        this.phoneNumber = phoneNumber;
        this.createdDate = createdDate;

    }
}