package nl.lunatech.movie.imgdb.common.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * @author alikhandani
 * @created 02/06/2020
 * @project lunatech
 */
public abstract class BaseRuntimeException extends RuntimeException implements LocalBaseException {

    private final String messageCode;
    private final Map<String, Object> params = new HashMap<>();

    public BaseRuntimeException(String messageCode) {
        super(messageCode);
        this.messageCode = messageCode;
    }

    public BaseRuntimeException(String messageCode, Map<String, Object> params) {
        super(messageCode);
        this.messageCode = messageCode;
        this.params.putAll(params);
    }

    @Override
    public String getMessageCode() {
        return messageCode;
    }

    @Override
    public Map<String, Object> getParams() {
        return params;
    }
}