package nl.lunatech.movie.imgdb.core.dao;

import nl.lunatech.movie.imgdb.core.pojo.domain.Movie;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.neo4j.repository.support.CypherdslStatementExecutor;

import java.util.List;

public interface MovieRepository extends Neo4jRepository<Movie, Integer>, CypherdslStatementExecutor<Movie> {



    @Query("MATCH (o:Person)-[]-(m:Movie)-[]-(p:Person) WHERE o.pid = $0 and p.pid = $1 return m")
    List<Movie> getShareMovie(int firstPid, int secondPid);


    @Query("MATCH (p:Person{pid: $0})-[:actor|actress]->(m:Movie) WHERE m.genres IS NOT NULL RETURN m")
    List<Movie> getGenreOfArtistByPid(int pid);

    @Query("MATCH (m:Movie) where m.directorWriters = true RETURN m")
    List<Movie> SamePersonDirectorAndWriter();

    @Query("MATCH (n:Movie) WITH n order by n.numVotes DESC where n.startYear = $0 RETURN n LIMIT 3")
    List<Movie> findTopBoxOffice(int year);
}
