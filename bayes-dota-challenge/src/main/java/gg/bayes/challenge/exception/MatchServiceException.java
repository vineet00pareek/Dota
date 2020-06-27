package gg.bayes.challenge.exception;

/***
 * The MatchServiceException wraps all checked standard Java exception and
 * enriches them with a custom error code. You can use this code to retrieve
 * match error messages.
 * 
 * @author VineetPareek
 *
 */
public class MatchServiceException extends MatchException {

    /**
     * 
     */
    private static final long serialVersionUID = -3623188934070852246L;

    public MatchServiceException(String reason, String message, Throwable cause) {
        super(reason, message, cause);
    }

    public MatchServiceException(String message) {
        super(message);
    }

}
