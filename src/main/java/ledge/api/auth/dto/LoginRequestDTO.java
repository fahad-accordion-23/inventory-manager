package ledge.api.auth.dto;

public record LoginRequestDTO(
        String username,
        String password) {}
