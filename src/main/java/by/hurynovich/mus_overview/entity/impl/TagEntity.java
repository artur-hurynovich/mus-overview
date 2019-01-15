package by.hurynovich.mus_overview.entity.impl;

import by.hurynovich.mus_overview.entity.AbstractNamedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "tags")
public class TagEntity extends AbstractNamedEntity {

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "overviews_tags",
            joinColumns = @JoinColumn(name = "tag_id"),
            inverseJoinColumns = @JoinColumn(name = "overview_id"))
    private List<OverviewEntity> overviews;

}
