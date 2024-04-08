package com.example.backend.service;
import com.example.backend.dto.CardDto;
import java.util.List;

public interface CardService {
    List<CardDto> getCards(Long userId);
}
