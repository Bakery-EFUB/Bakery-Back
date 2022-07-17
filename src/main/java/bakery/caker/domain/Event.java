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
public class Event {
    
    @Id
    @GeneratedValue
    private Long id;

    @JoinColumn(name = "store_id", nullable = false)
    private Long store;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    @Column
    private LocalDateTime pickupDate;

    @Column
    private LocalDateTime pickupTime;


    @Builder
    public Event(Long id, Long store, String content, LocalDateTime pickupDate, LocalDateTime pickupTime) {
        this.id = id;
        this.store = store;
        this.content = content;
        this.pickupDate = pickupDate;
        this.pickupTime = pickupTime;
    }
}
