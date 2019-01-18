package by.hurynovich.mus_overview.vaadin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.vaadin.spring.security.annotation.EnableVaadinManagedSecurity;
import org.vaadin.spring.security.config.AuthenticationManagerConfigurer;

@Configuration
@EnableVaadinManagedSecurity
public class VaadinSecurityConfig implements AuthenticationManagerConfigurer {

    @Autowired
    @Qualifier("userService")
    private UserDetailsService userDetailsService;

    @Override
    public void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(getPasswordEncoder());
    }

    @Bean("passwordEncoder")
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
