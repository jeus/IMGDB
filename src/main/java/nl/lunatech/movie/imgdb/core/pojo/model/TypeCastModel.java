package nl.lunatech.movie.imgdb.core.pojo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author alikhandani
 * @created 03/06/2020
 * @project lunatech
 */
@Data
@AllArgsConstructor
public class TypeCastModel {

    private String typeCastGenre;
    private List<GenrePercent> genrePercents;
}
