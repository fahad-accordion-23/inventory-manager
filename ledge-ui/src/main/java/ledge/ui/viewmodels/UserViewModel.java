package ledge.ui.viewmodels;

import javafx.beans.property.*;
import ledge.api.users.dto.response.UserResponseDTO;
import ledge.security.api.dto.RoleDTO;

import java.util.UUID;

/**
 * JavaFX-friendly view model for User.
 * Updated to support single RoleDTO architecture.
 */
public class UserViewModel {
    private final ObjectProperty<UUID> id = new SimpleObjectProperty<>();
    private final StringProperty username = new SimpleStringProperty();
    private final ObjectProperty<RoleDTO> role = new SimpleObjectProperty<>();

    public UserViewModel() {
    }

    public UserViewModel(UserResponseDTO dto) {
        updateFrom(dto);
    }

    public static UserViewModel fromDTO(UserResponseDTO dto) {
        return new UserViewModel(dto);
    }

    public void updateFrom(UserResponseDTO dto) {
        this.id.set(dto.id());
        this.username.set(dto.username());
        this.role.set(dto.role());
    }

    public UserResponseDTO toDTO() {
        return new UserResponseDTO(id.get(), username.get(), role.get());
    }

    // --- Properties ---
    public ObjectProperty<UUID> idProperty() {
        return id;
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public ObjectProperty<RoleDTO> roleProperty() {
        return role;
    }

    // --- Accessors ---
    public UUID getId() {
        return id.get();
    }

    public String getUsername() {
        return username.get();
    }

    public RoleDTO getRole() {
        return role.get();
    }

    public void setId(UUID id) {
        this.id.set(id);
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public void setRole(RoleDTO role) {
        this.role.set(role);
    }
}
