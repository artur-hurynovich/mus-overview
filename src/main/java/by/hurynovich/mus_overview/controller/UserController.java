package by.hurynovich.mus_overview.controller;

import by.hurynovich.mus_overview.dto.impl.UserDTO;
import by.hurynovich.mus_overview.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/user")
public class UserController {

    private final UserDetailsServiceImpl userService;

    @Autowired
    public UserController(final UserDetailsServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public UserDTO create(@RequestBody final UserDTO userDTO) {
        if (userService.emailExists(userDTO.getEmail())) {
            return null;
        } else {
            return userService.save(userDTO);
        }
    }

    @GetMapping("/all")
    public List<UserDTO> getAll() {
        return userService.findAll();
    }

}
