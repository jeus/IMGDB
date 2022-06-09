package nl.lunatech.movie.imgdb.core.pojo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import nl.lunatech.movie.imgdb.core.pojo.domain.relationship.Role;
import nl.lunatech.movie.imgdb.core.pojo.model.Crew;
import nl.lunatech.movie.imgdb.core.pojo.model.Rating;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

/**
 * @author alikhandani
 * @created 28/05/2020
 * @project lunatech
 */
@Data
@Node()
public class Movie {
    @Id
    private Integer mid;
    private String name;
    private String title;
    private String type;
    //    @DateString
    @Property(name = "startYear")
    private Integer release;
    private List<String> genres;
    private String description;
    private Float averageRating;
    private int numVotes;
    private String directorPid;
    private String writerPid;
    private Boolean directorWriters;


    public Movie setDetails(Rating rating, Crew crew) {
        if (rating != null) {
            averageRating = rating.getAverageRating();
            numVotes = rating.getNumVotes();
        }
        if (crew != null) {
            directorPid = crew.getDirectorPids();
            writerPid = crew.getWriterPids();
            directorWriters = !crew.getWriterDirector().isEmpty();
        }
        return this;
    }


    @JsonIgnoreProperties("movie")
    @Relationship(type = "ACTED_IN", direction = Relationship.Direction.INCOMING)
    private List<Role> actors = new ArrayList<>();

    @JsonIgnoreProperties({"actedIn", "directed", "wrote"})
    @Relationship(type = "DIRECTED", direction = Relationship.Direction.INCOMING)
    private List<Person> directors = new ArrayList<>();

    @JsonIgnoreProperties({"actedIn", "directed", "wrote"})
    @Relationship(type = "WROTE", direction = Relationship.Direction.INCOMING)
    private List<Person> writers = new ArrayList<>();

}
