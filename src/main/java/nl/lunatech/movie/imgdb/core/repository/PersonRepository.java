package nl.lunatech.movie.imgdb.core.repository;

import nl.lunatech.movie.imgdb.core.pojo.domain.Movie;
import nl.lunatech.movie.imgdb.core.pojo.domain.Person;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

/**
 * Query on Person table
 *
 * @author alikhandani
 * @created 27/05/2020
 * @project lunatech
 */
public interface PersonRepository extends Neo4jRepository<Person, Long> {


    @Query("MATCH (o:Person)-[]-(m:Movie) WHERE o.name = $0 return m")
    List<Movie> findMovies(String name);

    @Query("MATCH (o:Person)-[]-(m:Movie)-[]-(p:Person) WHERE o.uid = $0 and p.uid = $1 return m")
    List<Movie> getShareMovie(String firstUid, String seconUid);

    @Query("MATCH (n:Person { uid: $0 }),(m:Person { uid: $1 }), p = shortestPath((n)-[:ACTED_IN*]-(m)) WHERE length(p) > 1 RETURN length(p) - 2")
    Integer getShortPath(String firstUid, String secondUid);

    @Query("MATCH(n:Person{uid: $0})-[:ACTED_IN]->(g:Movie) WHERE g.genre IS NOT NULL RETURN g.genre")
    List<String> getGenreOfArtistByUid(String uid);

    @CachePut("person")
    @Query("MATCH (n:Person) WHERE n.name in $0 return n")
    List<Person> getPersonByName(String[] name);


}
