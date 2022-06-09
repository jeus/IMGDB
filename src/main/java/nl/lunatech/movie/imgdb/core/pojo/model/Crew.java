package nl.lunatech.movie.imgdb.core.pojo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * @author alikhandani
 * @created 08/06/2022
 * @project imgdb
 */
@Data
@AllArgsConstructor
public class Crew {
    private Integer mid;
    private String directorPids = "";
    private String writerPids = "";
    private Set<Integer> writerDirector = new HashSet<>();


    public Crew getThis() {
        return this;
    }


}
