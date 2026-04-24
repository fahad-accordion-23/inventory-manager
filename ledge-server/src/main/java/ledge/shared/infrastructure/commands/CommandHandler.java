package ledge.shared.infrastructure.commands;

/**
 * Implemented by classes that handle specific Command types.
 *
 * @param <C> The specific Command type this handler processes.
 */
public interface CommandHandler<C extends Command> {
    void handle(C command);
}