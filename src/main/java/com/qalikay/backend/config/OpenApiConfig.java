package com.qalikay.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuracion de Swagger / OpenAPI 3.
 *
 * Personaliza la pagina de documentacion interactiva accesible en:
 *   http://localhost:8080/api/v1/swagger-ui.html
 *
 * Define el esquema de seguridad "bearerAuth" que permite probar
 * endpoints protegidos con JWT desde la propia interfaz.
 */
@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI qalikayOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("QaliKay API")
                        .description("Backend REST de QaliKay — plataforma de medicina natural " +
                                "que conecta clientes con expertos en medicina tradicional peruana.")
                        .version("0.0.1")
                        .contact(new Contact()
                                .name("Equipo QaliKay")
                                .email("contacto@qalikay.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server().url("http://localhost:8080/api/v1").description("Entorno local")
                ))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Pega aqui el accessToken obtenido en /auth/login")));
    }
}
