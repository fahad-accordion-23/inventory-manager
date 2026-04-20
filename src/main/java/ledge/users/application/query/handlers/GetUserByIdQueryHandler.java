package ledge.users.application.query.handlers;

import ledge.users.application.dtos.UserDTO;
import ledge.users.application.query.GetUserByIdQuery;
import ledge.users.application.services.UserService;
import ledge.util.cqrs.QueryHandler;

import java.util.Optional;

public class GetUserByIdQueryHandler {
    private final UserService userService;

    public GetUserByIdQueryHandler(UserService userService) {
        this.userService = userService;
    }

    @QueryHandler
    public Optional<UserDTO> handle(GetUserByIdQuery query) {
        return userService.getUserById(query.id())
                .map(UserDTO::fromUser);
    }
}
