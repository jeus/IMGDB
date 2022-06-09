package nl.lunatech.movie.imgdb.core.pojo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import nl.lunatech.movie.imgdb.core.pojo.domain.relationship.Role;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.DateString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author alikhandani
 * @created 28/05/2020
 * @project lunatech
 */
@Data
@Node("Person")
public class Person {
    @Id
    private Integer pid;
    private String name;
    private List<String> knownForTitles;
    private List<String> primaryProfession;
    @Property("born")
    private Integer birthyear;
    @Property("death")
    private Integer deathyear;

    @JsonIgnoreProperties("person")
    @Relationship(type = "ACTED_IN")
    private List<Role> actedIn = new ArrayList<>();

    @JsonIgnoreProperties({"actors", "directors", "writers"})
    @Relationship(type = "DIRECTED")
    private List<Movie> directed = new ArrayList<>();

    @JsonIgnoreProperties({"actors", "directors", "writers"})
    @Relationship(type = "WROTE")
    private List<Movie> wrote = new ArrayList<>();

}
