package nl.lunatech.movie.imgdb.core.controller;

import nl.lunatech.movie.imgdb.core.pojo.dto.MovieDto;
import nl.lunatech.movie.imgdb.core.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Using for checking swagger and web service
 * @author alikhandani
 * @created 27/05/2020
 * @project imgdb
 */
@RestController
@RequestMapping("/api")
public class StatisticController {

    @Autowired
    StatisticService statisticService;

    @GetMapping(path = "/statistic")
    public Map<String, AtomicInteger> getTopBoxOffice() {
        return statisticService.getApiCalls();
    }

}