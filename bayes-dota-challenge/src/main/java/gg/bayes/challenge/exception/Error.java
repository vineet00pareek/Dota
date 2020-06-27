package gg.bayes.challenge.exception;

import lombok.Data;

/**
 * It's a Error class to contain error message and code.
 * 
 * @author VineetPareek
 *
 */
@Data
public class Error {

    /**
     * It is store status code for rest services.
     */
    private Integer statusCode;

    /**
     * It is store for error.
     */
    private String error;

    /**
     * It is store for error message.
     */
    private String message;

}
