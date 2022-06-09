package nl.lunatech.movie.imgdb.common.helper;

import nl.lunatech.movie.imgdb.common.exception.AssertingException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

/**
 * @author alikhandani
 * @created 02/06/2020
 * @project lunatech
 */
@Component
public interface AssertHelper {

    /**
     * @param params contain reference of object name.
     *               If the object is not null, it will be thrown AssertingException and
     *               this argument will be used in exception's message.
     */
    void notNull(Object object, Map<String, Object> params) throws AssertingException;
    void notNull(Object... object) throws AssertingException;
    void notNull(String str) throws AssertingException;
    void notEmpty(Collection<?> collection) throws AssertingException;
    <T> void notEqual(T first, T second) throws AssertingException;
    void notBigger(int first, int second) throws AssertingException;

}
