package by.hurynovich.mus_overview.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO {
    private long id;
    private String name;
    private List<OverviewDTO> overviews = new ArrayList<>();
}
