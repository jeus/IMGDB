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

import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Scope("application")
public class ImportDataServiceImpl implements ImportDataService {
    private final static Logger LOG = LoggerFactory.getLogger(ImportDataServiceImpl.class);
    @Autowired
    private ResourceLoader resourceLoader;

    private final String TITLE_BASIC;
    private final String NAME_BASIC;
    private final String TITLE_PRINCIPAL;
    private final String TITLE_RATING;
    private final String TITLE_CREW;

    public static final String MOVIE = "MOVIE";
    public static final String PERSON = "PERSON";
    public static final String RELATION = "RELATION";

    public AtomicInteger titleBasic = new AtomicInteger(0);
    public AtomicInteger nameBasic = new AtomicInteger(0);
    public AtomicInteger titlePrincipal = new AtomicInteger(0);

    public long totalTitleBasic;
    public long totalNameBasic;
    public long totalTitlePrincipal;

    public ImportDataServiceImpl(@Value("${dataset.path}") String path,
                                 @Value("${dataset.title_basic}") String titleBasic,
                                 @Value("${dataset.name_basic}") String nameBasic,
                                 @Value("${dataset.title_principal}") String titlePrincipal,
                                 @Value("${dataset.title_rating}") String titleRating,
                                 @Value("${dataset.title_crew}") String titleCrew) {
        var PATH = path;
        try {
            PATH = ResourceUtils.getFile("classpath:" + path).getPath();
            LOG.info("---------->"+PATH);
        } catch (Exception ex) {
            LOG.error("CAN'T LOAD FILES");
        }
        TITLE_BASIC = PATH + titleBasic;
        NAME_BASIC = PATH + nameBasic;
        TITLE_PRINCIPAL = PATH + titlePrincipal;
        TITLE_RATING = PATH + titleRating;
        TITLE_CREW = PATH + titleCrew;
        LOG.error(TITLE_BASIC);


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
            if (titleBasic.get() != 0) {
                return getCurrentConditions();
            } else {
                titleBasic.incrementAndGet();
            }
        }
        setInitiateCondition();

        StringDecoder stringDecoder = StringDecoder.textPlainOnly();
        StringDecoder stringDecoder1 = StringDecoder.textPlainOnly();
        Map<Integer, Crew> crewMap = DataBufferUtils.readAsynchronousFileChannel(() -> AsynchronousFileChannel.open(Path.of(TITLE_CREW),
                StandardOpenOption.READ), DefaultDataBufferFactory.sharedInstance, 1024)
                .transform(dataBufferFlux -> stringDecoder1.decode(dataBufferFlux, null, null, null))
                .map(TsvParser::titleCrewToCrew).collectMap(Crew::getMid, Crew::getThis).block();
        Map<Integer, Rating> ratingMap = DataBufferUtils.readAsynchronousFileChannel(() -> AsynchronousFileChannel.open(Path.of(TITLE_RATING),
                StandardOpenOption.READ), DefaultDataBufferFactory.sharedInstance, 1024)
                .transform(dataBufferFlux -> stringDecoder1.decode(dataBufferFlux, null, null, null))
                .map(TsvParser::titleRatingToRating).collectMap(Rating::getMid, Rating::getThis).block();


        Flux<Movie> flux = DataBufferUtils.readAsynchronousFileChannel(() -> AsynchronousFileChannel.open(Path.of(TITLE_BASIC),
                StandardOpenOption.READ), DefaultDataBufferFactory.sharedInstance, 1024)
                .transform(dataBufferFlux -> stringDecoder.decode(dataBufferFlux, null, null, null))
                .map(TsvParser::titleBasicToMovie).filter(movie -> movie.getMid() != null)
                .map(movie -> movie.setDetails(ratingMap.get(movie.getMid()), crewMap.get(movie.getMid()))).map(jpaMovieDao::saveAndFlush);

        flux.parallel(3).subscribe(a -> LOG.info("Movie:" + titleBasic.incrementAndGet()), err -> LOG.error("-----" + err));

