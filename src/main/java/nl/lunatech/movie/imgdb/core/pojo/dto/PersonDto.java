package nl.lunatech.movie.imgdb.core.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


import java.util.List;

/**
 * @author AliKhandani
 * @created 02/06/2020
 * @project lunatech
 */
@Data
@AllArgsConstructor
public class PersonDto {

    private String uid;
    private String name;
    private List<String> knownForTitles;
    private List<String> primaryProfession;
    private int birthyear;

}
