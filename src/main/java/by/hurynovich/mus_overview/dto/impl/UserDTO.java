package by.hurynovich.mus_overview.dto.impl;

import by.hurynovich.mus_overview.dto.AbstractNamedDTO;
import by.hurynovich.mus_overview.enumeration.UserRole;
import by.hurynovich.mus_overview.vaadin.annotation.Bind;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UserDTO extends AbstractNamedDTO {

    @Bind(fieldClass = "com.vaadin.ui.TextField")
    private String email;

    @Bind(fieldClass = "com.vaadin.ui.PasswordField")
    private String password;

    @Bind(fieldClass = "by.hurynovich.mus_overview.vaadin.custom_field.UserRoleField")
    private UserRole role;

}