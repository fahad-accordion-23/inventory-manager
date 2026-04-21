package ledge.security;

import ledge.security.application.services.IAuthenticationService;
import ledge.security.application.services.IAuthorizationService;
import ledge.security.domain.ISessionService;
import ledge.security.domain.SessionService;
import ledge.users.readmodel.infrastructure.IUserReadRepository;
import ledge.boot.Module;
import ledge.boot.ModuleRegistry;
import ledge.security.application.services.AuthenticationService;
import ledge.security.application.services.AuthorizationService;

public class SecurityModule implements Module {

    @Override
    public void register(ModuleRegistry registry) {
        registry.register(ISessionService.class, () -> new SessionService());

        registry.register(IAuthenticationService.class, () -> new AuthenticationService(
                registry.resolve(ISessionService.class),
                registry.resolve(IUserReadRepository.class)));

        registry.register(IAuthorizationService.class, () -> new AuthorizationService(
                registry.resolve(ISessionService.class)));
    }

}
