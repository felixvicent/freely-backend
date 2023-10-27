package com.freely.backend.suggestion.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class SuggestionDTO {
    private String label;
    private UUID value;
}
