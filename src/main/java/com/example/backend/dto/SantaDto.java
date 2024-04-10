package com.example.backend.dto;

import com.example.backend.model.Card;
import com.example.backend.model.Gift;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SantaDto {
    private String santaName;
    private String receiverName;
    private String santaEmail;
    private String receiverEmail;


    public SantaDto(String receiverName, String receiverEmail) {
        this.receiverName = receiverName;
        this.receiverEmail = receiverEmail;

    }

    public SantaDto(Card currentCard, Card nextCard) {
        this.santaName = currentCard.getOwner().getFullName();
        this.santaEmail = currentCard.getOwner().getEmail();

        this.receiverName = nextCard.getOwner().getFullName();
        this.receiverEmail = nextCard.getOwner().getEmail();
    }
}
