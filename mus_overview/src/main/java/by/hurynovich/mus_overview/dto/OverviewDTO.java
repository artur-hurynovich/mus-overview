package by.hurynovich.mus_overview.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
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
