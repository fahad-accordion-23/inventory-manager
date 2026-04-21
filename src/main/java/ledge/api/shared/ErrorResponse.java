package ledge.api.shared;

/**
 * Standard error response format for the API layer.
 */
public record ErrorResponse(
        String message,
        String code) {}
