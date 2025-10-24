package sn.coud.gestioncourrierback.exception;

import java.util.List;

/**
 * Exception métier spécifique aux règles de validation des suivis.
 */
public class SuiviBusinessException extends RuntimeException {

    private final List<String> validationErrors;

    public SuiviBusinessException(String message) {
        super(message);
        this.validationErrors = null;
    }

    public SuiviBusinessException(String message, Throwable cause) {
        super(message, cause);
        this.validationErrors = null;
    }

    public SuiviBusinessException(String message, List<String> validationErrors) {
        super(message);
        this.validationErrors = validationErrors;
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }

    public boolean hasValidationErrors() {
        return validationErrors != null && !validationErrors.isEmpty();
    }
}
