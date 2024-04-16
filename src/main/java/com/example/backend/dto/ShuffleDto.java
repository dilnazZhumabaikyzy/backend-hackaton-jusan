package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShuffleDto {
    private String receiverEmail;
    private String receiverName;
    private List<GiftDto> receiverGiftList;
    private boolean giftSent;
}
