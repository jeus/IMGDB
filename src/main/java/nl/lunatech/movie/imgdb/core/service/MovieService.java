package nl.lunatech.movie.imgdb.core.service;

import nl.lunatech.movie.imgdb.core.pojo.domain.Movie;

import java.util.Collection;
import java.util.List;

public interface MovieService {


    List<Movie> getShare(int firstPid, int secondPid);

    Movie saveByTsv(String tsv);

    List<Movie> findPersonMoviesByPid(int pid, String roles);

    List<Movie> findDirectorWriters();

    List<Movie> findTopBoxOffice(Integer year);
}
