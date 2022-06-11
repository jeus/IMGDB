package nl.lunatech.movie.imgdb.core.service;

import nl.lunatech.movie.imgdb.common.helper.Statistic;
import nl.lunatech.movie.imgdb.common.helper.TsvParser;
import nl.lunatech.movie.imgdb.core.dao.JpaMovieDao;
import nl.lunatech.movie.imgdb.core.dao.JpaPersonDao;
import nl.lunatech.movie.imgdb.core.dao.MovieRepository;
import nl.lunatech.movie.imgdb.core.dao.PersonRepository;
import nl.lunatech.movie.imgdb.core.pojo.domain.Movie;
import nl.lunatech.movie.imgdb.core.pojo.domain.Person;
import nl.lunatech.movie.imgdb.core.pojo.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.core.codec.StringDecoder;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Scope("application")
public class ImportDataServiceImpl implements ImportDataService {
    private final static Logger LOG = LoggerFactory.getLogger(ImportDataServiceImpl.class);
    @Autowired
    private ResourceLoader resourceLoader;

    private String PATH;
    private final String TITLE_BASIC_FILE;
    private final String NAME_BASIC_FILE;
    private final String TITLE_PRINCIPAL_FILE;
    private final String TITLE_RATING_FILE;
    private final String TITLE_CREW_FILE;

    public static final String MOVIE = "MOVIE";
    public static final String PERSON = "PERSON";
    public static final String RELATION = "RELATION";
    public static final String RATING = "RATING";
    public static final String CREW = "CREW";

    public AtomicInteger titleBasicInt = new AtomicInteger(0);
    public AtomicInteger titleCrewInt = new AtomicInteger(0);
    public AtomicInteger nameBasicInt = new AtomicInteger(0);
    public AtomicInteger titleRatingInt = new AtomicInteger(0);
    public AtomicInteger titlePrincipalInt = new AtomicInteger(0);

    public long totalTitleBasic;
    public long totalNameBasic;
    public long totalTitleCrew;
    public long totalTitleRating;
    public long totalTitlePrincipal;

    public ImportDataServiceImpl(@Value("${dataset.path}") String path,
                                 @Value("${dataset.title_basic}") String titleBasicInt,
                                 @Value("${dataset.name_basic}") String nameBasic,
                                 @Value("${dataset.title_principal}") String titlePrincipalInt,
                                 @Value("${dataset.title_rating}") String titleRating,
                                 @Value("${dataset.title_crew}") String titleCrew) {

        PATH = path;
        try {
            if (path.startsWith("classpath:")) {
                PATH = ResourceUtils.getFile(path).getPath();
            }
        } catch (Exception ex) {
            LOG.error("CAN'T LOAD FILES" + PATH);
        }
        LOG.info("[PATH] Loading files from:" + PATH);
        TITLE_BASIC_FILE = titleBasicInt;
        NAME_BASIC_FILE = nameBasic;
        TITLE_PRINCIPAL_FILE = titlePrincipalInt;
        TITLE_RATING_FILE = titleRating;
        TITLE_CREW_FILE = titleCrew;


    }

    @Autowired
    JpaMovieDao jpaMovieDao;
    @Autowired
    JpaPersonDao jpaPersonDao;
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    PersonRepository personRepository;


