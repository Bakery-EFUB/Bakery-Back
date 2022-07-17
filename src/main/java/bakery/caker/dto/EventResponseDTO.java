package bakery.caker.dto;
import bakery.caker.domain.Store;
import jdk.tools.jlink.plugin.ResourcePoolEntry;
import lombok.*;

import java.time.LocalDateTime;

import bakery.caker.domain.Event;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EventResponseDTO {
    private String storeName;
    private Long store;
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
}