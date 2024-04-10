package com.example.backend.dto;

import com.example.backend.model.Gift;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GiftDto {
    private String description;
    private Long card_id;
    private int priority;
    public GiftDto() {}

    public GiftDto(Gift gift){
        this.description = gift.getDescription();
        if(gift != null){
            this.card_id = gift.getCard().getId();
        }
        this.priority = gift.getPriority();
    }
}
