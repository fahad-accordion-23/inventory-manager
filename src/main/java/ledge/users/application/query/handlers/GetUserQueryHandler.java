package ledge.users.application.query.handlers;

import ledge.users.application.dtos.UserDTO;
import ledge.users.application.query.GetUserByUsernameQuery;
import ledge.users.application.services.UserService;
import ledge.util.cqrs.QueryHandler;

import java.util.Optional;

public class GetUserQueryHandler {
    private final UserService userService;

    public GetUserQueryHandler(UserService userService) {
        this.userService = userService;
    }

    @QueryHandler
    public Optional<UserDTO> handle(GetUserByUsernameQuery query) {
        return userService.getUserByUsername(query.username())
                .map(UserDTO::fromUser);
    }
}
