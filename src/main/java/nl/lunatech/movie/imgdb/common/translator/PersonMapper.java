package nl.lunatech.movie.imgdb.common.translator;

import nl.lunatech.movie.imgdb.core.pojo.domain.Person;
import nl.lunatech.movie.imgdb.core.pojo.dto.PersonDto;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author alikhandani
 * @created 03/06/2020
 * @project lunatech
 */
@Mapper(componentModel = "spring")
public interface PersonMapper {


    PersonDto toPersonDto(Person person);

    List<PersonDto> toPersonDtos(List<Person> person);

}
