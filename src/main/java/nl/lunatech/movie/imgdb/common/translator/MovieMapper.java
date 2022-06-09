package nl.lunatech.movie.imgdb.common.translator;

import nl.lunatech.movie.imgdb.core.pojo.domain.Movie;
import nl.lunatech.movie.imgdb.core.pojo.dto.GenrePercentDto;
import nl.lunatech.movie.imgdb.core.pojo.dto.MovieDto;
import nl.lunatech.movie.imgdb.core.pojo.dto.TypeCastDto;
import nl.lunatech.movie.imgdb.core.pojo.model.GenrePercent;
import nl.lunatech.movie.imgdb.core.pojo.model.TypeCastModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.Collection;
import java.util.List;

/**
 * @author alikhandani
 * @created 03/06/2020
 * @project lunatech
 */
@Mapper(componentModel = "spring")
public interface MovieMapper {

    @Mappings({
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "title", source = "title"),
            @Mapping(target = "type", source = "type"),
            @Mapping(target = "genres", source = "genres"),
            @Mapping(target = "release", source = "release"),
            @Mapping(target = "description", source = "description"),
    })
    MovieDto toMovieDto(Movie movie);

    List<MovieDto> toMovieDtos(List<Movie> movie);

    Collection<MovieDto> toMovieDtos(Collection<Movie> movie);

    GenrePercentDto toGenrePercentDto(GenrePercent genrePercent);

    TypeCastDto toTypeCastDto(TypeCastModel typeCastModel);

    GenrePercent toGenrePercentModel(GenrePercentDto genrePercentDto);

    List<GenrePercent> toGenrePercentModel(List<GenrePercentDto> genrePercents);

    List<GenrePercentDto> toGenrePercentDto(List<GenrePercent> genrePercents);

}
