package sn.coud.gestioncourrierback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.context.annotation.Bean;

/**
 * Main application class for Gestion Courrier Backend.
 */
@SpringBootApplication
@EnableScheduling
public class GestionCourrierBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestionCourrierBackApplication.class, args);
        System.out.println("Gestion Courrier Backend is running!");
    }

    /**
     * Configure CORS for the application.
     * 
     * @return WebMvcConfigurer
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                        .allowedHeaders("*")
                        .maxAge(3600);
            }
        };
    }
}
