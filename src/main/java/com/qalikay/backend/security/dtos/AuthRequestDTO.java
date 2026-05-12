package com.qalikay.backend.security.dtos;

import lombok.Data;

// DTO que recibe POST /api/authenticate. @Data (Lombok) genera getters/setters/equals/hash/toString.
@Data
public class AuthRequestDTO {
    private String username;
    private String password;
}
