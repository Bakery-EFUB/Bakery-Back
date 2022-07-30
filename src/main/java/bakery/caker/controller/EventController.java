package bakery.caker.controller;

import bakery.caker.dto.EventRequestDTO;
import bakery.caker.dto.SessionUserDTO;
import bakery.caker.exception.CustomException;
import bakery.caker.exception.ErrorCode;
import bakery.caker.service.EventService;
import bakery.caker.service.JwtTokenProvider;
import bakery.caker.service.StoreService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    private final StoreService storeService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/store/{storeId}/events")
    ResponseEntity<?> eventList(HttpServletRequest httpRequest, @PathVariable("storeId") Long storeId) {
        SessionUserDTO sessionUser = jwtTokenProvider.getUserInfoByToken(httpRequest);
        if(sessionUser.getMemberId() != storeService.getStoreDetail(storeId).getOwner().getMemberId()) {
            throw new CustomException(ErrorCode.ACCESS_DENIED, "해당 가게 소유자가 아닙니다.");
        }
        return new ResponseEntity<>(eventService.getEventList(storeId), HttpStatus.OK);
    }

    @PostMapping("/events")
    ResponseEntity<?> eventUpdate(HttpServletRequest httpRequest, @RequestBody EventRequestDTO eventrequestDTO) {
        SessionUserDTO sessionUser = jwtTokenProvider.getUserInfoByToken(httpRequest);
        return new ResponseEntity<>(eventService.saveEvent(sessionUser.getMemberId(), eventrequestDTO), HttpStatus.OK);
    }

    @DeleteMapping("/store/{storeId}/events/{events_id}")
    ResponseEntity<?> eventRemove(@PathVariable("storeId") Long storeId, @PathVariable("events_id") Long events_id, HttpServletRequest httpRequest) {
        SessionUserDTO sessionUser = jwtTokenProvider.getUserInfoByToken(httpRequest);
        eventService.deleteEvent(storeId,storeId,sessionUser.getMemberId());
        return new ResponseEntity<>("successfully deleted", HttpStatus.OK);
    }
}