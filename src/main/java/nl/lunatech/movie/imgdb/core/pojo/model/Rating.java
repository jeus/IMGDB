package nl.lunatech.movie.imgdb.core.pojo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author alikhandani
 * @created 08/06/2022
 * @project imgdb
 */

@Data
@AllArgsConstructor
public class Rating {

    private Integer mid;
    private Float averageRating;
    private Integer numVotes;

    public Rating getThis() {
        return this;
    }

}
