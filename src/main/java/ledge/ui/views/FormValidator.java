package ledge.ui.views;

import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Reusable form validation utility. Collects all field errors
 * so the user sees every problem at once rather than one at a time.
 */
public class FormValidator {

    private final List<String> errors = new ArrayList<>();

    public String requireNonBlank(TextField field, String label) {
        String val = field.getText() == null ? "" : field.getText().trim();
        if (val.isBlank()) {
            errors.add(label + " is required");
        }
        return val;
    }

    public BigDecimal requirePositiveDecimal(TextField field, String label) {
        String text = field.getText() == null ? "" : field.getText().trim();
        if (text.isBlank()) {
            errors.add(label + " is required");
            return null;
        }
        try {
            BigDecimal val = new BigDecimal(text);
            if (val.compareTo(BigDecimal.ZERO) <= 0) {
                errors.add(label + " must be positive");
            }
            return val;
        } catch (NumberFormatException e) {
            errors.add(label + " must be a valid number");
            return null;
        }
    }

    public int requireNonNegativeInt(TextField field, String label) {
        String text = field.getText() == null ? "" : field.getText().trim();
        if (text.isBlank()) {
            errors.add(label + " is required");
            return 0;
        }
        try {
            int val = Integer.parseInt(text);
            if (val < 0) {
                errors.add(label + " cannot be negative");
            }
            return val;
        } catch (NumberFormatException e) {
            errors.add(label + " must be a whole number");
            return 0;
        }
    }

    public BigDecimal optionalDecimalInRange(TextField field, String label, BigDecimal min, BigDecimal max) {
        String text = field.getText() == null ? "" : field.getText().trim();
        if (text.isBlank()) {
            return null;
        }
        try {
            BigDecimal val = new BigDecimal(text);
            if (val.compareTo(min) < 0 || val.compareTo(max) > 0) {
                errors.add(label + " must be between " + min + " and " + max);
            }
            return val;
        } catch (NumberFormatException e) {
            errors.add(label + " must be a valid number");
            return null;
        }
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public String getErrorSummary() {
        return String.join("\n", errors);
    }
}
