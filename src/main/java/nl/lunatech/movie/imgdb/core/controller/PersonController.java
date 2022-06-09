package nl.lunatech.movie.imgdb.core.controller;

import nl.lunatech.movie.imgdb.common.helper.ArgumentValidator;
import nl.lunatech.movie.imgdb.common.translator.MovieMapper;
import nl.lunatech.movie.imgdb.common.translator.PersonMapper;
import nl.lunatech.movie.imgdb.core.dao.PersonRepository;
import nl.lunatech.movie.imgdb.core.pojo.dto.GenrePercentDto;
import nl.lunatech.movie.imgdb.core.pojo.dto.PersonDto;
import nl.lunatech.movie.imgdb.core.pojo.dto.TypeCastDto;
import nl.lunatech.movie.imgdb.core.service.MovieService;
import nl.lunatech.movie.imgdb.core.service.PersonService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author alikhandani
 * @created 28/05/2020
 * @project lunatech
 */
@RestController
@RequestMapping("/api")
public class PersonController {

    final PersonRepository personRepository;
    final ArgumentValidator personArgumentValidator;
    final PersonService personService;
    final MovieService movieService;
    final MovieMapper movieMapper;
    final PersonMapper personMapper;

    public PersonController(PersonRepository personRepository, ArgumentValidator personArgumentValidator,
                            MovieService movieService, PersonService personService, MovieMapper movieMapper,
                            PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.personArgumentValidator = personArgumentValidator;
        this.personService = personService;
        this.movieService = movieService;
        this.movieMapper = movieMapper;
        this.personMapper = personMapper;
    }

    @Cacheable(value = "person-name", key = "#name")
    @GetMapping("/persons")
    public List<PersonDto> findByName(@RequestParam(name = "name") String... name) {
        personArgumentValidator.validatePersonName(name);
        return personMapper.toPersonDtos(personRepository.getPersonByName(name));
    }


    @Cacheable(value = "degree", key = "{#firstpid, #secondpid}")
    @GetMapping(path = "/person/degree")
    public Integer getDegreeInt(@RequestParam(name = "firstpid", defaultValue = "102", required = false) int firstPid,
                                @RequestParam(name = "secondpid") int secondPid) {

        personArgumentValidator.validateRequestForDegree(firstPid, secondPid);
        return personService.getDegree(firstPid, secondPid);
    }


    @Cacheable(value = "genres", key = "{#pid}")
    @GetMapping(path = "/person/genres")
    public List<GenrePercentDto> getGenres(@RequestParam(name = "pid", required = false) int pid) {
        return movieMapper.toGenrePercentDto(personService.getGenere(pid));
    }

    @Cacheable(value = "typecast", key = "{#pid}")
    @GetMapping(path = "/person/typecast")
    public TypeCastDto getTypecast(@RequestParam(name = "pid") int pid) {
        return movieMapper.toTypeCastDto(personService.getTypecast(pid));
    }

    @PostMapping(path = "/person")
    public PersonDto AddNewMovieByTsv(@NotNull @RequestBody String tsvRow) {
        return personMapper.toPersonDto(personService.saveByTsv(tsvRow));
    }

}
