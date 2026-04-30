package com.qalikay.backend.shared.health;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Controlador de salud del backend.
 *
 * Sirve para verificar que el servidor esta activo y respondiendo.
 * Lo usan herramientas de monitoreo (Docker, Kubernetes, UptimeRobot, etc.)
 * para detectar caidas del servicio.
 */
@RestController
@RequestMapping("/health")
@Tag(name = "Health", description = "Verificacion de estado del backend")
public class HealthController {

    @GetMapping
    @Operation(
            summary = "Estado del backend",
            description = "Devuelve el estado actual del servicio. Util para monitoreo y health checks."
    )
    public Map<String, Object> health() {
        return Map.of(
                "status", "UP",
                "service", "qali-backend",
                "version", "0.0.1",
                "timestamp", LocalDateTime.now(),
                "message", "QaliKay backend esta corriendo correctamente"
        );
    }
}
