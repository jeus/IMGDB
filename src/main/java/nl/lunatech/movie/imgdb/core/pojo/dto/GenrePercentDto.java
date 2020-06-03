package nl.lunatech.movie.imgdb.core.pojo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author alikhandani
 * @created 02/06/2020
 * @project lunatech
 */
@Data
@AllArgsConstructor
public class GenrePercentDto {

    private String name;
    private int count;
    private float percent;

    @JsonIgnore
    private final int sum;

}
