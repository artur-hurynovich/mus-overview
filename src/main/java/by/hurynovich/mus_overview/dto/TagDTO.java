package by.hurynovich.mus_overview.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO {
    private long id;
    private String name;
    private List<OverviewDTO> overviews = new ArrayList<>();
}
