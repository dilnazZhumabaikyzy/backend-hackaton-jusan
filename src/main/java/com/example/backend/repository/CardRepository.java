package com.example.backend.repository;

import com.example.backend.dto.CardDto;
import com.example.backend.dto.EventDto;
import com.example.backend.model.Card;
import com.example.backend.model.Event;
import com.example.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    Card getCardByOwnerAndEvent(User user, Event event);
    List<Card> getCardsByOwner(User user);
}
