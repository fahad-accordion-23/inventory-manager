package ledge.users.application.commands.handlers;

import ledge.users.application.commands.ChangeUsernameCommand;
import ledge.users.application.events.UsernameChanged;
import ledge.users.application.services.IUserService;
import ledge.users.infrastructure.messaging.UserEventBroker;
import ledge.util.cqrs.CommandHandler;

public class ChangeUsernameCommandHandler {
    private final IUserService userService;
    private final UserEventBroker eventBroker;

    public ChangeUsernameCommandHandler(IUserService userService, UserEventBroker eventBroker) {
        this.userService = userService;
        this.eventBroker = eventBroker;
    }

    @CommandHandler
    public void handle(ChangeUsernameCommand command) {
        userService.changeUsername(command.id(), command.newUsername());
        eventBroker.publish(new UsernameChanged(command.id()));
    }
}
