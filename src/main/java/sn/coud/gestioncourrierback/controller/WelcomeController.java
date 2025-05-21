package sn.coud.gestioncourrierback.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for welcome message.
 */
@RestController
public class WelcomeController {
    
    @GetMapping("/")
    public Map<String, Object> welcome() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Bienvenue sur l'API de Gestion des Courriers");
        response.put("version", "1.0.0");
        response.put("documentation", "/swagger-ui.html");
        return response;
    }
}