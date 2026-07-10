package com.kuugae.ca.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Data;

@Data
public class CertificateApplyRequest {

    @NotBlank(message = "组织机构名称不能为空")
    private String organization;

    @NotBlank(message = "工商注册号不能为空")
    private String registrationNumber;

    @NotBlank(message = "法人姓名不能为空")
    private String juridicalPerson;

    @NotBlank(message = "经办人不能为空")
    private String chargePerson;

    @NotBlank(message = "经办人电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String chargePhone;

    @Min(value = 7, message = "有效期最少 7 天")
    @Max(value = 1825, message = "有效期最多 5 年")
    private int validityPeriod = 365;

    @NotBlank(message = "密钥算法不能为空")
    @Pattern(regexp = "^(RSA|EC)$", message = "密钥算法只支持 RSA 或 EC")
    private String keyAlgorithm = "RSA";

    @Min(value = 256, message = "密钥长度最小 256 位")
    private int keySize = 2048;

    @NotBlank(message = "验证码不能为空")
    private String checkCode;
}
