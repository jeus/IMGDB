package nl.lunatech.movie.imgdb.common.helper;

import nl.lunatech.movie.imgdb.common.exception.AssertingException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author alikhandani
 * @created 02/06/2020
 * @project lunatech
 */
@Component
public class AssertHelperImpl implements AssertHelper {

    @Override
    public void notNull(Object object, Map<String, Object> params) throws AssertingException {
        assert (params != null) : "params can not be null!";
        if (object == null) {
            throw new AssertingException("lunatech.movie.imgdb.notNull", params);
        }
    }

    @Override
    public void notNull(Object... object) throws AssertingException {
        if (object == null) {
            throw new AssertingException("lunatech.movie.imgdb.notNull");
        }
    }

    @Override
    public void notNull(String str) throws AssertingException {
        if (str == null) {
            throw new AssertingException("lunatech.movie.imgdb.notNull");
        }
    }

    @Override
    public void notEmpty(Collection<?> collection) throws AssertingException {
        if ((collection == null) || (collection.isEmpty())) {
            throw new AssertingException("lunatech.movie.imgdb.collection_is_empty");
        }
    }

    @Override
    public <T> void notEqual(T first, T second) throws AssertingException {
        if (first == second) {
            throw new AssertingException("lunatech.movie.imgdb.notNull.is-not-equal");
        }
        if ((first != null) && first.equals(second)) {
            throw new AssertingException("lunatech.movie.imgdb.notNull.is-not-equal");
        }
    }

    public void notBigger(int first, int second) throws AssertingException {
        if (first > second) {
            throw new AssertingException("lunatech.movie.imgdb.notNull.is-bigger");
        }
    }

}
