package ledge.users.application.commands.handlers;

import ledge.users.application.commands.ChangeUserRoleCommand;
import ledge.users.application.events.UserRoleChanged;
import ledge.users.application.services.IUserService;
import ledge.users.infrastructure.messaging.UserEventBroker;
import ledge.util.cqrs.CommandHandler;

public class ChangeUserRoleCommandHandler {
    private final IUserService userService;
    private final UserEventBroker eventBroker;

    public ChangeUserRoleCommandHandler(IUserService userService, UserEventBroker eventBroker) {
        this.userService = userService;
        this.eventBroker = eventBroker;
    }

    @CommandHandler
    public void handle(ChangeUserRoleCommand command) {
        userService.changeUserRole(command.id(), command.newRole());
        eventBroker.publish(new UserRoleChanged(command.id()));
    }
}
