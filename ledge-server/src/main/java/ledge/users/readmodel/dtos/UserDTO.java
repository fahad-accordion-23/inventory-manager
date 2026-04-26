package ledge.users.readmodel.dtos;

import com.google.gson.annotations.SerializedName;
import ledge.users.writemodel.domain.User;

import java.util.UUID;

public record UserDTO(
        UUID id,
        String username,
        @SerializedName("passwordHash") String hashedPassword) {

    public static UserDTO fromUser(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getPasswordHash());
    }
}
