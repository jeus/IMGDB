package nl.lunatech.movie.imgdb.core.rest;

import nl.lunatech.movie.imgdb.common.helper.PersonArgumentValidator;
import nl.lunatech.movie.imgdb.common.helper.Statistic;
import nl.lunatech.movie.imgdb.common.translator.MovieMapper;
import nl.lunatech.movie.imgdb.common.translator.PersonMapper;
import nl.lunatech.movie.imgdb.core.pojo.dto.GenrePercentDto;
import nl.lunatech.movie.imgdb.core.pojo.dto.MovieDto;
import nl.lunatech.movie.imgdb.core.pojo.dto.PersonDto;
import nl.lunatech.movie.imgdb.core.pojo.dto.TypeCastDto;
import nl.lunatech.movie.imgdb.core.pojo.model.GenrePercent;
import nl.lunatech.movie.imgdb.core.pojo.model.TypeCastModel;
import nl.lunatech.movie.imgdb.core.repository.PersonRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author alikhandani
 * @created 28/05/2020
 * @project lunatech
 */
@RestController
@RequestMapping("/person")
public class PersonQueryController {

    final PersonRepository personRepository;
    final PersonArgumentValidator personArgumentValidator;
    final Statistic statistic;
    final MovieMapper movieMapper;
    final PersonMapper personMapper;

    public PersonQueryController(PersonRepository personRepository, PersonArgumentValidator personArgumentValidator,
                                 Statistic statistic, MovieMapper movieMapper, PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.personArgumentValidator = personArgumentValidator;
        this.statistic = statistic;
        this.movieMapper = movieMapper;
        this.personMapper = personMapper;
    }

    @Cacheable(value = "person-name", key = "#name")
    @GetMapping()
    public List<PersonDto> findByName(@RequestParam(name = "name") String... name) {
        personArgumentValidator.validatePersonName(name);
        return personMapper.toPersonDtos(personRepository.getPersonByName(name));
    }

    @Cacheable(value = "movie", key = "#name")
    @GetMapping(path = "/movie")
    public List<MovieDto> findAllMovie(@RequestParam(name = "name") String name) {
        personArgumentValidator.validateMovieName(name);
        return movieMapper.toMovieDtos(personRepository.findMovies(name));
    }

    @Cacheable(value = "degree", key = "{#firstuid, #seconduid}")
    @GetMapping(path = "/degree")
    public Integer getDegreeInt(@RequestParam(name = "firstuid", defaultValue = "nm0000102", required = false) String firstUid,
                                @RequestParam(name = "seconduid") String secondUid) {
        personArgumentValidator.validateRequestForDegree(firstUid, secondUid);
        return personRepository.getShortPath(firstUid, secondUid);
    }

    @Cacheable(value = "share", key = "{#firstuid, #seconduid}")
    @GetMapping(path = "/share")
    public List<MovieDto> getShare(@RequestParam(name = "firstuid", defaultValue = "nm0000102", required = false) String firstUid,
                                   @RequestParam(name = "seconduid") String secondUid) {
        personArgumentValidator.validateRequestForShare(firstUid, secondUid);
        return movieMapper.toMovieDtos(personRepository.getShareMovie(firstUid, secondUid));
    }


    @Cacheable(value = "genres", key = "{#uid}")
    @GetMapping(path = "/genres")
    public List<GenrePercentDto> getGenere(@RequestParam(name = "uid", required = false) String uid) {
        List<String> movieGenre = personRepository.getGenreOfArtistByUid(uid);
        return movieMapper.toGenrePercentDto(statistic.genreStats(movieGenre));
    }

    @Cacheable(value = "typecast", key = "{#uid}")
    @GetMapping(path = "/typecast")
    public TypeCastDto getTypecast(@RequestParam(name = "uid") String uid) {
        personArgumentValidator.validateRequestTypeScript(uid);
        List<GenrePercent> genrePercents = movieMapper.toGenrePercentModel(getGenere(uid));
        personArgumentValidator.validateGenreTypescript(genrePercents);
        Collections.sort(genrePercents);
        TypeCastModel typeCastModel = new TypeCastModel(genrePercents.get(0).getPercent() >= 50 ? genrePercents.get(0).getName() : null, genrePercents);
        return movieMapper.toTypeCastDto(typeCastModel);
    }

}
