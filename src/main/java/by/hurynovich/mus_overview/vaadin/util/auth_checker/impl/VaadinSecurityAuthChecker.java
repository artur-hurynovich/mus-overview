package by.hurynovich.mus_overview.vaadin.util.auth_checker.impl;

import by.hurynovich.mus_overview.enumeration.UserRole;
import by.hurynovich.mus_overview.vaadin.util.auth_checker.IAuthChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.vaadin.spring.security.VaadinSecurity;

@Component("authChecker")
public class VaadinSecurityAuthChecker implements IAuthChecker {

    @Autowired
    private VaadinSecurity vaadinSecurity;

    @Override
    public boolean checkAuth(final UserRole... grantedRoles) {
        final Authentication authentication = vaadinSecurity.getAuthentication();
        if (authentication != null) {
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                for (UserRole role : grantedRoles) {
                    if (authority.getAuthority().equals(role.name())) {
                        return true;
                    }
                }
            }
            return false;
        } else {
            return false;
        }
    }

}
