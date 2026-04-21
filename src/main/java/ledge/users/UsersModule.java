package ledge.users;

import ledge.boot.Module;
import ledge.boot.ModuleRegistry;
import ledge.security.application.services.IAuthorizationService;
import ledge.users.readmodel.application.handlers.GetAllUsersQueryHandler;
import ledge.users.readmodel.application.handlers.GetUserByIdQueryHandler;
import ledge.users.readmodel.application.handlers.GetUserQueryHandler;
import ledge.users.readmodel.infrastructure.IUserReadRepository;
import ledge.users.readmodel.infrastructure.UserReadRepository;
import ledge.users.readmodel.infrastructure.messaging.UserQueryBus;
import ledge.users.writemodel.application.handlers.AddUserCommandHandler;
import ledge.users.writemodel.application.handlers.ChangeUserPasswordCommandHandler;
import ledge.users.writemodel.application.handlers.ChangeUserRoleCommandHandler;
import ledge.users.writemodel.application.handlers.ChangeUsernameCommandHandler;
import ledge.users.writemodel.application.handlers.RemoveUserCommandHandler;
import ledge.users.writemodel.infrastructure.IUserWriteRepository;
import ledge.users.writemodel.infrastructure.UserWriteRepository;
import ledge.users.writemodel.infrastructure.messaging.UserCommandBus;

public class UsersModule implements Module {

        @Override
        public void register(ModuleRegistry registry) {

                registry.register(UserCommandBus.class, () -> {
                        UserCommandBus bus = new UserCommandBus(
                                        registry.resolve(IAuthorizationService.class));
                        bus.register(registry.resolve(AddUserCommandHandler.class));
                        bus.register(registry.resolve(RemoveUserCommandHandler.class));
                        bus.register(registry.resolve(ChangeUserPasswordCommandHandler.class));
                        bus.register(registry.resolve(ChangeUserRoleCommandHandler.class));
                        bus.register(registry.resolve(ChangeUsernameCommandHandler.class));
                        return bus;
                });

                registry.register(UserQueryBus.class, () -> {
                        UserQueryBus bus = new UserQueryBus(
                                        registry.resolve(IAuthorizationService.class));
                        bus.register(registry.resolve(GetAllUsersQueryHandler.class));
                        bus.register(registry.resolve(GetUserQueryHandler.class));
                        bus.register(registry.resolve(GetUserByIdQueryHandler.class));
                        return bus;
                });

                registry.register(IUserWriteRepository.class, () -> new UserWriteRepository());
                registry.register(IUserReadRepository.class, () -> new UserReadRepository());

                registry.register(AddUserCommandHandler.class, () -> new AddUserCommandHandler(
                                registry.resolve(IUserWriteRepository.class),
                                registry.resolve(IUserReadRepository.class)));

                registry.register(RemoveUserCommandHandler.class, () -> new RemoveUserCommandHandler(
                                registry.resolve(IUserWriteRepository.class),
                                registry.resolve(IUserReadRepository.class)));

                registry.register(ChangeUserPasswordCommandHandler.class, () -> new ChangeUserPasswordCommandHandler(
                                registry.resolve(IUserWriteRepository.class),
                                registry.resolve(IUserReadRepository.class)));

                registry.register(ChangeUserRoleCommandHandler.class, () -> new ChangeUserRoleCommandHandler(
                                registry.resolve(IUserWriteRepository.class),
                                registry.resolve(IUserReadRepository.class)));

                registry.register(ChangeUsernameCommandHandler.class, () -> new ChangeUsernameCommandHandler(
                                registry.resolve(IUserWriteRepository.class),
                                registry.resolve(IUserReadRepository.class)));

                registry.register(GetAllUsersQueryHandler.class, () -> new GetAllUsersQueryHandler(
                                registry.resolve(IUserReadRepository.class)));

                registry.register(GetUserQueryHandler.class, () -> new GetUserQueryHandler(
                                registry.resolve(IUserReadRepository.class)));

                registry.register(GetUserByIdQueryHandler.class, () -> new GetUserByIdQueryHandler(
                                registry.resolve(IUserReadRepository.class)));
        }

}
