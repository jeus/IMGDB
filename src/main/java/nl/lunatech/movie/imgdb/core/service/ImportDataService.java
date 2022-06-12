package nl.lunatech.movie.imgdb.core.service;

import nl.lunatech.movie.imgdb.core.pojo.model.ItemCondition;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public interface ImportDataService {
    Map<String, ItemCondition> importMovies();

    Map<String, ItemCondition> importCrew();

    Map<String, ItemCondition> importCastRelations();

    Map<String, ItemCondition> getConditions();
}
