package nl.lunatech.movie.imgdb.core.pojo.domain.relationship;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import nl.lunatech.movie.imgdb.core.pojo.domain.Movie;
import nl.lunatech.movie.imgdb.core.pojo.domain.Person;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

import java.util.ArrayList;
import java.util.List;


/**
 * @author alikhandani
 * @created 28/05/2020
 * @project lunatech
 */
@Data
@RelationshipEntity(type = "DIRECTED")
public class Director extends BaseRelationship {

    private List<String> director = new ArrayList<>();

    @StartNode
    @JsonIgnoreProperties({"actedIn", "directed", "wrote"})
    private Person person;

    @EndNode
    @JsonIgnoreProperties({"actors", "directors", "writers"})
    private List<Movie> directed = new ArrayList<>();

    public String getPerson() {
        return person.getName();
    }




}
