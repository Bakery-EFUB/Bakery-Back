package bakery.caker.service;

import bakery.caker.domain.Store;
import bakery.caker.dto.EventResponseDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import bakery.caker.domain.Event;

import bakery.caker.dto.StoreResponseDTO;
import bakery.caker.repository.MemberRepository;
import bakery.caker.repository.StoreRepository;
import bakery.caker.repository.EventRepository;

import javax.transaction.Transactional;

public class EventService {

    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;
    private final EventRepository eventRepository;

    public EventService(StoreRepository storeRepository, MemberRepository memberRepository, EventRepository eventRepository) {
        this.storeRepository = storeRepository;
        this.memberRepository = memberRepository;
        this.eventRepository = eventRepository;
    }

    @Transactional
    public List<EventResponseDTO> getEventList(Long store_id) {
        Optional<Store> store = storeRepository.findById(store_id);
        List<Event> EventList = eventRepository.findAllByStore(store_id);
        List<EventResponseDTO> eventResponseDTOList = new ArrayList<>();

        for (Event event : EventList) {
                EventResponseDTO eventResponseDTO = EventResponseDTO.builder()
                        .storeName(store.get().getName())
                        .content(event.getContent())
                        .pickupDate(event.getPickupDate())
                        .pickupTime(event.getPickupTime())
                        .build();
            eventResponseDTOList.add(eventResponseDTO);
        }
        return eventResponseDTOList;
    }

    @Transactional
    public Long saveEvent(EventResponseDTO eventResponseDTO)  {
        return eventRepository.save(eventResponseDTO.toEntity()).getId();
    }

    @Transactional
    public void deleteEvent(Long store_id, Long event_id, Long owner_id) {
        //세션 유저가 가게 주인일때만 삭제 가능
        Optional<Store> store = storeRepository.findById(store_id);
        if( owner_id == store.get().getOwner()){
            eventRepository.deleteById(event_id);
        }
        else return;
    }
}
