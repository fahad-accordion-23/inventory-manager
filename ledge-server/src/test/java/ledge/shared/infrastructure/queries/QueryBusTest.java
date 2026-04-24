package ledge.shared.infrastructure.queries;

import ledge.security.application.services.IAuthorizationService;
import ledge.security.application.events.AuthorizationException;
import ledge.shared.types.Action;
import ledge.shared.types.Permission;
import ledge.shared.types.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QueryBusTest {

    private IAuthorizationService authService;

    @BeforeEach
    void setUp() {
        authService = mock(IAuthorizationService.class);
    }

    @Test
    void testDispatch() {
        TestHandler handler = new TestHandler();
        QueryBus queryBus = new QueryBus(List.of(handler), authService);

        TestQuery query = new TestQuery("request");
        String result = queryBus.dispatch(query, "valid-token");

        assertEquals("response for request", result);
    }

    @Test
    void testAuthorizationEnforcement() {
        TestHandlerWithPermission handler = new TestHandlerWithPermission();
        QueryBus queryBus = new QueryBus(List.of(handler), authService);

        doThrow(new AuthorizationException("Denied")).when(authService).require("bad-token", new Permission(Resource.USER, Action.READ));

        TestQueryWithPermission query = new TestQueryWithPermission();
        
        assertThrows(AuthorizationException.class, () -> queryBus.dispatch(query, "bad-token"));
    }

    static class TestQuery implements Query<String> {
        final String data;
        TestQuery(String data) { this.data = data; }
        @Override public Optional<Permission> getRequiredPermission() { return Optional.empty(); }
    }

    static class TestQueryWithPermission implements Query<String> {
        @Override public Optional<Permission> getRequiredPermission() { return Optional.of(new Permission(Resource.USER, Action.READ)); }
    }

    static class TestHandler implements QueryHandler<TestQuery, String> {
        @Override
        public String handle(TestQuery query) {
            return "response for " + query.data;
        }
    }

    static class TestHandlerWithPermission implements QueryHandler<TestQueryWithPermission, String> {
        @Override
        public String handle(TestQueryWithPermission query) {
            return "secure data";
        }
    }
}