    @Override
    public Map<String, ItemCondition> importMovies() {

        synchronized (this) {
            if (titleCrewInt.get() != 0) {
                LOG.info("[LOADING] LOADING ALL PROCESS... ");
                return getCurrentConditions();
            } else {
                LOG.info("[STARTING] LOADING ALL PROCESS... ");
                titleCrewInt.incrementAndGet();
            }
        }
        setInitiateCondition();

        StringDecoder stringDecoder = StringDecoder.textPlainOnly();
        StringDecoder stringDecoder1 = StringDecoder.textPlainOnly();
        Mono<Map<Integer, Crew>> crewMap = DataBufferUtils.readAsynchronousFileChannel(
                () -> AsynchronousFileChannel.open(pathOf(TITLE_CREW_FILE), StandardOpenOption.READ),
                DefaultDataBufferFactory.sharedInstance, 1024)
                .transform(dataBufferFlux -> stringDecoder1.decode(dataBufferFlux, null, null, null))
                .map(tsvStr -> {
                    titleCrewInt.incrementAndGet();
                    return TsvParser.titleCrewToCrew(tsvStr);
                }).collectMap(Crew::getMid, Crew::getThis).cache();

        Mono<Map<Integer, Rating>> ratingMap = DataBufferUtils.readAsynchronousFileChannel(
                () -> AsynchronousFileChannel.open(pathOf(TITLE_RATING_FILE), StandardOpenOption.READ),
                DefaultDataBufferFactory.sharedInstance, 1024)
                .transform(dataBufferFlux -> stringDecoder1.decode(dataBufferFlux, null, null, null))
                .map(tsvStr -> {
                    titleRatingInt.incrementAndGet();
                    return TsvParser.titleRatingToRating(tsvStr);
                })
                .collectMap(Rating::getMid, Rating::getThis).cache();

        Flux<List<Movie>> flux = DataBufferUtils.readAsynchronousFileChannel(
                () -> AsynchronousFileChannel.open(pathOf(TITLE_BASIC_FILE), StandardOpenOption.READ),
                DefaultDataBufferFactory.sharedInstance, 1024)
                .transform(dataBufferFlux -> stringDecoder.decode(dataBufferFlux, null, null, null))
                .map(tsvStr -> {
                    titleBasicInt.incrementAndGet();
                    return TsvParser.titleBasicToMovie(tsvStr);
                })
                .filter(movie -> movie.getMid() != null)
                .map(movie -> movie.setDetails(ratingMap.block().get(movie.getMid()), crewMap.block().get(movie.getMid()))).buffer(100).map(jpaMovieDao::saveAllAndFlush);


        flux.parallel(3).
                subscribe(a -> LOG.info("[Movie] " + titleBasicInt.get() + " Save in database"),
                        err -> LOG.error("-----" + err), () -> System.out.println("[MOVIE] Done"));
        return getConditions();
    }

    @Override
    public Map<String, ItemCondition> importCrew() {
        synchronized (this) {
            if (nameBasicInt.get() != 0) {
                return getCurrentConditions();
            } else {
                nameBasicInt.incrementAndGet();
            }
        }
        StringDecoder stringDecoder = StringDecoder.textPlainOnly();

        Flux<List<Person>> flux = DataBufferUtils.readAsynchronousFileChannel(
                () -> AsynchronousFileChannel.open(pathOf(NAME_BASIC_FILE), StandardOpenOption.READ),
                DefaultDataBufferFactory.sharedInstance, 1024)
                .transform(dataBufferFlux -> stringDecoder.decode(dataBufferFlux, null, null, null))
                .map(tsvStr -> {
                    nameBasicInt.incrementAndGet();
                    return TsvParser.nameBasicToPerson(tsvStr);
                }).filter(person -> person.getPid() != null).buffer(100).map(jpaPersonDao::saveAllAndFlush);

        flux.parallel(3).subscribe(a -> LOG.info("PERSON count:" + nameBasicInt.get()), err -> LOG.error("-----" + err));
        return getConditions();
    }


    @Override
    public Map<String, ItemCondition> importCastRelations() {
        synchronized (this) {
            if (getConditions().get(MOVIE).getStatus() != ImportStatus.IMPORTED
                    || getConditions().get(PERSON).getStatus() != ImportStatus.IMPORTED
                    || getConditions().get(RELATION).getStatus() != ImportStatus.NOT_IMPORTED) {
                return getCurrentConditions();
            } else {
                titlePrincipalInt.incrementAndGet();
            }

        }
        StringDecoder stringDecoder = StringDecoder.textPlainOnly();

        Flux<Person> flux = DataBufferUtils.readAsynchronousFileChannel(
                () -> AsynchronousFileChannel.open(pathOf(TITLE_PRINCIPAL_FILE), StandardOpenOption.READ),
                DefaultDataBufferFactory.sharedInstance, 1024)
                .transform(dataBufferFlux -> stringDecoder.decode(dataBufferFlux, null, null, null))
                .map(TsvParser::titlePrincipalToRelation).filter(relation -> relation.getPid() != null)
                .map(relation -> jpaPersonDao.joinById(relation.getPid(), relation.getMid(), relation.getTitle()))
                .onErrorContinue((throwable, o) -> LOG.error("Error:" + ((Relation) o).getPid() + "->" + ((Relation) o).getMid()));

        flux.parallel(20).subscribe(a -> LOG.info("[RELATION] Pid:" + a.getPid() + "  count:" + titlePrincipalInt.incrementAndGet())
                , err -> LOG.error(err.getCause() + "-----" + err));
        return getCurrentConditions();
    }


