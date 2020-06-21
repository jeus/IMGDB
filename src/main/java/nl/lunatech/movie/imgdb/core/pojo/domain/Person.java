package nl.lunatech.movie.imgdb.core.pojo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import nl.lunatech.movie.imgdb.core.pojo.domain.relationship.Role;
import org.neo4j.ogm.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author alikhandani
 * @created 28/05/2020
 * @project lunatech
 */
@Data
@NodeEntity
public class Person {
    @Id
    @GeneratedValue
    private Long id;
    private String uid;
    private String name;
    private List<String> knownForTitles;
    private List<String> primaryProfession;
    @Property("born")
    private int birthyear;

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
