package by.hurynovich.mus_overview.vaadin.security;

import com.vaadin.spring.access.SecuredViewAccessControl;
import com.vaadin.spring.access.ViewAccessControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class VaadinSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("userService")
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(getPasswordEncoder());
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests().
                antMatchers("/mus_overview/VAADIN/**", "/mus_overview/**").
                permitAll();
        http.csrf().disable();
    }

    @Bean("passwordEncoder")
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(7);
    }

    @Bean
    public ViewAccessControl getViewAccessControl() {
        return new SecuredViewAccessControl();
    }

}
