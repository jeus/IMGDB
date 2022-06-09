package nl.lunatech.movie.imgdb.common.helper;

import nl.lunatech.movie.imgdb.core.pojo.model.GenrePercent;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author alikhandani
 * @created 03/06/2020
 * @project lunatech
 */
@Component
public class Statistic {

    public List<GenrePercent> genreStats(List<String> movieGenre) {
        int sum = 0;
        Map<String, Integer> genreMap = new HashMap<>();
        for (String genres : movieGenre) {
            for (String genre : genres.split(",")) {
                sum++;
                Integer current = genreMap.get(genre);
                int count = current == null ? 1 : current + 1;
                genreMap.put(genre, count);
            }
        }
        List<GenrePercent> genrePercents = new ArrayList<>();
        for (String str : genreMap.keySet()) {
            int count = genreMap.get(str);
            float percent = percent(count, sum);
            genrePercents.add(new GenrePercent(str, count, percent, sum));
        }
        Collections.sort(genrePercents);
        return genrePercents;
    }




    public static float percent(int count, int total) {
        float percent = (int) (count / (total / 100f));
        return percent;
    }

    public static float percent(int count, long total) {
        float percent = (int) (count / (total / 100f));
        return percent;
    }




}
