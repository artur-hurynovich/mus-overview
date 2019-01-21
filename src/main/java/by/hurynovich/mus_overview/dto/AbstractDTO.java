package by.hurynovich.mus_overview.dto;

import lombok.Data;

@Data
public class AbstractDTO {

    private long id;

    @Override
    public boolean equals(final Object o) {
        if (o == null) return false;
        if (this == o) return true;
        if (getClass() != o.getClass()) return false;
        final AbstractDTO temp = (AbstractDTO) o;
        return id == temp.id;
    }

}
