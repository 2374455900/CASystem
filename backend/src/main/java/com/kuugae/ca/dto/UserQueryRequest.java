package com.kuugae.ca.dto;

import lombok.Data;

@Data
public class UserQueryRequest {
    private String keyword;
    private String authority;
    private String sortField = "createdAt";
    private String sortOrder = "desc";
    private Integer page = 1;
    private Integer pageSize = 10;
}
