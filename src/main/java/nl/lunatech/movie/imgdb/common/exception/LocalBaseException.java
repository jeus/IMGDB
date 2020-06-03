package nl.lunatech.movie.imgdb.common.exception;

import java.util.Map;

/**
 * @author alikhandani
 * @created 02/06/2020
 * @project lunatech
 */
public interface LocalBaseException {

    String getMessageCode();
    Map<String, Object> getParams();
}
