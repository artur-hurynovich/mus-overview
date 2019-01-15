package by.hurynovich.mus_overview.entity.impl;

import by.hurynovich.mus_overview.entity.AbstractNamedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "groups")
public class GroupEntity extends AbstractNamedEntity {

}
