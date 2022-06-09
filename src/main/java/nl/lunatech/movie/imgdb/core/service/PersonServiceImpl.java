package nl.lunatech.movie.imgdb.core.service;

import nl.lunatech.movie.imgdb.common.helper.ArgumentValidator;
import nl.lunatech.movie.imgdb.common.helper.Statistic;
import nl.lunatech.movie.imgdb.common.helper.TsvParser;
import nl.lunatech.movie.imgdb.common.translator.MovieMapper;
import nl.lunatech.movie.imgdb.core.dao.JpaPersonDao;
import nl.lunatech.movie.imgdb.core.dao.MovieRepository;
import nl.lunatech.movie.imgdb.core.dao.PersonRepository;
import nl.lunatech.movie.imgdb.core.pojo.domain.Movie;
import nl.lunatech.movie.imgdb.core.pojo.domain.Person;
import nl.lunatech.movie.imgdb.core.pojo.model.GenrePercent;
import nl.lunatech.movie.imgdb.core.pojo.model.TypeCastModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonServiceImpl implements PersonService {
    @Autowired
    PersonRepository personRepository;
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    Statistic statistic;
    @Autowired
    MovieMapper movieMapper;
    @Autowired
    ArgumentValidator argumentValidator;
    @Autowired
    JpaPersonDao jpaPersonDao;

    public List<GenrePercent> getGenere(int pid) {
        var listArrayGenre = movieRepository.getGenreOfArtistByPid(pid);
        List<String> movieGenre = listArrayGenre.stream().map(Movie::getGenres).flatMap(Collection::stream).collect(Collectors.toList());
        return statistic.genreStats(movieGenre);
    }

    public TypeCastModel getTypecast(int pid) {
        argumentValidator.validateRequestTypeScript(pid);
        List<GenrePercent> genrePercents = getGenere(pid);
        argumentValidator.validateGenreTypescript(genrePercents);
        Collections.sort(genrePercents);
        TypeCastModel typeCastModel = new TypeCastModel(genrePercents.get(0).getPercent() >= 50 ? genrePercents.get(0).getName() : null, genrePercents);
        return typeCastModel;
    }

    @Override
    public Integer getDegree(int firstPid, int secondPid) {
        return jpaPersonDao.getShortestPath(firstPid, secondPid);
    }

    public Person saveByTsv(String tsv) {
        try {
            Person person = TsvParser.nameBasicToPerson(tsv);
            Person newPerson = jpaPersonDao.saveAndFlush(person);
            return newPerson;
        } catch (Exception e) {
            return null;
        }
    }
}
