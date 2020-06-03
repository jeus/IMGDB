package nl.lunatech.movie.imgdb.core.rest;

import nl.lunatech.movie.imgdb.core.pojo.dto.TypeCastDto;
import nl.lunatech.movie.imgdb.core.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

/**
 * @author alikhandani
 * @created 03/06/2020
 * @project lunatech
 */
@SpringBootTest
class PersonQueryControllerTest {

    @MockBean
    PersonRepository personRepository;

    @Autowired
    PersonQueryController personQueryController;

    @Test
    void getGenere() {
        Mockito.when(personRepository.getGenreOfArtistByUid(any())).thenReturn(mockmMovieGenre());

        TypeCastDto typeCastDto = personQueryController.getTypecast("UID");
        assertEquals("darama", typeCastDto.getTypeCastGenre());
        assertEquals(6, typeCastDto.getGenrePercents().size());
    }


    private List<String> mockmMovieGenre() {
        List<String> genres = new ArrayList<>();
        genres.add("comedy,darama");
        genres.add("sport,darama");
        genres.add("ducumentry,darama");
        genres.add("horror,darama");
        genres.add("history,darama");
        return genres;
    }
}