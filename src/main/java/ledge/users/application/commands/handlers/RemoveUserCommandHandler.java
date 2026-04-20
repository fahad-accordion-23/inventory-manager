package ledge.users.application.commands.handlers;

import ledge.users.application.commands.RemoveUserCommand;
import ledge.users.application.events.UserRemovedEvent;
import ledge.users.application.services.UserService;
import ledge.users.infrastructure.messaging.UserEventBroker;
import ledge.util.cqrs.CommandHandler;

public class RemoveUserCommandHandler {
    private final UserService userService;
    private final UserEventBroker eventBroker;

    public RemoveUserCommandHandler(UserService userService, UserEventBroker eventBroker) {
        this.userService = userService;
        this.eventBroker = eventBroker;
    }

    @CommandHandler
    public void handle(RemoveUserCommand command) {
        userService.removeUser(command.id());
        eventBroker.publish(new UserRemovedEvent(command.id()));
    }
}
