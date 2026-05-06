package com.qalikay.backend.security.dtos;

import lombok.Data;

import java.util.Set;

@Data
public class AuthResponseDTO {
    private String jwt;
    private String username;
    private Set<String> roles;
}
