package nl.lunatech.movie.imgdb.common.helper;

import nl.lunatech.movie.imgdb.core.pojo.domain.Movie;
import nl.lunatech.movie.imgdb.core.pojo.domain.Person;
import nl.lunatech.movie.imgdb.core.pojo.model.Crew;
import nl.lunatech.movie.imgdb.core.pojo.model.Rating;
import nl.lunatech.movie.imgdb.core.pojo.model.Relation;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class TsvParser {
    private final static Logger LOG = LoggerFactory.getLogger(TsvParser.class);
    private static final int TCONST = 0;
    private static final int TITLE_TYPE = 1;
    private static final int PRIMARY_TITLE = 2;
    private static final int ORIGINAL_TITLE = 3;
    private static final int IS_ADULT = 4;
    private static final int START_YEAR = 5;
    private static final int END_YEAR = 6;
    private static final int RUNTIME_MINUTES = 7;
    private static final int GENRES = 8;
    private static final int NCONST = 0;
    private static final int PRIMARY_NAME = 1;
    private static final int BIRTH_YEAR = 2;
    private static final int DEATH_YEAR = 3;
    private static final int PRIMARY_PROFESSION = 4;
    private static final int KNOWN_FOR_TITLES = 5;
    private static final int AVG_RATING = 1;
    private static final int NUM_VOTES = 2;
    private static final int DIRECTOR = 1;
    private static final int WRITER = 2;


    /**
     * tconst|	titleType|	primaryTitle|	originalTitle|	isAdult	startYear	endYear	runtimeMinutes	genres
     * tt1832382	movie	A Separation	Jodaeiye Nader az Simin	0	2011	\N	123	Drama
     *
     * @param tsvRow
     * @return Movie
     */
    public static Movie titleBasicToMovie(String tsvRow) {

        Movie movie = new Movie();
        String[] titleBasic = tsvRow.split("\t");
        movie.setMid(keyExtract(titleBasic[TCONST]));
        movie.setName(checkNull(titleBasic[PRIMARY_TITLE]));
        movie.setTitle(checkNull(titleBasic[ORIGINAL_TITLE]));
        movie.setType(checkNull(titleBasic[TITLE_TYPE]));
        movie.setRelease(parseToInt(titleBasic[END_YEAR]));
        movie.setGenres(Arrays.asList(checkNullArray(titleBasic[GENRES])));
        movie.setRelease(parseToInt(titleBasic[START_YEAR]));
        movie.setDescription("{" +
                "\"isAdult\":\"" + checkNull(titleBasic[IS_ADULT], "") + "\"," +
                "\"runtime-minute\":\"" + checkNull(titleBasic[RUNTIME_MINUTES], "") + "\"" +
                "}");

        return movie;
    }

    public static Rating titleRatingToRating(String tsvRow) {
        String[] titleRating = tsvRow.split("\t");
        var mid = keyExtract(titleRating[TCONST]);
        if (mid != null)
            if (mid % 100000 == 0)
                LOG.info("[RATE] "+mid + " rate on ram");
        return new Rating(keyExtract(titleRating[TCONST]), parseFloat(titleRating[AVG_RATING]), parseToInt(titleRating[NUM_VOTES]));
    }

    public static Crew titleCrewToCrew(String tsvRow) {

        String[] titleCrew = tsvRow.split("\t");
        var mid = keyExtract(titleCrew[TCONST]);
        if (mid != null)
            if (mid % 1000000 == 0)
                LOG.info("[CREW] "+mid + " crew on ram");
        var diretorPids = titleCrew[DIRECTOR];
        var writerPids = titleCrew[WRITER];


        var crew = new Crew(keyExtract(titleCrew[TCONST]), checkNull(diretorPids), checkNull(writerPids), directorWriters(keyExtractorSet(diretorPids), keyExtractorSet(writerPids)));
        return crew;
    }

    /**
     * nconst	primaryName	birthYear	deathYear	primaryProfession	knownForTitles
     * nm1410815	Asghar Farhadi	1972	\N	writer,director,producer	tt1832382,tt2404461,tt5186714,tt1360860
     *
     * @param tsvRow
     * @return
     */
    public static Person nameBasicToPerson(String tsvRow) {
        Person person = new Person();
        String[] titleBasic = tsvRow.split("\t");
        person.setPid(keyExtract(titleBasic[NCONST]));
        person.setName(checkNull(titleBasic[PRIMARY_NAME]));
        person.setBirthyear(parseToInt(titleBasic[BIRTH_YEAR]));
        person.setDeathyear(parseToInt(titleBasic[DEATH_YEAR]));
        person.setKnownForTitles(Arrays.asList(checkNullArray(titleBasic[KNOWN_FOR_TITLES])));
        person.setPrimaryProfession(Arrays.asList(checkNullArray(titleBasic[PRIMARY_PROFESSION])));

        return person;
    }


    /**
     * nconst	primaryName	birthYear	deathYear	primaryProfession	knownForTitles
     * nm1410815	Asghar Farhadi	1972	\N	writer,director,producer	tt1832382,tt2404461,tt5186714,tt1360860
     *
     * @param tsvRow
     * @return
     */
    public static Relation titlePrincipalToRelation(String tsvRow) {
        Relation relation = new Relation();
        String[] titlePrincipals = tsvRow.split("\t");

        relation.setMid(keyExtract(titlePrincipals[0]));
        relation.setPid(keyExtract(titlePrincipals[2]));
        relation.setTitle(checkNull(titlePrincipals[3]));

        return relation;
    }

    private static Float parseFloat(String text) {
        try {
            return Float.parseFloat(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Integer parseToInt(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }


    private static Integer keyExtract(String strId) {
        if (strId.equals("\\N") || strId.isEmpty())
            return null;
        return parseToInt(strId.substring(2));
    }

    private static String checkNull(String text) {
        if (text.equals("\\N"))
            return null;
        else
            return text;
    }

    private static String checkNull(String text, String defStr) {
        if (text.equals("\\N"))
            return defStr;
        else
            return text;
    }

    private static String[] checkNullArray(String text) {
        if (text.equals("\\N"))
            return ArrayUtils.EMPTY_STRING_ARRAY;
        else
            return text.split(",");
    }

    private static Set<Integer> keyExtractorSet(String str) {
        if (str.equals("\\N"))
            return new HashSet<>();
        return Arrays.stream(str.split(",")).map(TsvParser::keyExtract).collect(Collectors.toSet());
    }


    private static Set<Integer> directorWriters(Set<Integer> directorPids, Set<Integer> writerPids) {
        if (directorPids.isEmpty() || writerPids.isEmpty()) {
            return new HashSet<>();
        }

        Set<Integer> intersection = new HashSet<>(directorPids);
        intersection.retainAll(writerPids);
        return intersection;
    }

}
