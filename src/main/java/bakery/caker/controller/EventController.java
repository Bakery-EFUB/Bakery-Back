package bakery.caker.controller;

import bakery.caker.config.LoginUser;
import bakery.caker.dto.EventResponseDTO;
import bakery.caker.dto.SessionUserDTO;
import bakery.caker.service.EventService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.ui.Model;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Controller
public class EventController {
    private EventService eventService;

    @GetMapping("/store/{store_id}/events")
    ResponseEntity<?> getEventList(@PathVariable("store_id") Long store_id) {
        eventService.getEventList(store_id);
        return new ResponseEntity<>("successfully deleted", HttpStatus.OK);
    }

    @PostMapping("/events")
    ResponseEntity<?> write(@LoginUser SessionUserDTO sessionUser, @RequestBody EventResponseDTO eventResponseDTO) {
        return new ResponseEntity<>(eventService.saveEvent(sessionUser.getMemberId(), eventResponseDTO), HttpStatus.OK);
    }

    @DeleteMapping("/store/{store_id}/events/{events_id}")
    ResponseEntity<?> delete(@PathVariable("store_id") Long store_id,
                             @PathVariable("events_id") Long events_id,
                             @LoginUser SessionUserDTO sessionUser) {
        eventService.deleteEvent(store_id,store_id,sessionUser.getMemberId());
        return new ResponseEntity<>("successfully deleted", HttpStatus.OK);
    }
}