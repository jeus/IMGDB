package nl.lunatech.movie.imgdb.core.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import nl.lunatech.movie.imgdb.core.pojo.model.GenrePercent;

import java.util.List;

/**
 * @author alikhandani
 * @created 03/06/2020
 * @project lunatech
 */
@Data
@AllArgsConstructor
public  class TypeCastDto {

    private  String typeCastGenre;
    private  List<GenrePercent> genrePercents;

}
