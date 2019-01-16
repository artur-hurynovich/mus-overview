package by.hurynovich.mus_overview.service.impl;

import by.hurynovich.mus_overview.converter.impl.UserConverter;
import by.hurynovich.mus_overview.dto.impl.UserDTO;
import by.hurynovich.mus_overview.entity.impl.UserEntity;
import by.hurynovich.mus_overview.repository.UserRepository;
import by.hurynovich.mus_overview.service.IUserDTOService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("userService")
public class UserService implements IUserDTOService {
    private final UserRepository userRepository;
    private final UserConverter userConverter;

    @Autowired
    public UserService(final UserRepository userRepository,
                       final UserConverter userConverter) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
    }

    @Override
    public UserDTO save(final UserDTO userDTO) {
        return userConverter.convertToDTO(userRepository.save(userConverter.convertToEntity(userDTO)));
    }

    @Override
    public UserDTO findOne(final long id) {
        return userConverter.convertToDTO(userRepository.getOne(id));
    }

    @Override
    public UserDTO findByEmailAndPassword(final UserDTO userDTO) {
        return userConverter.convertToDTO(userRepository.
                findByEmailAndPassword(userDTO.getEmail(), userDTO.getPassword()));
    }

    @Override
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream().map(userConverter::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public UserDTO update(final UserDTO userDTO) {
        final UserEntity userEntity = userRepository.getOne(userDTO.getId());
        BeanUtils.copyProperties(userDTO, userEntity);
        return userConverter.convertToDTO(userRepository.save(userEntity));
    }

    @Override
    public void delete(final UserDTO userDTO) {
        userRepository.delete(userConverter.convertToEntity(userDTO));
    }

    @Override
    public long count() {
        return userRepository.count();
    }

    @Override
    public boolean emailExists(final String email) {
        final UserEntity userEntity = userRepository.findByEmail(email);
        return userEntity != null;
    }

}
