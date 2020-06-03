package nl.lunatech.movie.imgdb.core.pojo.domain.relationship;

import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;

/**
 * @author alikhandani
 * @created 28/05/2020
 * @project lunatech
 */
@Data
public abstract class BaseRelationship {
    @Id
    @GeneratedValue
    private Long relationshipId;
}