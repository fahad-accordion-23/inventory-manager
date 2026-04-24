package ledge.users.readmodel.application.handlers;

import org.springframework.stereotype.Service;

import ledge.users.readmodel.contracts.GetAllUsersQuery;
import ledge.users.readmodel.dtos.UserDTO;
import ledge.users.readmodel.infrastructure.IUserReadRepository;
import ledge.shared.infrastructure.queries.QueryHandler;

import java.util.List;

@Service
public class GetAllUsersQueryHandler implements QueryHandler<GetAllUsersQuery, List<UserDTO>> {
    private final IUserReadRepository userReadRepository;

    public GetAllUsersQueryHandler(IUserReadRepository userReadRepository) {
        this.userReadRepository = userReadRepository;
    }

    @Override
    public List<UserDTO> handle(GetAllUsersQuery query) {
        return userReadRepository.findAll();
    }
}
