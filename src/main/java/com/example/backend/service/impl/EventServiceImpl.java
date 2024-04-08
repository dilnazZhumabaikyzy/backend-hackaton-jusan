package com.example.backend.service.impl;

import com.example.backend.dto.CardDto;
import com.example.backend.dto.EventDto;
import com.example.backend.dto.GiftDto;
import com.example.backend.model.Card;
import com.example.backend.model.Event;
import com.example.backend.model.Gift;
import com.example.backend.model.User;
import com.example.backend.repository.CardRepository;
import com.example.backend.repository.EventRepository;
import com.example.backend.repository.GiftRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.CardService;
import com.example.backend.service.EventService;
import com.example.backend.util.AuthenticationUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Data
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final GiftRepository giftRepository;
    private final AuthenticationUtils authenticationUtils;
    private final CardService cardService;
    private int count = 0;

    @Override
    public EventDto createEvent(EventDto eventDTO, Authentication authentication) {
        Event event = new Event();
        event.setName(eventDTO.getName());
        String eventID = eventDTO.getIdentificator();
        if (eventID == null || eventID.isEmpty()) {
            eventID = generateUniqueId();
        }

        if (!isIdUnique(eventDTO.getIdentificator())) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
//                    body("This id is already exists in system. Please write another id");
        }

        event.setId(eventID);

        LocalDateTime currentTime = LocalDateTime.now();
        event.setCreatedAt(currentTime);
        if (eventDTO.getIsLimited()) {
            event.setPrice(eventDTO.getPrice());
        }
        event.setIsLimitSet(eventDTO.getIsLimited());
//        User user = userRepository.findUserByFullName(name);
        User user = getUser(authentication);
        event.setOwner(user);
        eventRepository.save(event);
        return new EventDto(event);
//        return ResponseEntity.status(HttpStatus.OK).body("Event created successfully!");

    }

    @Override
    public List<EventDto> getAllEvents(Authentication authentication) {
        User user = getUser(authentication);
        List<CardDto> cardList = cardService.getCards(user.getId());
        List<EventDto> eventDtoList = new ArrayList<>();
        List<Event> myEvents = eventRepository.findEventsByOwner(user);
        List<EventDto> myEventDtos = transformToDto(myEvents);
        if(cardList != null){
            for (CardDto cardDto : cardList) {
                Event tempEvent = eventRepository.findById(cardDto.getEvent_id()).orElseThrow();
                EventDto eventDto = new EventDto(tempEvent);
                eventDtoList.add(eventDto);
            }
        }

        if(myEventDtos != null){
            for (EventDto eventDto : myEventDtos) {
                eventDtoList.add(eventDto);
            }
        }

        return eventDtoList;
    }

    @Override
    public CardDto createCard(CardDto cardDto, Authentication authentication) {
        Event event = eventRepository.findById(cardDto.getEvent_id()).orElseThrow();
        User  owner = authenticationUtils.getUser(authentication);
        Card card = Card.builder().event(event).owner(owner).build();
        cardRepository.save(card);
        List<GiftDto> giftDtos = cardDto.getGifts();
        List<Gift> giftList  =  new ArrayList<>();
        for(GiftDto giftDto: giftDtos){
            Gift gift = Gift.builder()
                    .card(card)
                    .description(giftDto.getDescription())
                    .priority(giftDto.getPriority())
                    .build();
            giftRepository.save(gift);
            giftList.add(gift);
        }
        card.setGifts(giftList);
        cardRepository.save(card);
        return new CardDto(card);
    }

    public List<EventDto> transformToDto(List<Event> eventList) {
        List<EventDto> eventDtoList = new ArrayList<>();
        for (Event event : eventList) {
            eventDtoList.add(new EventDto(event));
        }
        return eventDtoList;
    }

    public Boolean isIdUnique(String id) {
        return !eventRepository.existsById(id);
    }

    public String generateUniqueId() {
        String id = "random" + count;
        setCount(count++);
        return id;
    }

    private User getUser(Authentication authentication) {
        long userId = 0;
        if (authentication != null && authentication.isAuthenticated()) {

            Object principal = authentication.getPrincipal();

            if (principal instanceof User user) {
                userId = user.getId();
            }
        }
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user;
    }

}
