package com.example.backend.service.impl;

import com.example.backend.dto.*;
import com.example.backend.exception.DuplicateKeyException;
import com.example.backend.exception.InvalidEmailException;
import com.example.backend.exception.InvalidEventException;
import com.example.backend.model.*;
import com.example.backend.repository.*;
import com.example.backend.service.CardService;
import com.example.backend.service.EventService;
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
    private Logger logger;

    @Override
    public EventDto createEvent(EventDto eventDTO, Authentication authentication) {
        Event event = new Event();
        event.setName(eventDTO.getName());
        String eventID = eventDTO.getIdentificator();
        if (eventID.trim().equals("")) {
            throw new NullPointerException("Идентификатор не должен быть пустым");
        }
        if (!isIdUnique(eventDTO.getIdentificator())) {
            throw new DuplicateKeyException("Игра с таким идентификатором существует. Попробуйте другое название");
        }
        event.setId(eventID);

        LocalDateTime currentTime = LocalDateTime.now();
        event.setCreatedAt(currentTime);
        if (eventDTO.getIsLimited()) {
            if (eventDTO.getPrice() > 0) {
                event.setPrice(eventDTO.getPrice());
            } else {
                throw new IllegalArgumentException("Укажите лимит больше 0");
            }
            event.setCurrency(eventDTO.getCurrencyType());
        }
        event.setIsLimitSet(eventDTO.getIsLimited());
        User user = getUser(authentication);
        event.setOwner(user);

        event.setActive(true);
        eventRepository.save(event);

        return new EventDto(event);
    }

    @Override
    public List<EventDto> getAllEvents(Authentication authentication) {
        Set<EventDto> eventDtoHashSet = new HashSet<>();

        User user = getUser(authentication);

        List<EventDto> eventDtoList = new ArrayList<>();
        List<CardDto> cardList = cardService.getCards(user);

        List<Event> myEvents = eventRepository.findEventsByOwner(user);
        List<EventDto> myEventDtos = transformToDto(myEvents);

        if (cardList != null) {
            for (CardDto cardDto : cardList) {
                Event tempEvent = eventRepository.findById(cardDto.getEvent_id()).orElseThrow();
                EventDto eventDto = new EventDto(tempEvent);
                eventDtoHashSet.add(eventDto);
            }
        }

        if (myEventDtos != null) {
            eventDtoHashSet.addAll(myEventDtos);
        }
        eventDtoList.addAll(eventDtoHashSet);
        return eventDtoList;
    }


    @Override
    public CardDto createCard(CardDto cardDto, Authentication authentication) {
        Event event = eventRepository.findById(cardDto.getEvent_id()).
                orElseThrow(() -> new InvalidEventException(cardDto.getEvent_id()));
        List<Card> cardList = event.getCards();
        User owner = authenticationUtils.getUser(authentication);
        Card card = Card.builder().event(event).owner(owner).build();
        cardRepository.save(card);
        List<GiftDto> giftDtos = cardDto.getGifts();
        List<Gift> giftList = new ArrayList<>();
        for (GiftDto giftDto : giftDtos) {
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
        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
        return user;
    }

    public ShuffleDto shuffle(String eventId, Authentication authentication) {
        Event event = eventRepository.findEventById(eventId);
        if (!event.isActive()) {
            throw new InvalidEventException();
        }
        User user = getUser(authentication);
        if (!eventRepository.existsById(eventId)) {
            throw new InvalidEventException(eventId);
        }


        ShuffleDto shuffleDto = new ShuffleDto();

        List<Card> cardList = event.getCards();
        Collections.shuffle(cardList);

        for (int i = 0; i < cardList.size(); i++) {
            Card currentCard = cardList.get(i);
            Card nextCard = cardList.get((i + 1) % cardList.size());
            SantaDto santaDto = new SantaDto(currentCard, nextCard);

            if (currentCard.getOwner() == user) {
                shuffleDto = ShuffleDto.builder()
                        .receiverEmail(santaDto.getReceiverEmail())
                        .receiverName(santaDto.getReceiverName())
                        .receiverGiftList(transformToGiftDto(nextCard.getGifts()))
                        .build();
            }

            Santa santa = new Santa(event, currentCard.getOwner(), nextCard.getOwner());
            santaRepository.save(santa);
            event.setActive(false);
            eventRepository.save(event);
            mailService.sendSantaMessage(santaDto.getSantaEmail(), nextCard);
        }

        return shuffleDto;
    }

    public List<GiftDto> transformToGiftDto(List<Gift> giftList) {
        List<GiftDto> giftDtoList = new ArrayList<>();
        for (Gift gift : giftList) {
            giftDtoList.add(new GiftDto(gift));
        }
        return giftDtoList;
    }

    public ShuffleDto showMyReceiver(String eventId, Authentication authentication) {
        User user = getUser(authentication);
        if (!eventRepository.existsById(eventId)) {
            throw new InvalidEventException(eventId);
        }
        Event event = eventRepository.findEventById(eventId);
        Santa santa = santaRepository.getSantaBySantaUserAndEvent_Id(user, event.getId());
        if(santa == null){
            throw new NoSuchElementException("У вас нету получателя");
        }

        Card card = cardRepository.getCardByOwnerAndEvent(santa.getReceiverUser(), event);
        List<GiftDto> giftDtoList = transformToGiftDto(card.getGifts());


        ShuffleDto shuffleDto = ShuffleDto.builder()
                .receiverEmail(santa.getReceiverUser().getEmail())
                .receiverName(santa.getReceiverUser().getFullName())
                .receiverGiftList(giftDtoList)
                .build();

        shuffleDto.setGiftSent(card.isSent());
        return shuffleDto;
    }

    @Override
    public EventDto getEvent(String eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new InvalidEventException(eventId));
        List<Card> cardList = event.getCards();
        List<String> emailList = new ArrayList<>();
        for (Card card : cardList) {
            emailList.add(card.getOwner().getEmail());
        }

        EventDto eventDto = new EventDto(event, emailList);
        return eventDto;
    }

    @Override
    public void sendInvitations(EmailsRequestDto emailsRequestDto) {
        String[] emails = emailsRequestDto.getEmails();
        String event_id = emailsRequestDto.getEvent_id();

        for (String email : emails) {
            mailService.sendInvitationMessage(email, event_id);
        }
    }

    @Override
    public void updateCardDetail(String event_id, ShuffleDto dto) {
        Event event = eventRepository.findById(event_id).orElseThrow(() -> new InvalidEventException(event_id));
        User user = userRepository.findByEmail(dto.getReceiverEmail())
                .orElseThrow(() -> new InvalidEmailException(dto.getReceiverEmail()));
        Card card = cardRepository.getCardByOwnerAndEvent(user, event);
        card.setSent(true);
        cardRepository.save(card);
        mailService.sendGiftSentMessage(dto.getReceiverEmail());

    }

    @Override
    public void deleteCard(String event_id, RequestDto dto, Authentication authentication) throws Exception {
        User current_user = getUser(authentication);
        Event event = eventRepository.findById(event_id).orElseThrow(() -> new InvalidEventException(event_id));
        if (event.getOwner() != current_user) {
            throw new Exception("Только владелец игры может удалять участников");
        }
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new InvalidEmailException(dto.getEmail()));
        Card card = cardRepository.getCardByOwnerAndEvent(user, event);
        cardRepository.delete(card);
    }
}
