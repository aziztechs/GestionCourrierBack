package sn.coud.gestioncourrierback.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Configuration pour l'audit JPA automatique.
 * Active l'audit des entités avec @CreatedDate, @LastModifiedDate, @CreatedBy, @LastModifiedBy
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditConfig {

    /**
     * Fournit l'utilisateur actuel pour l'audit automatique.
     * Pour l'instant, retourne "system" par défaut.
     * À adapter selon votre système d'authentification.
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() || 
                "anonymousUser".equals(authentication.getPrincipal())) {
                return Optional.of("system");
            }
            
            // Si vous avez un système d'authentification personnalisé,
            // adaptez cette partie pour récupérer l'utilisateur connecté
            return Optional.of(authentication.getName());
        };
    }
}
