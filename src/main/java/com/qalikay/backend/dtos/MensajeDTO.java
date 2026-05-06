package com.qalikay.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MensajeDTO {
    private Long id;
    private String contenido;
    private String remitente;
    private LocalDateTime fechaEnvio;
}
