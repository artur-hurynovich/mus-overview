package by.hurynovich.mus_overview.vaadin.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@ApplicationScope
public class RedirectAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(final HttpServletRequest httpServletRequest,
                                        final HttpServletResponse httpServletResponse,
                                        final Authentication authentication)
            throws IOException {
        httpServletResponse.sendRedirect("/mus_overview/vaadin");
    }
}
