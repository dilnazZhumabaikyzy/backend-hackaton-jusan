package com.example.backend.dto;

import com.example.backend.model.CurrencyType;
import com.example.backend.model.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {
    private String name;
    private String owner_email;
    private String identificator;
    private Boolean isLimited;
    private int price;
    private CurrencyType currencyType;
    private boolean active;
    private int playersNumber;

    public EventDto(Event event) {
        this.name = event.getName();

        if(event.getOwner() != null){
        this.owner_email = event.getOwner().getEmail();}

        this.identificator = event.getId();
        this.isLimited = event.getIsLimitSet();
        this.price = event.getPrice();
        this.currencyType = event.getCurrency();
        this.active = event.isActive();
        this.playersNumber = event.getCards().size();
    }
}
