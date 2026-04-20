package ledge.users.application.commands.handlers;

import ledge.users.application.commands.ChangeUserPasswordCommand;
import ledge.users.application.events.UserPasswordChanged;
import ledge.users.application.services.IUserService;
import ledge.users.infrastructure.messaging.UserEventBroker;
import ledge.util.cqrs.CommandHandler;

public class ChangeUserPasswordCommandHandler {
    private final IUserService userService;
    private final UserEventBroker eventBroker;

    public ChangeUserPasswordCommandHandler(IUserService userService, UserEventBroker eventBroker) {
        this.userService = userService;
        this.eventBroker = eventBroker;
    }

    @CommandHandler
    public void handle(ChangeUserPasswordCommand command) {
        userService.changeUserPassword(command.id(), command.newPassword());
        eventBroker.publish(new UserPasswordChanged(command.id()));
    }
}
