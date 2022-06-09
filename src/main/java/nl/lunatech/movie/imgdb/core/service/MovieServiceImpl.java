package nl.lunatech.movie.imgdb.core.service;

import nl.lunatech.movie.imgdb.common.helper.ArgumentValidator;
import nl.lunatech.movie.imgdb.common.helper.TsvParser;
import nl.lunatech.movie.imgdb.common.translator.MovieMapper;
import nl.lunatech.movie.imgdb.core.dao.JpaMovieDao;
import nl.lunatech.movie.imgdb.core.dao.MovieRepository;
import nl.lunatech.movie.imgdb.core.pojo.domain.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    ArgumentValidator personArgumentValidator;
    @Autowired
    MovieMapper movieMapper;
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    JpaMovieDao jpaMovieDao;


    public List<Movie> getShare(int firstPid, int secondPid) {
        personArgumentValidator.validateRequestForShare(firstPid, secondPid);
        return movieRepository.getShareMovie(firstPid, secondPid);
    }

    public Movie saveByTsv(String tsv) {
        Movie movie = TsvParser.titleBasicToMovie(tsv);
        Movie newMovie = jpaMovieDao.saveAndFlush(movie);
        return newMovie;
    }

    public List<Movie> findPersonMoviesByPid(int pid, String role) {
        return jpaMovieDao.findPersonMovies(pid, role);
    }

    public List<Movie> findDirectorWriters() {
        return jpaMovieDao.directorIsWriter();
    }

    public List<Movie> findTopBoxOffice(Integer year) {
        int yearfilter = year;
        if (year == 0)
            yearfilter = Calendar.getInstance().get(Calendar.YEAR);
        return jpaMovieDao.findTopBoxOffice(yearfilter);
    }
}
