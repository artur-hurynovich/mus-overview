package by.hurynovich.mus_overview.dto;

import by.hurynovich.mus_overview.role.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String password;
    private UserRole role;
}