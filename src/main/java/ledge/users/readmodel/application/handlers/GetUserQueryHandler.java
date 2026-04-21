package ledge.users.readmodel.application.handlers;

import ledge.users.readmodel.contracts.GetUserByUsernameQuery;
import ledge.users.readmodel.dtos.UserDTO;
import ledge.users.readmodel.infrastructure.IUserReadRepository;
import ledge.shared.infrastructure.queries.QueryHandler;

import java.util.Optional;

public class GetUserQueryHandler {
    private final IUserReadRepository userReadRepository;

    public GetUserQueryHandler(IUserReadRepository userReadRepository) {
        this.userReadRepository = userReadRepository;
    }

    @QueryHandler
    public Optional<UserDTO> handle(GetUserByUsernameQuery query) {
        return userReadRepository.findByUsername(query.username());
    }
}
