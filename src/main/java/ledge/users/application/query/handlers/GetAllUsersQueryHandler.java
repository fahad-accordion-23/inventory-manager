package ledge.users.application.query.handlers;

import ledge.users.application.dtos.UserDTO;
import ledge.users.application.query.GetAllUsersQuery;
import ledge.users.application.services.UserService;
import ledge.util.cqrs.QueryHandler;

import java.util.List;
import java.util.stream.Collectors;

public class GetAllUsersQueryHandler {
    private final UserService userService;

    public GetAllUsersQueryHandler(UserService userService) {
        this.userService = userService;
    }

    @QueryHandler
    public List<UserDTO> handle(GetAllUsersQuery query) {
        return userService.getAllUsers().stream()
                .map(UserDTO::fromUser)
                .collect(Collectors.toList());
    }
}
