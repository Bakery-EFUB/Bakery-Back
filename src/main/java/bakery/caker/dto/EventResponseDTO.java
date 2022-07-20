package bakery.caker.dto;
import bakery.caker.domain.Member;
import bakery.caker.domain.Store;

import lombok.*;

import java.time.LocalDateTime;

import bakery.caker.domain.Event;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EventResponseDTO {
    private String storeName;
    private Store store;
    private String content;
    private LocalDateTime pickupDate;
    private LocalDateTime pickupTime;

    @Builder
    public EventResponseDTO(String storeName, String content, LocalDateTime pickupDate,
                            LocalDateTime pickupTime){
        this.storeName = storeName;
        this.content = content;
        this.pickupDate = pickupDate;
        this.pickupTime = pickupTime;
    }

    public Event toEntity() {
        Event event = Event.builder()
                .store(store)
                .content(content)
                .pickupDate(pickupDate)
                .pickupTime(pickupTime)
                .build();
        return event;
    }
    public void updateStore(Store store){
        this.store = store;
    }

    public void updateStoreName(String name) {this.storeName = name;}
}