package ledge.users.readmodel.application.handlers;

import ledge.users.readmodel.contracts.GetAllUsersQuery;
import ledge.users.readmodel.dtos.UserDTO;
import ledge.users.readmodel.infrastructure.IUserReadRepository;
import ledge.shared.infrastructure.queries.QueryHandler;

import java.util.List;

public class GetAllUsersQueryHandler {
    private final IUserReadRepository userReadRepository;

    public GetAllUsersQueryHandler(IUserReadRepository userReadRepository) {
        this.userReadRepository = userReadRepository;
    }

    @QueryHandler
    public List<UserDTO> handle(GetAllUsersQuery query) {
        return userReadRepository.findAll();
    }
}
