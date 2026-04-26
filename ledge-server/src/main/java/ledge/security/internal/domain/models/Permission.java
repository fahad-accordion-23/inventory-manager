package ledge.security.internal.domain.models;

import ledge.shared.security.models.Action;
import ledge.shared.security.models.Resource;

public record Permission(Resource resource, Action action) {
}
