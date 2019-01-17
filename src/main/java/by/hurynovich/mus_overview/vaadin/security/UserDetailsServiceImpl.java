package by.hurynovich.mus_overview.vaadin.security;

import by.hurynovich.mus_overview.converter.DTOEntityConverter;
import by.hurynovich.mus_overview.dto.impl.UserDTO;
import by.hurynovich.mus_overview.entity.impl.UserEntity;
import by.hurynovich.mus_overview.repository.UserRepository;
import com.vaadin.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringComponent("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    @Qualifier("userRepository")
    private UserRepository userRepository;

    @Autowired
    @Qualifier("userConverter")
    private DTOEntityConverter<UserDTO, UserEntity> userConverter;

    @Autowired
    @Qualifier("passwordEncoder")
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        final UserDTO userDTO = userConverter.convertToDTO(userRepository.findByEmail(email));
        if (userDTO == null) {
            throw new UsernameNotFoundException("User with email \"" + email + "\" not found!");
        } else {
            return User.withUsername(email).
                    passwordEncoder(passwordEncoder::encode).
                    password(userDTO.getPassword()).
                    authorities(userDTO.getRole().toString()).
                    build();
        }
    }

}
