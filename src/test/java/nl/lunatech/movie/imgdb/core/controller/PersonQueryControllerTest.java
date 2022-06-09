package nl.lunatech.movie.imgdb.core.controller;

import nl.lunatech.movie.imgdb.core.dao.MovieRepository;
import nl.lunatech.movie.imgdb.core.dao.PersonRepository;
import nl.lunatech.movie.imgdb.core.pojo.domain.Movie;
import nl.lunatech.movie.imgdb.core.pojo.dto.TypeCastDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

/**
 * @author alikhandani
 * @created 03/06/2020
 * @project lunatech
 */
@SpringBootTest
class PersonQueryControllerTest {

    @MockBean
    MovieRepository movieRepository;

    @Autowired
    PersonController personController;

     @Test
    void getGenere() {
        Mockito.when(movieRepository.getGenreOfArtistByPid(anyInt())).thenReturn(mockmMovieGenre());

        TypeCastDto typeCastDto = personController.getTypecast(0);
        assertEquals("darama", typeCastDto.getTypeCastGenre());
        assertEquals(6, typeCastDto.getGenrePercents().size());
    }


    private List<Movie> mockmMovieGenre() {
       var m1 = new Movie();
       var m2 = new Movie();
       var m3 = new Movie();
       var m4 = new Movie();
       var m5 = new Movie();


        String[] a1 = {"comedy", "darama"};
        String[] a2 = {"sport,darama"};
        String[] a3 = {"ducumentry,darama"};
        String[] a4 = {"horror,darama"};
        String[] a5 = {"history,darama"};

        m1.setGenres(Arrays.asList(a1));
        m2.setGenres(Arrays.asList(a2));
        m3.setGenres(Arrays.asList(a3));
        m4.setGenres(Arrays.asList(a4));
        m5.setGenres(Arrays.asList(a5));

        List<Movie> movies = new ArrayList<>();
        movies.add(m1);
        movies.add(m2);
        movies.add(m3);
        movies.add(m4);
        movies.add(m5);
        return movies;
    }
}