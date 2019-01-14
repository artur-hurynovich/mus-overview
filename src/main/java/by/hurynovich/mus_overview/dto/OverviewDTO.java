package by.hurynovich.mus_overview.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OverviewDTO {
    private long id;
    private String title;
    private String text;
    private LocalDate date;
    private long subgroupId;
    private List<TagDTO> tags = new ArrayList<>();
}
