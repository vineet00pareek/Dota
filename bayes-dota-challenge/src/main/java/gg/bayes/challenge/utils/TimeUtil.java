package gg.bayes.challenge.utils;

import java.time.LocalTime;

/**
 * This class is used to provide Time related manipulation.
 * 
 * @author VineetPareek
 *
 */
public class TimeUtil {

    /**
     * It is converting string to LocalTime
     * 
     * @param token this is time stamp
     * @return {@link LocalTime}
     */
    public static Long getTimeStamp(String token) {
        token = token.substring(1, token.length() - 1);
        LocalTime localTime = LocalTime.parse(token);
        long time = localTime.toNanoOfDay() / 1000000;
        return time;
    }

}
