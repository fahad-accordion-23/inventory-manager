package ledge.users.application.services;

import ledge.shared.types.Role;
import ledge.users.domain.User;
import ledge.users.infrastructure.IUserRepository;
import ledge.util.PasswordHasher;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for managing users (adding, updating, removing).
 */
public class UserService implements IUserService {
    private final IUserRepository userRepository;

    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Adds a new user to the system.
     * 
     * @param user The user to add.
     */
    public void addUser(String username, String password, Role role) {
        String passwordHash = PasswordHasher.hash(password);
        User user = User.register(username, passwordHash, role);
        userRepository.save(user);
    }

    /**
     * Updates an existing user in the system.
     * 
     * @param user The user to update.
     */
    public void updateUser(UUID id, String username, String password, Role role) {
        String passwordHash = PasswordHasher.hash(password);
        User user = User.rehydrate(id, username, passwordHash, role);
        userRepository.update(user);
    }

    /**
     * Removes a user from the system by their ID.
     */
    @Override
    public void changeUserPassword(UUID id, String newPassword) {
        userRepository.findById(id).ifPresent(user -> {
            String passwordHash = PasswordHasher.hash(newPassword);
            User updatedUser = User.rehydrate(id, user.getUsername(), passwordHash, user.getRole());
            userRepository.update(updatedUser);
        });
    }

    @Override
    public void changeUserRole(UUID id, Role newRole) {
        userRepository.findById(id).ifPresent(user -> {
            User updatedUser = User.rehydrate(id, user.getUsername(), user.getPasswordHash(), newRole);
            userRepository.update(updatedUser);
        });
    }

    @Override
    public void changeUsername(UUID id, String newUsername) {
        userRepository.findById(id).ifPresent(user -> {
            User updatedUser = User.rehydrate(id, newUsername, user.getPasswordHash(), user.getRole());
            userRepository.update(updatedUser);
        });
    }

    /**
     * Removes a user from the system by their ID.
     * 
     * @param id The ID of the user to remove.
     */
    public void removeUser(UUID id) {
        userRepository.delete(id);
    }

    /**
     * Retrieves all users in the system.
     * 
     * @return A list of all users.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
