package ledge.shared.infrastructure.commands;

import org.springframework.core.GenericTypeResolver;
import org.springframework.stereotype.Component;
import ledge.security.application.services.IAuthorizationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CommandBus {

    private final Map<Class<?>, CommandHandler<?>> handlers = new HashMap<>();
    private final IAuthorizationService authService;

    /**
     * Spring automatically injects every bean that implements CommandHandler.
     */
    public CommandBus(List<CommandHandler<?>> registeredHandlers, IAuthorizationService authService) {
        this.authService = authService;

        for (CommandHandler<?> handler : registeredHandlers) {
            // Spring utility to safely extract the <C> type from the handler
            Class<?>[] typeArgs = GenericTypeResolver.resolveTypeArguments(handler.getClass(), CommandHandler.class);

            if (typeArgs != null && typeArgs.length > 0) {
                Class<?> commandType = typeArgs[0];

                if (handlers.containsKey(commandType)) {
                    throw new IllegalStateException(
                            "Multiple handlers registered for command: " + commandType.getName());
                }
                handlers.put(commandType, handler);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <C extends Command> void dispatch(C command, String token) {
        // Authorization check
        command.getRequiredPermission().ifPresent(permission -> {
            authService.require(token, permission);
        });

        // Fetch and execute handler
        CommandHandler<C> handler = (CommandHandler<C>) handlers.get(command.getClass());
        if (handler == null) {
            throw new IllegalStateException("No handler registered for command: " + command.getClass().getName());
        }

        handler.handle(command);
    }
}