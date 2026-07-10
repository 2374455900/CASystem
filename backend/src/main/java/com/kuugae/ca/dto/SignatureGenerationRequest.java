package com.kuugae.ca.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignatureGenerationRequest {
    
    @NotBlank(message = "原始数据不能为空")
    private String data;
    
    @NotBlank(message = "证书序列号不能为空")
    private String serialNumber;
}
