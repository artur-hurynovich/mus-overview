package by.hurynovich.mus_overview.service.impl;

import by.hurynovich.mus_overview.converter.impl.UserConverter;
import by.hurynovich.mus_overview.dto.impl.UserDTO;
import by.hurynovich.mus_overview.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserConverter userConverter;

    @Autowired
    public UserService(final UserRepository userRepository,
                       final UserConverter userConverter) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
    }

    public UserDTO signUp(final UserDTO userDTO) {
        return userConverter.convertToDTO(userRepository.save(userConverter.convertToEntity(userDTO)));
    }

    public UserDTO signIn(final UserDTO userDTO) {
        return userConverter.convertToDTO(userRepository.
                findByEmailAndPassword(userDTO.getEmail(), userDTO.getPassword()));
    }

    public boolean isUniqueEmail (final String email) {
        List<UserDTO> usersList = userRepository.findAll().stream().
                map(userConverter::convertToDTO).collect(Collectors.toList());
        for (UserDTO user : usersList) {
            if (user.getEmail().equals(email)) {
                return false;
            }
        }
        return true;
    }

    public List<UserDTO> getUsersList() {
        return userRepository.findAll().stream().map(userConverter::convertToDTO).collect(Collectors.toList());
    }
}
