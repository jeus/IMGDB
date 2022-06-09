package nl.lunatech.movie.imgdb.core.dao;

import nl.lunatech.movie.imgdb.core.pojo.domain.Movie;

import java.util.Collection;
import java.util.List;


public interface MovieDao {

    Movie saveAndFlush(Movie movie);

    List<Movie> saveAllAndFlush(List<Movie> movie);

    Collection<Movie> findPersonMovies(int pid, String role);

    Collection<Movie> directorIsWriter();

    List<Movie> findTopBoxOffice(int yearfilter);
}
