package ledge.users.readmodel.application.handlers;

import ledge.users.readmodel.contracts.GetUserByIdQuery;
import ledge.users.readmodel.dtos.UserDTO;
import ledge.users.readmodel.infrastructure.IUserReadRepository;
import ledge.shared.infrastructure.queries.QueryHandler;

import java.util.Optional;

public class GetUserByIdQueryHandler {
    private final IUserReadRepository userReadRepository;

    public GetUserByIdQueryHandler(IUserReadRepository userReadRepository) {
        this.userReadRepository = userReadRepository;
    }

    @QueryHandler
    public Optional<UserDTO> handle(GetUserByIdQuery query) {
        return userReadRepository.findById(query.id());
    }
}
