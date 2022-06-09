package nl.lunatech.movie.imgdb.core.dao;

import nl.lunatech.movie.imgdb.core.pojo.domain.Person;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.neo4j.repository.support.CypherdslStatementExecutor;

import java.util.List;

/**
 * Query on Person table
 *
 * @author alikhandani
 * @created 27/05/2020
 * @project lunatech
 */
public interface PersonRepository extends Neo4jRepository<Person, Integer>, CypherdslStatementExecutor<Person> {

    @Query("MATCH (p1:Person { pid: $0 }),(p2:Person { pid: $1 }), result = shortestPath((p1)-[:actor|actress|director|writer*]-(p2)) WHERE length(result) > 1 RETURN length(result) - 2")
    Integer getShortPath(int firstPid, int secondPid);
//    uid: 243918
//    uid: 97421

    @CachePut("person")
    @Query("MATCH (n:Person) WHERE n.name in $0 return n")
    List<Person> getPersonByName(String[] name);

}
