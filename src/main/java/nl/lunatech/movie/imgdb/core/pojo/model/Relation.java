package nl.lunatech.movie.imgdb.core.pojo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author alikhandani
 * @created 06/06/2022
 * @project imgdb
 */
@Data
@NoArgsConstructor
public class Relation {

    private Integer mid;
    private Integer pid;
    private String title;

}
