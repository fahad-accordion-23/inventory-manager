package ledge.users.application.commands.handlers;

import ledge.users.application.commands.AddUserCommand;
import ledge.users.application.events.UserAddedEvent;
import ledge.users.application.services.UserService;
import ledge.users.domain.User;
import ledge.users.infrastructure.messaging.UserEventBroker;
import ledge.util.cqrs.CommandHandler;

import java.util.Optional;

public class AddUserCommandHandler {
    private final UserService userService;
    private final UserEventBroker eventBroker;

    public AddUserCommandHandler(UserService userService, UserEventBroker eventBroker) {
        this.userService = userService;
        this.eventBroker = eventBroker;
    }

    @CommandHandler
    public void handle(AddUserCommand command) {
        userService.addUser(command.username(), command.password(), command.role());

        Optional<User> userOpt = userService.getAllUsers().stream()
                .filter(u -> u.getUsername().equals(command.username()))
                .findFirst();

        userOpt.ifPresent(user -> eventBroker.publish(new UserAddedEvent(user.getId())));
    }
}
