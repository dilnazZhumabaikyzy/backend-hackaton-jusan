package com.example.backend.controller;

import com.example.backend.dto.*;
import com.example.backend.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @Transactional
    @PostMapping("/create")
    public ResponseEntity<EventDto> createEvent(@RequestBody EventDto eventDTO, Authentication authentication) {
        return ResponseEntity.ok().body(eventService.createEvent(eventDTO, authentication));
    }

    @GetMapping
    public ResponseEntity<List<EventDto>> getEvents(Authentication authentication) {
        return ResponseEntity.ok().body(eventService.getAllEvents(authentication));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDto> getEventInfo(@PathVariable String eventId) {
        return ResponseEntity.ok().body(eventService.getEvent(eventId));
    }

    @Transactional
    @PostMapping("/card")
    public ResponseEntity<CardDto> createCard(@RequestBody CardDto card, Authentication authentication) {
        return ResponseEntity.ok().body(eventService.createCard(card, authentication));
    }

    @Transactional
    @PostMapping("/shuffle/{eventId}")
    public ResponseEntity<ShuffleDto> shuffleCards(@PathVariable String eventId, Authentication authentication) {
        return ResponseEntity.ok().body(eventService.shuffle(eventId, authentication));
    }

    @GetMapping("/my-receiver/{eventId}")
    public ResponseEntity<ShuffleDto> getMyReceiver(@PathVariable String eventId, Authentication authentication) {
        return ResponseEntity.ok().body(eventService.showMyReceiver(eventId, authentication));
    }

    @PostMapping("/send-invitations")
    public ResponseEntity<Void> sendInvitations(@RequestBody EmailsRequestDto emailsRequestDto) {
        eventService.sendInvitations(emailsRequestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/gift-sent/{eventId}")
    public ResponseEntity<Void> giftSent(@PathVariable String eventId,
                                         @RequestBody ShuffleDto dto) {
        eventService.updateCardDetail(eventId, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/card/{eventId}")
    public ResponseEntity<Void> deleteCard(@PathVariable String eventId,
                                           @RequestBody RequestDto dto,
                                           Authentication authentication) throws Exception {
        eventService.deleteCard(eventId, dto, authentication);
        return ResponseEntity.ok().build();
    }
}
