package nl.lunatech.movie.imgdb.core.pojo.domain.relationship;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import nl.lunatech.movie.imgdb.core.pojo.domain.Movie;
import nl.lunatech.movie.imgdb.core.pojo.domain.Person;
import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.DateString;

import java.util.ArrayList;
import java.util.List;


/**
 * @author alikhandani
 * @created 28/05/2020
 * @project lunatech
 */
@EqualsAndHashCode(callSuper = true)
@Data
////@Relationship(type = "DIRECTED" , direction = Relationship.Direction.OUTGOING)
//@RelationshipProperties
public class Director extends BaseRelationship {

    private List<String> director = new ArrayList<>();

    @TargetNode
    @JsonIgnoreProperties({"actedIn", "directed", "wrote"})
    private Person person;


//    @JsonIgnoreProperties({"actors", "directors", "writers"})
//    private List<Movie> directed = new ArrayList<>();

    public String getPerson() {
        return person.getName();
    }




}
