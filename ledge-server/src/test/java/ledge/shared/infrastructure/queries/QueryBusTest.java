package ledge.shared.infrastructure.queries;

import ledge.security.api.IAuthorizationService;
import ledge.security.api.dto.PermissionDTO;
import ledge.security.api.exceptions.AuthorizationException;
import ledge.shared.security.models.Action;
import ledge.shared.security.models.Resource;
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

        TestQuery query = new TestQuery();
        String result = queryBus.dispatch(query, "valid-token");

        assertEquals("result", result);
    }

    @Test
    void testAuthorizationEnforcement() {
        TestHandler handler = new TestHandler();
        QueryBus queryBus = new QueryBus(List.of(handler), authService);

        PermissionDTO required = new PermissionDTO(Resource.USER, Action.READ);
        doThrow(new AuthorizationException("Denied")).when(authService).require("bad-token", required);

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
        @Override
        public Optional<PermissionDTO> getRequiredPermission() {
            return Optional.empty();
        }
    }

    static class TestQueryWithPermission implements Query<String> {
        @Override
        public Optional<PermissionDTO> getRequiredPermission() {
            return Optional.of(new PermissionDTO(Resource.USER, Action.READ));
        }
    }

    static class TestHandler implements QueryHandler<TestQuery, String> {
        @Override
        public String handle(TestQuery query) {
            return "result";
        }
    }
}
