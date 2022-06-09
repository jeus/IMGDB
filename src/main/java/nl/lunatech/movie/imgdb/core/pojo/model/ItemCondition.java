package nl.lunatech.movie.imgdb.core.pojo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemCondition {
    private String name;
    private String title;
    private String description;
    private int percent;
    private long rowCount;
    private ImportStatus status;
    private int order;
}
