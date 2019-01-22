package by.hurynovich.mus_overview.entity.impl;

import by.hurynovich.mus_overview.entity.AbstractNamedEntity;
import by.hurynovich.mus_overview.enumeration.UserRole;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity extends AbstractNamedEntity {
    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private UserRole role;
}