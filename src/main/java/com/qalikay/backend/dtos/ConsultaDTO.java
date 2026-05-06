package com.qalikay.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaDTO {
    private Long id;
    private String asunto;
    private String estado;
    private LocalDateTime fechaCreacion;
    private ClienteDTO cliente;
    private ExpertoDTO experto;
    private List<MensajeDTO> mensajes;
}
