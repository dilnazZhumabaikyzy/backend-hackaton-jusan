package com.example.backend.service.impl;

import com.example.backend.dto.CardDto;
import com.example.backend.model.Card;
import com.example.backend.model.User;
import com.example.backend.repository.CardRepository;
import com.example.backend.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    @Override
    public List<CardDto> getCards(User user) {
        List<CardDto> cardDtoList = new ArrayList<>();
        List<Card> cardList = cardRepository.getCardsByOwner(user);
        for(Card card : cardList){
            cardDtoList.add(new CardDto(card));
        }
        return cardDtoList;
    }
}
