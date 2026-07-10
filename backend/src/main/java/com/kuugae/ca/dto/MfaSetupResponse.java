package com.kuugae.ca.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MfaSetupResponse {
    private String secret;
    private String qrCodeUri;
    private String manualEntryKey;
}
