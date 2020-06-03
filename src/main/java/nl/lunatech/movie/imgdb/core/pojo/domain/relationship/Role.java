package nl.lunatech.movie.imgdb.core.pojo.domain.relationship;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import nl.lunatech.movie.imgdb.core.pojo.domain.Movie;
import nl.lunatech.movie.imgdb.core.pojo.domain.Person;
import org.neo4j.ogm.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author alikhandani
 * @created 28/05/2020
 * @project lunatech
 */

@Data
@RelationshipEntity(type = "ACTED_IN")
public class Role extends BaseRelationship {

    private List<String> roles = new ArrayList<>();

    @StartNode
    @JsonIgnoreProperties({"actedIn", "directed", "wrote"})
    private Person person;

    @EndNode
    @JsonIgnoreProperties({"actors", "directors", "writers"})
    private Movie movie;

    public String getMovie() {
        return movie.getTitle();
    }
}