package nl.lunatech.movie.imgdb.core.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author alikhandani
 * @created 09/06/2022
 * @project imgdb
 */
@Service
public interface StatisticService {
    Map<String, AtomicInteger> getApiCalls();
}
