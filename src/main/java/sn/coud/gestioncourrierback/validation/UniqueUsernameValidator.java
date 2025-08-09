package sn.coud.gestioncourrierback.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sn.coud.gestioncourrierback.repository.UserRepository;

/**
 * Validator for UniqueUsername annotation.
 */
@Component
@RequiredArgsConstructor
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    private final UserRepository userRepository;

    @Override
    public void initialize(UniqueUsername constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (username == null || username.trim().isEmpty()) {
            return true; // Let @NotBlank handle null/empty validation
        }
        
        return !userRepository.existsByUsername(username);
    }
}

