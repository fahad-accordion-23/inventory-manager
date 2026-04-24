package ledge.api.shared;

/**
 * Standard wrapper for all API responses.
 * 
 * @param <T> The type of the data returned by the API.
 */
public record ApiResponse<T>(
        boolean success,
        T data,
        ErrorResponse error) {

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static <T> ApiResponse<T> error(String message, String code) {
        return new ApiResponse<>(false, null, new ErrorResponse(message, code));
    }
}
