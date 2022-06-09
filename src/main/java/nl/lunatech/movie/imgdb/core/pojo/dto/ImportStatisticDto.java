package nl.lunatech.movie.imgdb.core.pojo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import nl.lunatech.movie.imgdb.core.pojo.model.ImportStatus;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ImportStatisticDto {

   private String name;
   private String title;
   private String description;
   private int percent;
   private long rowCount;
   private ImportStatus status;
   @JsonIgnore
   private int order;
}
