package sn.coud.gestioncourrierback.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sn.coud.gestioncourrierback.repository.UserRepository;

/**
 * Validator for UniqueEmail annotation.
 */
@Component
@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final UserRepository userRepository;

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.trim().isEmpty()) {
            return true; // Let @NotBlank and @Email handle null/empty validation
        }
        
        return !userRepository.existsByEmail(email);
    }
}

