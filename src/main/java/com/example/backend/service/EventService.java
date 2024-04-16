package com.example.backend.service;

import com.example.backend.dto.*;
import com.example.backend.model.Event;
import org.springframework.security.core.Authentication;
import java.util.List;


public interface EventService {
    EventDto createEvent(EventDto eventDTO, Authentication authentication);

    List<EventDto> getAllEvents(Authentication authentication);

    CardDto createCard(CardDto card, Authentication authentication);
    ShuffleDto shuffle(String eventId, Authentication authentication);

    ShuffleDto showMyReceiver(String eventId, Authentication authentication);

    EventDto getEvent(String eventId);

    void sendInvitations(EmailsRequestDto emailsRequestDto);
    void updateCardDetail(String event_id, ShuffleDto dto);

    void deleteCard(String event_id, RequestDto dto, Authentication authentication) throws Exception;
}
