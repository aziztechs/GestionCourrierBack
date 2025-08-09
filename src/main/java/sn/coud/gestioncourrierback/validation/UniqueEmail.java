package sn.coud.gestioncourrierback.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Validation annotation to check if email is unique.
 */
@Documented
@Constraint(validatedBy = UniqueEmailValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmail {
    String message() default "Cette adresse email existe déjà";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

