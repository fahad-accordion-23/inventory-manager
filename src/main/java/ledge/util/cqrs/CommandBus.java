package ledge.util.cqrs;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CommandBus {
    
    private static class HandlerProxy {
        private final Object target;
        private final Method method;

        HandlerProxy(Object target, Method method) {
            this.target = target;
            this.method = method;
            this.method.setAccessible(true);
        }

        void invoke(Command command) {
            try {
                method.invoke(target, command);
            } catch (IllegalAccessException | InvocationTargetException e) {
                if (e.getCause() instanceof RuntimeException) {
                    throw (RuntimeException) e.getCause();
                }
                throw new RuntimeException("Command execution failed for " + command.getClass().getSimpleName(), e.getCause() != null ? e.getCause() : e);
            }
        }
    }

    private final Map<Class<? extends Command>, HandlerProxy> handlers = new ConcurrentHashMap<>();

    public void register(Object handler) {
        for (Method method : handler.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(CommandHandler.class) && method.getParameterCount() == 1) {
                Class<?> paramType = method.getParameterTypes()[0];
                if (Command.class.isAssignableFrom(paramType)) {
                    @SuppressWarnings("unchecked")
                    Class<? extends Command> commandType = (Class<? extends Command>) paramType;
                    if (handlers.containsKey(commandType)) {
                        throw new IllegalStateException("Multiple handlers registered for command: " + commandType.getName());
                    }
                    handlers.put(commandType, new HandlerProxy(handler, method));
                }
            }
        }
    }

    public void dispatch(Command command) {
        HandlerProxy proxy = handlers.get(command.getClass());
        if (proxy == null) {
            throw new IllegalStateException("No handler registered for command: " + command.getClass().getName());
        }
        proxy.invoke(command);
    }
}
