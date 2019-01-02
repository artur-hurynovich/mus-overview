package by.hurynovich.mus_overview.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;


public class TokenAuthenticationService {

    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 10; // 10 days
    private final String secret = "ThisIsASecret";
    private final String tokenPrefix = "Bearer";
    private final String headerString = "Authorization";

    public void addAuthentication(final HttpServletResponse response, final String username)
    {
        final String jwt = Jwts.builder()
                    .setSubject(username)
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .signWith(SignatureAlgorithm.HS512, secret)
                    .compact();
        response.addHeader(headerString,tokenPrefix + " "+ jwt);
    }

    public Authentication getAuthentication(final HttpServletRequest request)
    {
        final String token = request.getHeader(headerString);
        if(token != null)
        {
            final String username = Jwts.parser()
                        .setSigningKey(secret)
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject();
            if (username != null) {
                return new AuthenticatedUser(username);
            }
        }
        return null;
    }
}
