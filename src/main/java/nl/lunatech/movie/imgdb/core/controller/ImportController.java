package nl.lunatech.movie.imgdb.core.controller;


import nl.lunatech.movie.imgdb.core.pojo.model.ItemCondition;
import nl.lunatech.movie.imgdb.core.service.ImportDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author alikhandani
 * @created 21/05/2022
 * @project Lobox
 */

@RestController
@RequestMapping("/api")
public class ImportController {

    @Autowired
    ImportDataService importDataService;

    @GetMapping(path = "/import/movies")
    public Map<String, ItemCondition> importAllMovies() {
        return importDataService.importMovies();
    }

    @GetMapping(path = "/import/person")
    public Map<String, ItemCondition> importAllPerson() {
        return importDataService.importCrew();
    }


    @GetMapping(path = "/import/relation")
    public Map<String, ItemCondition> importRelation() {
        return importDataService.importCastRelations();
    }


    @GetMapping(path = "/import/statistic")
    public Object getStatistic() {
        return importDataService.getConditions();
    }

}
