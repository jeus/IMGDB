package nl.lunatech.movie.imgdb.common.helper;

import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;

import static java.util.Collections.singletonMap;

/**
 * @author alikhandani
 * @created 02/06/2020
 * @project lunatech
 */
@Component
public class ArgumentValidator {

    protected final AssertHelper assertHelper;

    public ArgumentValidator(AssertHelper assertHelper) {
        this.assertHelper = assertHelper;
    }

    public void validatePersonName(String... name) {
        assertHelper.notNull(name);
    }

    public void validateMovieName(String mname) {
        assertHelper.notNull(mname);
    }

    public void validateRequestForDegree(int firstPid, int secondPid) {
        assertHelper.notNull(firstPid, singletonMap("firstPid", "lunatech.movie.imgdb.person.degree"));
        assertHelper.notNull(secondPid, singletonMap("secondPid", "lunatech.movie.imgdb.person.degree"));
        assertHelper.notEqual(firstPid, secondPid);
    }

    public void validateRequestForShare(int firstPid, int secondPid) {
        assertHelper.notNull(firstPid, singletonMap("firstPid", "lunatech.movie.imgdb.person.degree"));
        assertHelper.notNull(secondPid, singletonMap("secondPid", "lunatech.movie.imgdb.person.degree"));
        assertHelper.notEqual(firstPid, secondPid);
    }

    public void validateRequestTypeScript(int pid) {
        assertHelper.notNull(pid);
    }

    public void validateGenreTypescript(List<?> list) {
        assertHelper.notEmpty(list);
    }

    public void validateYear(int year) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        assertHelper.notBigger(year, currentYear);
    }
}
