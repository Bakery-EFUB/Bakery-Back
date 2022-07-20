package bakery.caker.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import bakery.caker.domain.Store;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "storeId", nullable = false)
    private Store store;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    @Column
    private LocalDateTime pickupDate;

    @Column
    private LocalDateTime pickupTime;


    @Builder
    public Event(Long id, Store store, String content, LocalDateTime pickupDate, LocalDateTime pickupTime) {
        this.id = id;
        this.store = store;
        this.content = content;
        this.pickupDate = pickupDate;
        this.pickupTime = pickupTime;
    }
}
