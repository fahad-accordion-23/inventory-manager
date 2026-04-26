package ledge.security.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import ledge.shared.security.models.Action;
import ledge.shared.security.models.Resource;

/**
 * Annotation to declare the required permission for a command or query.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RequiresPermission {
    Resource resource();
    Action action();
}
