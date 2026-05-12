package com.qalikay.backend.security.dtos;

import lombok.Data;

@Data
public class AuthRequestDTO {
    private String username;
    private String password;
}
