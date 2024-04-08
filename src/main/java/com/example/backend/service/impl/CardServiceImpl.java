package com.example.backend.service.impl;

import com.example.backend.dto.CardDto;
import com.example.backend.model.Card;
import com.example.backend.service.CardService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardServiceImpl implements CardService {
    @Override
    public List<CardDto> getCards(Long userId) {
        return null;
    }
}