    @Override
    public Map<String, ItemCondition> importMainCrews(String str) {
        return null;
    }


    @Override
    public Map<String, ItemCondition> getConditions() {
        Map<String, ItemCondition> importStatistics = new HashMap<>();

        ItemCondition crewStatistic = new ItemCondition("Crew", TITLE_CREW_FILE, "Loading Crew on RAM",
                (int) Statistic.percent(titleCrewInt.get(), totalTitleCrew), titleCrewInt.get(),
                getStatus(titleCrewInt.get(), totalTitleCrew), "NON");
        importStatistics.put(CREW, crewStatistic);


        ItemCondition ratingStatisctic = new ItemCondition("Rating", TITLE_RATING_FILE, "Loading Rating on RAM",
                (int) Statistic.percent(titleRatingInt.get(), totalTitleRating), titleRatingInt.get(),
                getStatus(titleRatingInt.get(), totalTitleRating), "NON");
        importStatistics.put(RATING, ratingStatisctic);


        ItemCondition movieStatistic = new ItemCondition("Movies", TITLE_BASIC_FILE, "Import Movies To Database",
                (int) Statistic.percent(titleBasicInt.get(), totalTitleBasic), titleBasicInt.get(),
                getStatus(titleBasicInt.get(), totalTitleBasic), "[Crew,Rating]");
        importStatistics.put(MOVIE, movieStatistic);

        ItemCondition personStatistic = new ItemCondition("Persons", NAME_BASIC_FILE, "Import person To Database",
                (int) Statistic.percent(nameBasicInt.get(), totalNameBasic), nameBasicInt.get(),
                getStatus(nameBasicInt.get(), totalNameBasic), "NON");
        importStatistics.put(PERSON, personStatistic);

        ItemCondition principalStatistic = new ItemCondition("Relations", TITLE_PRINCIPAL_FILE, "Import Relations To Database",
                (int) Statistic.percent(titlePrincipalInt.get(), totalTitlePrincipal), titlePrincipalInt.get(),
                getStatus(titlePrincipalInt.get(), totalTitlePrincipal), "[Movie,Person]");
        importStatistics.put(RELATION, principalStatistic);

        return importStatistics;
    }


    private Map<String, ItemCondition> getCurrentConditions() {
        return getConditions();
    }

    private void setInitiateCondition() {


        try {

            this.totalTitleBasic = Files.lines(Paths.get(PATH + TITLE_BASIC_FILE)).count();
            this.totalTitlePrincipal = Files.lines(Paths.get(PATH + TITLE_PRINCIPAL_FILE)).count();
            this.totalNameBasic = Files.lines(Paths.get(PATH + NAME_BASIC_FILE)).count();
            this.totalTitleCrew = Files.lines(Paths.get(PATH + TITLE_CREW_FILE)).count();
            this.totalTitleRating = Files.lines(Paths.get(PATH + TITLE_RATING_FILE)).count();
            LOG.info("[TOTLAS] TITLE_BASIC:{}  TITLE_PRINCIPAL:{}  NAME_BASIC:{} TITLE_CREW:{} TITLE_RATING:{}"
                    , totalTitlePrincipal, totalTitleBasic, totalNameBasic, totalTitleCrew, totalTitleRating);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private ImportStatus getStatus(int count, long total) {
        if (count == 0 || total == 0) {
            return ImportStatus.NOT_IMPORTED;
        }
        if (count < total) {
            return ImportStatus.IMPORTING;
        }
        return ImportStatus.IMPORTED;
    }

    private Path pathOf(String fileName) {
        return Path.of(PATH + fileName);
    }

}
