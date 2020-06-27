package gg.bayes.challenge.exception;

/**
 * The MatchException wraps all checked standard Java exception and enriches
 * them with a custom error code.
 * 
 * @author VineetPareek
 *
 */
public abstract class MatchException extends Throwable {

    /**
     * The Serial version ID
     */
    private static final long serialVersionUID = -4239569587169793789L;
    private String error;

    public MatchException(String error, String message, Throwable cause) {
        super(message, cause);
        this.error = error;
    }

    public MatchException(String error) {
        super(error);
        this.error = error;
    }

    public String getError() {
        return error;
    }

}
