package nl.lunatech.movie.imgdb.common.exception;

import java.util.Map;

/**
 * @author alikhandani
 * @created 02/06/2020
 * @project lunatech
 */
public class AssertingException extends BaseRuntimeException {

    public AssertingException(String code) {
        super(code);
    }

    public AssertingException(String code, Map<String, Object> params) {
        super(code, params);
    }

}