package ledge.api.security.dto;

import java.util.Map;
import java.util.UUID;

import ledge.shared.security.models.Action;
import ledge.shared.security.models.Resource;

public record RoleResponseDTO(UUID id, String name, Map<Resource, Action> permissions) {

}
