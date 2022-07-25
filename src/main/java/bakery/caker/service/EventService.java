package bakery.caker.service;

import bakery.caker.domain.Member;
import bakery.caker.domain.Store;
import bakery.caker.dto.EventRequestDTO;
import bakery.caker.dto.EventResponseDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import bakery.caker.domain.Event;

import bakery.caker.repository.MemberRepository;
import bakery.caker.repository.StoreRepository;
import bakery.caker.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class EventService {

    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;
    private final EventRepository eventRepository;

    @Transactional
    public List<EventResponseDTO> getEventList(Long storeId) {
        Optional<Store> store = storeRepository.findById(storeId);
        List<Event> EventList = eventRepository.findAllEventByStoreId(storeId);
        List<EventResponseDTO> eventResponseDTOList = new ArrayList<>();

        for (Event event : EventList) {
            eventResponseDTOList.add(new EventResponseDTO(event));
        }
        return eventResponseDTOList;
    }

    @Transactional
    public Long saveEvent(Long memberId, EventRequestDTO eventRequestDTO)  {
        Member owner = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다 "));
        Store store = storeRepository.findStoreByOwner(owner).orElseThrow(() -> new IllegalArgumentException("해당 스토어가 존재하지 않습니다 "));
        EventResponseDTO eventResponseDTO = EventResponseDTO.builder()
                .event(eventRequestDTO.toEntity())
                .build();
        eventResponseDTO.updateStore(store);
        return eventRepository.save(eventResponseDTO.toEntity()).getId();
    }

    @Transactional
    public void deleteEvent(Long storeId, Long eventId, Long ownerId) {
        //세션 유저가 가게 주인일때만 삭제 가능
        Optional<Store> store = storeRepository.findById(storeId);
        if( ownerId.equals(store.get().getOwner().getMemberId())){
            eventRepository.deleteById(eventId);
        }
        else return;
    }
}
