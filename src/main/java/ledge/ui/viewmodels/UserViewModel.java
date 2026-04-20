package ledge.ui.viewmodels;

import javafx.beans.property.*;
import ledge.shared.types.Role;
import ledge.users.application.dtos.UserDTO;

import java.util.UUID;

/**
 * JavaFX-friendly view model for User.
 */
public class UserViewModel {
    private final ObjectProperty<UUID> id = new SimpleObjectProperty<>();
    private final StringProperty username = new SimpleStringProperty();
    private final ObjectProperty<Role> role = new SimpleObjectProperty<>();

    public UserViewModel() {
    }

    public UserViewModel(UserDTO dto) {
        updateFrom(dto);
    }

    public static UserViewModel fromDTO(UserDTO dto) {
        return new UserViewModel(dto);
    }

    public void updateFrom(UserDTO dto) {
        this.id.set(dto.id());
        this.username.set(dto.username());
        this.role.set(dto.role());
    }

    public UserDTO toDTO() {
        return new UserDTO(id.get(), username.get(), role.get());
    }

    // --- Properties ---
    public ObjectProperty<UUID> idProperty() {
        return id;
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public ObjectProperty<Role> roleProperty() {
        return role;
    }

    // --- Accessors ---
    public UUID getId() {
        return id.get();
    }

    public String getUsername() {
        return username.get();
    }

    public Role getRole() {
        return role.get();
    }

    public void setId(UUID id) {
        this.id.set(id);
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public void setRole(Role role) {
        this.role.set(role);
    }
}
