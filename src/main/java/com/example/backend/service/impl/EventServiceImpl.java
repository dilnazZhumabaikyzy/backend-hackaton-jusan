package com.example.backend.service.impl;

import com.example.backend.dto.*;
import com.example.backend.model.*;
import com.example.backend.repository.*;
import com.example.backend.service.CardService;
import com.example.backend.service.EventService;
import com.example.backend.service.MailService;
import com.example.backend.util.AuthenticationUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

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
    private final SantaRepository santaRepository;
    private final MailServiceImpl mailService;
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
        List<Card> cardList = event.getCards();
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
        cardList.add(card);
        event.setCards(cardList);
        cardRepository.save(card);
        eventRepository.save(event);
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

    public SantaDto shuffle(ShuffleDto dto, Authentication authentication){
        User user = getUser(authentication);
        Event event = eventRepository.findEventById(dto.getId());
        List<Card> cardList = event.getCards();
        Collections.shuffle(cardList);
        List<SantaDto> santaDtoList = new ArrayList<>();
        SantaDto response = new SantaDto();
        for(int i = 0; i < cardList.size(); i++){
            Card currentCard = cardList.get(i);
            Card nextCard = cardList.get((i+1)%cardList.size());
            List<GiftDto> giftDtoList = transformToGiftDto(nextCard.getGifts());
            if(currentCard.getOwner() == user){
                 response = new SantaDto(currentCard.getOwner().getFullName(), nextCard.getOwner().getFullName(),
                         currentCard.getOwner().getEmail(), nextCard.getOwner().getEmail(), giftDtoList);
            }
            SantaDto santaDto = new SantaDto(currentCard.getOwner().getFullName(), nextCard.getOwner().getFullName(),
                    currentCard.getOwner().getEmail(),nextCard.getOwner().getEmail(),giftDtoList);
            User user1 = userRepository.getUserById(currentCard.getOwner().getId());
            User user2 = userRepository.getUserById(nextCard.getOwner().getId());
            Santa santa = new Santa(event, user1, user2);
            santaRepository.save(santa);
            santaDtoList.add(santaDto);
        }

        for(SantaDto santaDto : santaDtoList){
            String body = "Hi, dear participant. Now you are going to know who is your receiver: " + santaDto.getReceiverName()
                    + "\n" + "Here you can see the wish list of your receiver: " + santaDto.getGifts();
            mailService.sendSimpleMessage(santaDto.getSantaEmail(), "Please welcome your receiver", body);
        }

        return response;
    }

    public List<GiftDto> transformToGiftDto(List<Gift> giftList) {
        List<GiftDto> giftDtoList = new ArrayList<>();
        for (Gift gift : giftList) {
            giftDtoList.add(new GiftDto(gift));
        }
        return giftDtoList;
    }

    public SantaDto showMyReceiver(String evenId, Authentication authentication){
        User user = getUser(authentication);
        Event event = eventRepository.findEventById(evenId);
        Santa santa = santaRepository.getSantaBySantaUserAndEvent_Id(user, event.getId());
        Card card = cardRepository.getCardByOwnerAndEvent(santa.getReceiverUser(), event);
        List<GiftDto> giftDtoList = transformToGiftDto(card.getGifts());
        SantaDto santaDto = new SantaDto(santa.getReceiverUser().getFullName(),
                santa.getReceiverUser().getEmail(), giftDtoList);
        return santaDto;
    }
}