        return getConditions();
    }

    @Override
    public Map<String, ItemCondition> importCrew() {
        synchronized (this) {
            if (nameBasic.get() != 0) {
                return getCurrentConditions();
            } else {
                nameBasic.incrementAndGet();
            }
        }
        StringDecoder stringDecoder = StringDecoder.textPlainOnly();

        Flux<Person> flux = DataBufferUtils.readAsynchronousFileChannel(() -> AsynchronousFileChannel.open(Path.of(NAME_BASIC),
                StandardOpenOption.READ), DefaultDataBufferFactory.sharedInstance, 1024)
                .transform(dataBufferFlux -> stringDecoder.decode(dataBufferFlux, null, null, null))
                .map(TsvParser::nameBasicToPerson).filter(person -> person.getPid() != null).map(jpaPersonDao::saveAndFlush);

        flux.parallel(3).subscribe(a -> LOG.info("PERSON count:" + nameBasic.incrementAndGet()), err -> LOG.error("-----" + err));
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
                titlePrincipal.incrementAndGet();
            }

        }
        StringDecoder stringDecoder = StringDecoder.textPlainOnly();

        Flux<Person> flux = DataBufferUtils.readAsynchronousFileChannel(() -> AsynchronousFileChannel.open(Path.of(TITLE_PRINCIPAL),
                StandardOpenOption.READ), DefaultDataBufferFactory.sharedInstance, 1024)
                .transform(dataBufferFlux -> stringDecoder.decode(dataBufferFlux, null, null, null))
                .map(TsvParser::titlePrincipalToRelation).filter(relation -> relation.getPid() != null)
                .map(relation -> jpaPersonDao.joinById(relation.getPid(), relation.getMid(), relation.getTitle()))
                .onErrorContinue((throwable, o) -> LOG.error("Error:" + ((Relation) o).getPid() + "->" + ((Relation) o).getMid()));

        flux.parallel(3).subscribe(a -> LOG.info("CAST RELATION Pid"+a.getPid() + "  Number:" + titlePrincipal.incrementAndGet())
                , err -> LOG.error(err.getCause() + "-----" + err));
        return getConditions();
    }


    @Override
    public Map<String, ItemCondition> importMainCrews(String str) {
        return null;
    }


    @Override
    public Map<String, ItemCondition> getConditions() {
        Map<String, ItemCondition> importStatistics = new HashMap<>();

        ItemCondition movieStatistic = new ItemCondition("Movies", "title.basic", "Import Movies To Database",
                (int) Statistic.percent(titleBasic.get(), totalTitleBasic), titleBasic.get(),
                getStatus(titleBasic.get(), totalTitleBasic), 0);
        importStatistics.put(MOVIE, movieStatistic);

        ItemCondition personStatistic = new ItemCondition("Person", "name.basic", "Import person To Database",
                (int) Statistic.percent(nameBasic.get(), totalNameBasic), nameBasic.get(),
                getStatus(nameBasic.get(), totalNameBasic), 1);
        importStatistics.put(PERSON, personStatistic);

        ItemCondition principalStatistic = new ItemCondition("RELATION", "title.principals", "Import Relations To Database",
                (int) Statistic.percent(titlePrincipal.get(), totalTitlePrincipal), titlePrincipal.get(),
                getStatus(titlePrincipal.get(), totalTitlePrincipal), 2);
        importStatistics.put(RELATION, principalStatistic);

        return importStatistics;
    }


    private Map<String, ItemCondition> getCurrentConditions() {
        return getConditions();
    }

    private void setInitiateCondition() {
        Path fileTitleBasic = Paths.get(TITLE_BASIC);
        Path fileTitlePrincipal = Paths.get(TITLE_PRINCIPAL);
        Path fileNameBasic = Paths.get(NAME_BASIC);

        try {
            long totalTitleBasic = Files.lines(fileTitleBasic).count();
            long totalTitlePrincipal = Files.lines(fileTitlePrincipal).count();
            long totalNameBasic = Files.lines(fileNameBasic).count();
            LOG.info("TITLE_BASIC:" + totalTitleBasic
                    + "  TITLE_PRINCIPAL:" + totalTitlePrincipal
                    + "  NAME_BASIC:" + totalNameBasic + "-------" + titleBasic.get());

            this.totalTitleBasic = totalTitleBasic;
            this.totalTitlePrincipal = totalTitlePrincipal;
            this.totalNameBasic = totalNameBasic;
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

}
