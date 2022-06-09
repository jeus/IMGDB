package nl.lunatech.movie.imgdb.core.controller;

import nl.lunatech.movie.imgdb.common.helper.ArgumentValidator;
import nl.lunatech.movie.imgdb.common.translator.MovieMapper;
import nl.lunatech.movie.imgdb.core.pojo.dto.MovieDto;
import nl.lunatech.movie.imgdb.core.service.MovieService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

/**
 * @author alikhandani
 * @created 23/05/2020
 * @project lobox
 */
@RestController
@RequestMapping("/api")
public class MovieController {


    final MovieService movieService;
    final MovieMapper movieMapper;
    final ArgumentValidator argumentValidator;

    public MovieController(MovieService movieService, MovieMapper movieMapper, ArgumentValidator personArgumentValidator) {
        this.movieService = movieService;
        this.movieMapper = movieMapper;
        this.argumentValidator = personArgumentValidator;
    }

    @Cacheable(value = "movie", key = "{#pid,#role}")
    @GetMapping(path = "/movie")
    public Collection<MovieDto> findAllMovie(@RequestParam(name = "pid") int pid, @RequestParam(name = "role") String role) {
        return movieMapper.toMovieDtos(movieService.findPersonMoviesByPid(pid, role));
    }

    @Cacheable(value = "/movie/share", key = "{#firstpid, #secondpid}")
    @GetMapping(path = "/movie/shares")
    public List<MovieDto> getShare(@RequestParam(name = "firstpid", required = false) int firstPid,
                                   @RequestParam(name = "secondpid") int secondPid) {
        argumentValidator.validateRequestForShare(firstPid, secondPid);
        return movieMapper.toMovieDtos(movieService.getShare(firstPid, secondPid));
    }

    @Cacheable(value = "directorwriters")
    @GetMapping(path = "/movie/directorwriters")
    public List<MovieDto> getDirectoWriter() {
        return movieMapper.toMovieDtos(movieService.findDirectorWriters());
    }


    @PostMapping(path = "/movie")
    public MovieDto AddNewMovieByTsv(@NotNull @RequestBody String tsvRow) {
        return movieMapper.toMovieDto(movieService.saveByTsv(tsvRow));
    }

    @Cacheable(value = "directorwriters")
    @GetMapping(path = "/movie/boxoffice/annual")
    public List<MovieDto> getTopBoxOffice(@RequestParam(name = "year", required = false) Integer year) {
        argumentValidator.validateYear(year);
        return movieMapper.toMovieDtos(movieService.findTopBoxOffice(year));
    }
}
