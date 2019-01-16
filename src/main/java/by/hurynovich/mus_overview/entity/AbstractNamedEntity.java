package by.hurynovich.mus_overview.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
public class AbstractNamedEntity extends AbstractEntity {

    @NotNull
    private String name;

}
