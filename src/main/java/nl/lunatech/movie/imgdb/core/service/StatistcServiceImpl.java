package nl.lunatech.movie.imgdb.core.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author alikhandani
 * @created 09/06/2022
 * @project imgdb
 */
@Service
public class StatistcServiceImpl implements StatisticService {
    public static Map<String, AtomicInteger> statistics = new HashMap<>();


    @Override
    public Map<String, AtomicInteger> getApiCalls() {
        Map<String, AtomicInteger> result = statistics;
        statistics = new HashMap<>();
        return result;
    }
}
