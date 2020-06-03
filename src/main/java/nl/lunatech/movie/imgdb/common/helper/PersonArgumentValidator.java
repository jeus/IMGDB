package nl.lunatech.movie.imgdb.common.helper;

import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Collections.singletonMap;

/**
 * @author alikhandani
 * @created 02/06/2020
 * @project lunatech
 */
@Component
public class PersonArgumentValidator {

    protected final AssertHelper assertHelper;

    public PersonArgumentValidator(AssertHelper assertHelper) {
        this.assertHelper = assertHelper;
    }

    public void validatePersonName(String...name){
        assertHelper.notNull(name);
    }

    public void validateMovieName(String name){
        assertHelper.notNull(name);
    }

    public void validateRequestForDegree(String firstUid, String secondUid) {
        assertHelper.notNull(firstUid, singletonMap("firstUid", "lunatech.movie.imgdb.person.degree"));
        assertHelper.notNull(secondUid, singletonMap("secondUid", "lunatech.movie.imgdb.person.degree"));
        assertHelper.NotEqual(firstUid,secondUid);
    }

    public void validateRequestForShare(String firstUid, String secondUid) {
        assertHelper.notNull(firstUid, singletonMap("firstUid", "lunatech.movie.imgdb.person.degree"));
        assertHelper.notNull(secondUid, singletonMap("secondUid", "lunatech.movie.imgdb.person.degree"));
        assertHelper.NotEqual(firstUid,secondUid);
    }

    public void validateRequestTypeScript(String uid) {
        assertHelper.notNull(uid);
    }


    public void validateGenreTypescript(List<?> list)
    {
        assertHelper.notEmpty(list);
    }

}
