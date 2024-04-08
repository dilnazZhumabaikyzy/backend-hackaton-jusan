package com.example.backend.dto;

import com.example.backend.model.Card;
import com.example.backend.model.Event;
import com.example.backend.model.Gift;
import com.example.backend.model.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDto {
    private Long id;
    private Long owner_id;
    private String event_id;
    private List<GiftDto> gifts;

    public CardDto(Card card) {
        this.id = card.getId();
        this.owner_id = card.getOwner().getId();
        this.event_id = card.getEvent().getId();
    }
}
