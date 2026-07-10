package com.kuugae.ca.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CertificateRevokeRequest {

    @NotBlank(message = "证书序列号不能为空")
    private String serialNumber;

    @NotBlank(message = "验证码不能为空")
    private String checkCode;

    @NotBlank(message = "验证密码不能为空")
    private String revokePassword;

    private String signSerialNumber;
    private String signRevokePassword;
}
