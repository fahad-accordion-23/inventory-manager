package ledge.boot;

import ledge.shared.security.models.Action;
import ledge.shared.security.models.Resource;
import ledge.security.internal.domain.models.Permission;
import ledge.security.internal.domain.models.Role;
import ledge.security.internal.infrastructure.IRoleRepository;
import ledge.security.internal.infrastructure.IUserRoleRepository;
import ledge.users.readmodel.dtos.UserDTO;
import ledge.users.readmodel.infrastructure.IUserReadRepository;
import ledge.users.writemodel.domain.User;
import ledge.users.writemodel.infrastructure.IUserWriteRepository;
import ledge.util.PasswordHasher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Bootstrapper to seed initial system data (roles, admin user, and assignments).
 * Updated with correct security model packages.
 */
@Component
public class DatabaseBootstrapper implements CommandLineRunner {
    private final IRoleRepository roleRepository;
    private final IUserWriteRepository userWriteRepository;
    private final IUserReadRepository userReadRepository;
    private final IUserRoleRepository userRoleRepository;

    public DatabaseBootstrapper(IRoleRepository roleRepository,
                                 IUserWriteRepository userWriteRepository,
                                 IUserReadRepository userReadRepository,
                                 IUserRoleRepository userRoleRepository) {
        this.roleRepository = roleRepository;
        this.userWriteRepository = userWriteRepository;
        this.userReadRepository = userReadRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public void run(String... args) {
        seedRoles();
        seedAdminUser();
    }

    private void seedRoles() {
        if (!roleRepository.findAll().isEmpty()) return;

        // ADMIN: Full access to everything
        Set<Permission> adminPerms = new HashSet<>();
        for (Resource r : Resource.values()) {
            for (Action a : Action.values()) {
                adminPerms.add(new Permission(r, a));
            }
        }
        roleRepository.save(Role.register("ADMIN", adminPerms));

        // INVENTORY_MANAGER: Full access to products
        roleRepository.save(Role.register("INVENTORY_MANAGER", Set.of(
                new Permission(Resource.PRODUCT, Action.CREATE),
                new Permission(Resource.PRODUCT, Action.READ),
                new Permission(Resource.PRODUCT, Action.UPDATE),
                new Permission(Resource.PRODUCT, Action.DELETE))));

        // SALES_STAFF: Read products, create invoices
        roleRepository.save(Role.register("SALES_STAFF", Set.of(
                new Permission(Resource.PRODUCT, Action.READ),
                new Permission(Resource.INVOICE, Action.CREATE))));

        // DEFAULT_USER: View only
        roleRepository.save(Role.register("DEFAULT_USER", Set.of(
                new Permission(Resource.PRODUCT, Action.READ))));
    }

    private void seedAdminUser() {
        User admin;
        if (userWriteRepository.findByUsername("admin").isEmpty()) {
            admin = User.register("admin", PasswordHasher.hash("admin123"));
            userWriteRepository.save(admin);
            // Sync to read model
            userReadRepository.save(new UserDTO(admin.getId(), admin.getUsername(), admin.getPasswordHash()));
        } else {
            admin = userWriteRepository.findByUsername("admin").get();
        }

        // Always ensure admin has the ADMIN role assigned even if they already existed
        if (userRoleRepository.findRoleByUserId(admin.getId()).isEmpty()) {
            roleRepository.findByName("ADMIN").ifPresent(role -> {
                userRoleRepository.saveRole(admin.getId(), role.getId());
            });
        }
    }
}
