package nl.lunatech.movie.imgdb.core.pojo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author alikhandani
 * @created 02/06/2020
 * @project lunatech
 */
@Data
@AllArgsConstructor
public  class MovieDto {

    @JsonIgnore
    private Integer mid;
    private String name;
    private String title;
    private String type;
    private List<String> genres;
    private Integer release;
    private String description;
    private Float averageRating;
    private int numVotes;
    private String directorPid;
    private String writerPid;
    private Boolean directorWriters;

}
