package by.hurynovich.mus_overview.service;

import by.hurynovich.mus_overview.converter.DTOEntityConverter;
import by.hurynovich.mus_overview.dto.impl.UserDTO;
import by.hurynovich.mus_overview.entity.impl.UserEntity;
import by.hurynovich.mus_overview.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("userService")
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

    public boolean emailExists(final String email) {
        final UserEntity userEntity = userRepository.findByEmail(email);
        return userEntity != null;
    }

    public UserDTO save(final UserDTO userDTO) {
        final String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        userDTO.setPassword(encodedPassword);
        return userConverter.convertToDTO(userRepository.save(userConverter.convertToEntity(userDTO)));
    }

    public List<UserDTO> findAll() {
        return userRepository.findAll().stream().map(userConverter::convertToDTO).collect(Collectors.toList());
    }

    public List<UserDTO> findByEmail(final String email) {
        return userRepository.findByEmailContaining(email).stream().
                map(userConverter::convertToDTO).collect(Collectors.toList());
    }

    public UserDTO update(final UserDTO userDTO) {
        final UserEntity userEntity = userRepository.findById(userDTO.getId()).orElse(null);
        if (userEntity != null) {
            BeanUtils.copyProperties(userDTO, userEntity, "id", "password");
            return userConverter.convertToDTO(userRepository.save(userEntity));
        } else {
            return null;
        }
    }

    public void delete(final UserDTO userDTO) {
        userRepository.delete(userConverter.convertToEntity(userDTO));
    }

    public long count() {
        return userRepository.count();
    }

    public long countByEmail(final String email) {
        return userRepository.countByEmailContaining(email);
    }

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        final UserDTO userDTO = userConverter.convertToDTO(userRepository.findByEmail(email));
        if (userDTO == null) {
            throw new UsernameNotFoundException("User with email \"" + email + "\" not found!");
        } else {
            return User.withUsername(email).
                    password(userDTO.getPassword()).
                    authorities(userDTO.getRole().toString()).
                    build();
        }
    }

}
