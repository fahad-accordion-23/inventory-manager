package ledge.ui.viewmodels;

import javafx.beans.property.*;
import ledge.api.security.dto.RoleResponseDTO;
import ledge.api.users.dto.UserResponseDTO;

import java.util.UUID;

/**
 * JavaFX-friendly view model for User.
 * Updated to support single RoleDTO architecture and separate display
 * properties.
 */
public class UserViewModel {
    private final ObjectProperty<UUID> id = new SimpleObjectProperty<>();
    private final StringProperty username = new SimpleStringProperty();
    private final ObjectProperty<RoleResponseDTO> role = new SimpleObjectProperty<>();
    private final StringProperty roleName = new SimpleStringProperty();

    public UserViewModel() {
        setupBindings();
    }

    public UserViewModel(UserResponseDTO dto) {
        setupBindings();
        updateFrom(dto);
    }

    private void setupBindings() {
        // Sync roleName whenever the role object changes
        role.addListener((obs, oldVal, newVal) -> {
            roleName.set(newVal != null ? newVal.name() : "No Role");
        });
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

    public ObjectProperty<RoleResponseDTO> roleProperty() {
        return role;
    }

    public StringProperty roleNameProperty() {
        return roleName;
    }

    // --- Accessors ---
    public UUID getId() {
        return id.get();
    }

    public String getUsername() {
        return username.get();
    }

    public RoleResponseDTO getRole() {
        return role.get();
    }

    public String getRoleName() {
        return roleName.get();
    }

    public void setId(UUID id) {
        this.id.set(id);
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public void setRole(RoleResponseDTO role) {
        this.role.set(role);
    }
}
