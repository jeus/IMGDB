package nl.lunatech.movie.imgdb.core.pojo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import nl.lunatech.movie.imgdb.core.pojo.domain.relationship.Role;
import org.neo4j.ogm.annotation.*;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.util.ArrayList;
import java.util.List;
/**
 * @author alikhandani
 * @created 28/05/2020
 * @project lunatech
 */
@Data
@NodeEntity
public class Movie {
    @Id
    @GeneratedValue
    private Long id;
    private String uid;
    private String name;
    private String title;
    private String type;
    @Convert(graphPropertyType = String.class)
    @Property(name="startYear")
    private Integer release;
    private List<String> genres;
    private String description;

    @JsonIgnoreProperties("movie")
    @Relationship(type = "ACTED_IN", direction = Relationship.INCOMING)
    private List<Role> actors = new ArrayList<>();

    @JsonIgnoreProperties({"actedIn", "directed", "wrote"})
    @Relationship(type = "DIRECTED", direction = Relationship.INCOMING)
    private List<Person> directors = new ArrayList<>();

    @JsonIgnoreProperties({"actedIn", "directed", "wrote"})
    @Relationship(type = "WROTE", direction = Relationship.INCOMING)
    private List<Person> writers = new ArrayList<>();

}
