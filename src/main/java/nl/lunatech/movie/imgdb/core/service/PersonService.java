package nl.lunatech.movie.imgdb.core.service;

import nl.lunatech.movie.imgdb.core.pojo.domain.Movie;
import nl.lunatech.movie.imgdb.core.pojo.domain.Person;
import nl.lunatech.movie.imgdb.core.pojo.model.GenrePercent;
import nl.lunatech.movie.imgdb.core.pojo.model.TypeCastModel;

import java.util.List;

public interface PersonService {
     List<GenrePercent> getGenere(int pid);

     TypeCastModel getTypecast(int pid);

     Integer getDegree( int firstPid, int secondPid) ;

     Person saveByTsv(String tsv);
}
