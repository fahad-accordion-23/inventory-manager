package ledge.users.readmodel.application.handlers;

import ledge.users.readmodel.contracts.GetUserByUsernameQuery;
import ledge.users.readmodel.dtos.UserDTO;
import ledge.users.readmodel.infrastructure.IUserReadRepository;
import ledge.shared.infrastructure.queries.QueryHandler;

import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class GetUserQueryHandler implements QueryHandler<GetUserByUsernameQuery, Optional<UserDTO>> {
    private final IUserReadRepository userReadRepository;

    public GetUserQueryHandler(IUserReadRepository userReadRepository) {
        this.userReadRepository = userReadRepository;
    }

    @Override
    public Optional<UserDTO> handle(GetUserByUsernameQuery query) {
        return userReadRepository.findByUsername(query.username());
    }
}
