package nl.lunatech.movie.imgdb.core.pojo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author alikhandani
 * @created 03/06/2020
 * @project lunatech
 */
@Data
@AllArgsConstructor
public class GenrePercent implements Comparable<GenrePercent> {

    private String name;
    private int count;
    private float percent;
    private  int sum;

    @Override
    public int compareTo(GenrePercent o) {
        return Integer.compare(o.getCount(), this.count);
    }
}
