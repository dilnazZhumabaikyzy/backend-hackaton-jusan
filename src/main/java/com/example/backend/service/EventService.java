package com.example.backend.service;

import com.example.backend.dto.CardDto;
import com.example.backend.dto.EventDto;
import org.springframework.security.core.Authentication;
import java.util.List;


public interface EventService {
    EventDto createEvent(EventDto eventDTO, Authentication authentication);

    List<EventDto> getAllEvents(Authentication authentication);

    CardDto createCard(CardDto card, Authentication authentication);
}
