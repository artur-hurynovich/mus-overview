package by.hurynovich.mus_overview.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter{

    private final TokenAuthenticationService tokenAuthenticationService;

    public JWTLoginFilter(final String url, final AuthenticationManager authenticationManager)
    {
        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authenticationManager);
        tokenAuthenticationService = new TokenAuthenticationService();
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest httpServletRequest,
                                                final HttpServletResponse httpServletResponse)
            throws AuthenticationException, IOException {
        final AccountCredentials credentials = new ObjectMapper()
                .readValue(httpServletRequest.getInputStream(), AccountCredentials.class);
        final UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword());
        return getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(final HttpServletRequest request, final HttpServletResponse response,
                                            final FilterChain chain, final Authentication authentication) {
        final String name = authentication.getName();
        tokenAuthenticationService.addAuthentication(response, name);
    }
}
