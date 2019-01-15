package by.hurynovich.mus_overview.entity.impl;

import by.hurynovich.mus_overview.entity.AbstractNamedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "overviews")
public class OverviewEntity extends AbstractNamedEntity {

    @NotNull
    private String text;

    @NotNull
    @Column(name = "o_date")
    private LocalDate date;

    @NotNull
    private long subgroupId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "overviews_tags",
            joinColumns = @JoinColumn(name = "overview_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<TagEntity> tags;

}
