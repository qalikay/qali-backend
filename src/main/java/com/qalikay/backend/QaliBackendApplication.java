package com.qalikay.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de arranque.
 *
 * @SpringBootApplication combina 3 anotaciones:
 *   - @Configuration:          permite definir @Bean
 *   - @EnableAutoConfiguration: autoconfigura todo (Tomcat, JPA, Security) segun el classpath
 *   - @ComponentScan:          escanea com.qalikay.backend.** buscando @Component, @Service, @Repository, @Controller
 */
@SpringBootApplication
public class QaliBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(QaliBackendApplication.class, args);
    }

}
