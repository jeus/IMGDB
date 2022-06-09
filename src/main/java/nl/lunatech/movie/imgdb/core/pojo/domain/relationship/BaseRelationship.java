package nl.lunatech.movie.imgdb.core.pojo.domain.relationship;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.RelationshipId;

/**
 * @author alikhandani
 * @created 28/05/2020
 * @project lunatech
 */
@Data
public abstract class BaseRelationship {
    @RelationshipId
    private Long relationshipId;
}