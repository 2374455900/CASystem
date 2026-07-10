package com.kuugae.ca.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValidationResult {
    private Boolean valid;
    private String message;
    private String details;
}
