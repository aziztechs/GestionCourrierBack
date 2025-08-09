package sn.coud.gestioncourrierback.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Validation annotation to check if username is unique.
 */
@Documented
@Constraint(validatedBy = UniqueUsernameValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUsername {
    String message() default "Ce nom d'utilisateur existe déjà";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

