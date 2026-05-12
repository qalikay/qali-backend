package com.qalikay.backend.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Expone ModelMapper como bean para inyectarlo en los controllers/services.
 * Lo usamos para convertir Entidad <-> DTO sin escribir mapeos manuales.
 */
@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
