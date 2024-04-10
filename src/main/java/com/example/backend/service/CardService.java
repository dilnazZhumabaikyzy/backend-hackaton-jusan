package com.example.backend.service;
import com.example.backend.dto.CardDto;
import com.example.backend.model.Card;
import com.example.backend.model.User;

import java.util.List;

public interface CardService {
    List<CardDto> getCards(User user);
}
