package ledge.shared.infrastructure.queries;

import ledge.security.api.IAuthorizationService;
import ledge.security.api.exceptions.AuthorizationException;
import ledge.security.api.annotations.RequiresPermission;
import ledge.shared.security.models.Action;
import ledge.shared.security.models.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

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

        TestQuery query = new TestQuery();
        String result = queryBus.dispatch(query, "valid-token");

        assertEquals("result", result);
        verify(authService).authorize(eq("valid-token"), eq(query));
    }

    @Test
    void testAuthorizationEnforcement() {
        TestHandlerWithPermission handler = new TestHandlerWithPermission();
        QueryBus queryBus = new QueryBus(List.of(handler), authService);

        doThrow(new AuthorizationException("Denied")).when(authService).authorize(eq("bad-token"), any(Object.class));

        TestQueryWithPermission query = new TestQueryWithPermission();

        assertThrows(AuthorizationException.class, () -> queryBus.dispatch(query, "bad-token"));
    }

    @Test
    void testMissingHandler() {
        QueryBus queryBus = new QueryBus(List.of(), authService);
        assertThrows(IllegalStateException.class, () -> queryBus.dispatch(new TestQuery(), "token"));
    }

    // Test classes
    static class TestQuery implements Query<String> {
    }

    @RequiresPermission(resource = Resource.USER, action = Action.READ)
    static class TestQueryWithPermission implements Query<String> {
    }

    static class TestHandler implements QueryHandler<TestQuery, String> {
        @Override
        public String handle(TestQuery query) {
            return "result";
        }
    }

    static class TestHandlerWithPermission implements QueryHandler<TestQueryWithPermission, String> {
        @Override
        public String handle(TestQueryWithPermission query) {
            return "result";
        }
    }
}
