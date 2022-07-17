package bakery.caker.controller;

import bakery.caker.dto.EventResponseDTO;
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

    @PostMapping("/Events")
    ResponseEntity<?> write(@RequestBody EventResponseDTO EventResponseDTO) {
        return new ResponseEntity<>(EventService.saveEvent(EventResponseDTO), HttpStatus.OK);
    }

}