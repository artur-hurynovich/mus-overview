package by.hurynovich.mus_overview.vaadin.util.auth_checker;

import by.hurynovich.mus_overview.enumeration.UserRole;

public interface AuthChecker {
    boolean checkAuth(final UserRole... grantedRoles);
}
