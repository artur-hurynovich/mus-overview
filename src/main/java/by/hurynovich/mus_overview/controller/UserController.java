package by.hurynovich.mus_overview.controller;

import by.hurynovich.mus_overview.dto.UserDTO;
import by.hurynovich.mus_overview.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity createUser (@RequestBody final UserDTO userDTO) {
        if (userService.isUniqueEmail(userDTO.getEmail())) {
            userService.createUser(userDTO);
            return new ResponseEntity<>("User created", HttpStatus.CREATED);
        }

        return new ResponseEntity<>("Email already exist", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/list")
    public List<UserDTO> getAllUsers() {
        return userService.getUsersList();
    }

}
