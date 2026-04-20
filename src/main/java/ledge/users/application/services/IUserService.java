package ledge.users.application.services;

import ledge.shared.types.Role;
import ledge.users.domain.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IUserService {

    void addUser(String username, String password, Role role);

    void updateUser(UUID id, String username, String password, Role role);

    void changeUserPassword(UUID id, String newPassword);

    void changeUserRole(UUID id, Role newRole);

    void changeUsername(UUID id, String newUsername);

    void removeUser(UUID userId);

    List<User> getAllUsers();

    Optional<User> getUserById(UUID id);

    Optional<User> getUserByUsername(String username);
}