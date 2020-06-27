package gg.bayes.challenge.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import gg.bayes.challenge.exception.MatchException;

/**
 * Util that creates {@link ResponseEntity} objects.
 * 
 * @author VineetPareek
 *
 */
@Component
public class ResponseUtils {

    /**
     * Create a new {@link ResponseEntity} with the given entity.
     * 
     * @param entity the response entity
     * @return {@link ResponseEntity}
     */
    public ResponseEntity<Object> buildOk(Object entity) {
        return ResponseEntity.ok(entity);
    }

    /**
     * Create a new {@link ResponseEntity}, with default
     * {@link ResponseEntity.HttpStatus#INTERNAL_SERVER_ERROR} status.
     * 
     * @param exception {@link GenericException}
     * @return {@link ResponseEntity}
     */
    public ResponseEntity<Object> build(MatchException exception) {
        gg.bayes.challenge.exception.Error error = new gg.bayes.challenge.exception.Error();
        error.setError(exception.getError() != null ? exception.getError() : HttpStatus.NOT_FOUND.name());
        error.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setMessage(exception.getMessage());
        return ResponseEntity.status(error.getStatusCode()).body(error);
    }
}