package ledge.util.cqrs;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ledge.security.application.services.AuthorizationService;

public class QueryBus {

    private static class HandlerProxy {
        private final Object target;
        private final Method method;

        HandlerProxy(Object target, Method method) {
            this.target = target;
            this.method = method;
            this.method.setAccessible(true);
        }

        @SuppressWarnings("unchecked")
        <R> R invoke(Query<R> query) {
            try {
                return (R) method.invoke(target, query);
            } catch (IllegalAccessException | InvocationTargetException e) {
                if (e.getCause() instanceof RuntimeException) {
                    throw (RuntimeException) e.getCause();
                }
                throw new RuntimeException("Query execution failed for " + query.getClass().getSimpleName(),
                        e.getCause() != null ? e.getCause() : e);
            }
        }
    }

    private final Map<Class<? extends Query<?>>, HandlerProxy> handlers = new ConcurrentHashMap<>();
    private final AuthorizationService authService;

    public QueryBus(AuthorizationService authService) {
        this.authService = authService;
    }

    public void register(Object handler) {
        for (Method method : handler.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(QueryHandler.class) && method.getParameterCount() == 1) {
                Class<?> paramType = method.getParameterTypes()[0];
                if (Query.class.isAssignableFrom(paramType)) {
                    @SuppressWarnings("unchecked")
                    Class<? extends Query<?>> queryType = (Class<? extends Query<?>>) paramType;
                    if (handlers.containsKey(queryType)) {
                        throw new IllegalStateException(
                                "Multiple handlers registered for query: " + queryType.getName());
                    }
                    handlers.put(queryType, new HandlerProxy(handler, method));
                }
            }
        }
    }

    public <R> R dispatch(Query<R> query, String token) {
        // Authorization check
        query.getRequiredPermission().ifPresent(permission -> {
            authService.require(token, permission);
        });

        HandlerProxy proxy = handlers.get(query.getClass());
        if (proxy == null) {
            throw new IllegalStateException("No handler registered for query: " + query.getClass().getName());
        }
        return proxy.invoke(query);
    }
}
