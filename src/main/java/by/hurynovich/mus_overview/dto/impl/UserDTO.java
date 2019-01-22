package by.hurynovich.mus_overview.dto.impl;

import by.hurynovich.mus_overview.dto.AbstractNamedDTO;
import by.hurynovich.mus_overview.enumeration.UserRole;
import by.hurynovich.mus_overview.vaadin.annotation.GridColumn;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UserDTO extends AbstractNamedDTO {
    @GridColumn(caption = "E-mail", position = 20)
    private String email;

    private String password;

    @GridColumn(caption = "Role", position = 30)
    private UserRole role;
}