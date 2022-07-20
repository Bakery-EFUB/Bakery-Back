package bakery.caker.controller;

import bakery.caker.config.LoginUser;
import bakery.caker.dto.EventRequestDTO;
import bakery.caker.dto.EventResponseDTO;
import bakery.caker.dto.SessionUserDTO;
import bakery.caker.service.EventService;
import bakery.caker.service.StoreService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.ui.Model;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping("/store/{storeId}/events")
    ResponseEntity<?> EventList(@PathVariable("storeId") Long storeId) {

        return new ResponseEntity<>(eventService.getEventList(storeId), HttpStatus.OK);
    }

    @PostMapping("/events")
    ResponseEntity<?> EventUpdate(@LoginUser SessionUserDTO sessionUser, @RequestBody EventRequestDTO eventrequestDTO) {
//        return new ResponseEntity<>(sessionUser.getMemberId(), HttpStatus.OK);
//        eventService.saveEvent(sessionUser.getMemberId(), eventrequestDTO);
        return new ResponseEntity<>(eventService.saveEvent(sessionUser.getMemberId(), eventrequestDTO), HttpStatus.OK);
    }

    @DeleteMapping("/store/{storeId}/events/{events_id}")
    ResponseEntity<?> EventRemove(@PathVariable("storeId") Long storeId,
                             @PathVariable("events_id") Long events_id,
                             @LoginUser SessionUserDTO sessionUser) {
        eventService.deleteEvent(storeId,storeId,sessionUser.getMemberId());
        return new ResponseEntity<>("successfully deleted", HttpStatus.OK);
    }
}