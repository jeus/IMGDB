package nl.lunatech.movie.imgdb.core.dao;

import nl.lunatech.movie.imgdb.core.pojo.domain.Movie;
import org.neo4j.cypherdsl.core.Cypher;
import org.neo4j.cypherdsl.core.Node;
import org.neo4j.cypherdsl.core.Relationship;
import org.neo4j.cypherdsl.core.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class JpaMovieDao implements MovieDao {
    @Autowired
    MovieRepository movieRepository;

    @Override
    @Transactional
    public Movie saveAndFlush(Movie movie) {
        Movie movie1 = movieRepository.save(movie);
        return movie1;
    }


    @Override
    @Transactional
    public List<Movie> saveAllAndFlush(List<Movie> movies) {
        List<Movie> movie1 = movieRepository.saveAll(movies);
        return movie1;
    }


    //    List<Movie> findPersonMovies(String pid);
    @Override
    public List<Movie> findPersonMovies(int pid, String role) {

        return new ArrayList<>(movieRepository.findAll(getPersonMoviesStatmentByPid(pid, role)));
    }

    //    @Query("MATCH (p:Person)-[]-(m:Movie) WHERE p.name = $0 return m")

    @Override
    @Transactional
    public List<Movie> directorIsWriter() {
       return movieRepository.SamePersonDirectorAndWriter();
    }

    @Override
    @Transactional
    public List<Movie> findTopBoxOffice(int year){
        return movieRepository.findTopBoxOffice(year);
    }



    private Statement getPersonMoviesStatmentByPid(int pid, String roles) {
        Node p = Cypher.node("Person").named("p");
        Node m = Cypher.node("Movie").named("m");
        Relationship r = p.relationshipBetween(m, roles);
        return Cypher.match(p, r, m)
                .where(p.property("pid").isEqualTo(Cypher.anonParameter(pid)))
                .returning(
                        m.getRequiredSymbolicName()
                ).build();
    }
}
