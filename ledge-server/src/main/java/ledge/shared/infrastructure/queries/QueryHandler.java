package ledge.shared.infrastructure.queries;

/**
 * Implemented by classes that handle specific Query types.
 * * @param
 * <Q>The specific Query type.
 * 
 * @param <R> The return type of the Query.
 */
public interface QueryHandler<Q extends Query<R>, R> {
    R handle(Q query);
}