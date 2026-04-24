package ledge.security.internal.domain.models;

import ledge.security.api.models.Action;
import ledge.security.api.models.Resource;

public record Permission(Resource resource, Action action) {
}
