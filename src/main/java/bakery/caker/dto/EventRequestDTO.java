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
public class EventRequestDTO {
    private String content;
    private LocalDateTime pickupDate;
    private LocalDateTime pickupTime;

    @Builder
    public EventRequestDTO( String content, LocalDateTime pickupDate,
                            LocalDateTime pickupTime){
        this.content = content;
        this.pickupDate = pickupDate;
        this.pickupTime = pickupTime;
    }

    public Event toEntity() {
        Event event = Event.builder()
                .content(content)
                .pickupDate(pickupDate)
                .pickupTime(pickupTime)
                .build();
        return event;
    }
}