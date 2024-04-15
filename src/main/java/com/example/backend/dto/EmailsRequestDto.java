package com.example.backend.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EmailsRequestDto {
    private String[] emails;
    private String event_id;
}