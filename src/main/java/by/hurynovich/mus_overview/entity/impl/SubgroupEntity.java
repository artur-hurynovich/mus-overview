package by.hurynovich.mus_overview.entity.impl;

import by.hurynovich.mus_overview.entity.AbstractNamedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "subgroups")
public class SubgroupEntity extends AbstractNamedEntity {
    @NotNull
    private long groupId;
}
