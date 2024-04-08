package com.example.backend.controller;

import com.example.backend.dto.CardDto;
import com.example.backend.dto.EventDto;
import com.example.backend.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping("/create")
    public ResponseEntity<EventDto> createEvent(@RequestBody EventDto eventDTO, Authentication authentication) {
        return ResponseEntity.ok().body(eventService.createEvent(eventDTO, authentication));
    }
    @GetMapping
    public ResponseEntity<List<EventDto>> getEvents(Authentication authentication){
        return ResponseEntity.ok().body(eventService.getAllEvents(authentication));
    }

    @PostMapping("/card")
    public ResponseEntity<CardDto> createCard(@RequestBody CardDto card, Authentication authentication){
        return ResponseEntity.ok().body(eventService.createCard(card, authentication));
    }
}
