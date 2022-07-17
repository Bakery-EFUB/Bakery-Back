package bakery.caker.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class) /* JPA에게 해당 Entity는 Auditiong 기능을 사용함을 알립니다. */
public class Store {
    
    @Id
    @GeneratedValue
    @Column(name = "store_id")
    private Long id;

    @JoinColumn(name = "member_id", nullable = false)
    private Long owner;

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
    public Store(Long id, Long owner, String name, String mainImg, String readme, String address, String kakaoUrl, String instagram, Boolean certifyFlag,String openTime, String phoneNumber, LocalDateTime createdDate) {
        this.id = id;
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
