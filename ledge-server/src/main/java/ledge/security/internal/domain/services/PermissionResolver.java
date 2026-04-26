package ledge.security.internal.domain.services;

import ledge.security.api.IPermissionResolver;
import ledge.security.api.annotations.RequiresPermission;
import ledge.security.api.dto.PermissionDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PermissionResolver implements IPermissionResolver {

    @Override
    public Optional<PermissionDTO> resolve(Object context) {
        if (context == null) {
            return Optional.empty();
        }

        RequiresPermission annotation = context.getClass().getAnnotation(RequiresPermission.class);
        if (annotation == null) {
            return Optional.empty();
        }

        return Optional.of(new PermissionDTO(annotation.resource(), annotation.action()));
    }
}
