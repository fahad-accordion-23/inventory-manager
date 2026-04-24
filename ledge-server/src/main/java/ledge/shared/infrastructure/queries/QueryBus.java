package ledge.shared.infrastructure.queries;

import org.springframework.core.GenericTypeResolver;
import org.springframework.stereotype.Component;
import ledge.security.api.IAuthorizationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class QueryBus {

    private final Map<Class<?>, QueryHandler<?, ?>> handlers = new HashMap<>();
    private final IAuthorizationService authService;

    /**
     * Spring automatically injects every bean that implements QueryHandler.
     */
    public QueryBus(List<QueryHandler<?, ?>> registeredHandlers, IAuthorizationService authService) {
        this.authService = authService;

        for (QueryHandler<?, ?> handler : registeredHandlers) {
            // Spring utility that safely resolves the generic <Q> type of the handler,
            // even if the handler is wrapped in a CGLIB proxy (e.g., @Transactional)
            Class<?>[] typeArgs = GenericTypeResolver.resolveTypeArguments(handler.getClass(), QueryHandler.class);

            if (typeArgs != null && typeArgs.length > 0) {
                Class<?> queryType = typeArgs[0];

                if (handlers.containsKey(queryType)) {
                    throw new IllegalStateException("Multiple handlers registered for query: " + queryType.getName());
                }
                handlers.put(queryType, handler);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <R, Q extends Query<R>> R dispatch(Q query, String token) {
        // Authorization check
        query.getRequiredPermission().ifPresent(permission -> {
            authService.require(token, permission);
        });

        // Fetch and execute handler
        QueryHandler<Q, R> handler = (QueryHandler<Q, R>) handlers.get(query.getClass());
        if (handler == null) {
            throw new IllegalStateException("No handler registered for query: " + query.getClass().getName());
        }

        return handler.handle(query);
    }
}