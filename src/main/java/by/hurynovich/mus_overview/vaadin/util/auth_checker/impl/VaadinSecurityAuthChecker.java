package by.hurynovich.mus_overview.vaadin.util.auth_checker.impl;

import by.hurynovich.mus_overview.enumeration.UserRole;
import by.hurynovich.mus_overview.vaadin.util.auth_checker.AuthChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.vaadin.spring.security.VaadinSecurity;

@Component("authChecker")
public class VaadinSecurityAuthChecker implements AuthChecker {
    private final VaadinSecurity vaadinSecurity;

    @Autowired
    public VaadinSecurityAuthChecker(final VaadinSecurity vaadinSecurity) {
        this.vaadinSecurity = vaadinSecurity;
    }

    @Override
    public boolean checkAuth(final UserRole... grantedRoles) {
        final Authentication authentication = vaadinSecurity.getAuthentication();
        if (authentication == null) {
            return false;
        }
        for (UserRole userRole : grantedRoles) {
            if (authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).
                    anyMatch(authority -> authority.equals(userRole.name()))) {
                return true;
            }
        }
        return false;
    }
}
