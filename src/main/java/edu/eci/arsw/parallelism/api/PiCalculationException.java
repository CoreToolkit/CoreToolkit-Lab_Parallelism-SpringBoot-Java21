
package edu.eci.arsw.parallelism.api;

/**
 * Custom exception for Pi calculation API errors.
 * Contains centralized error messages as constants.
 */
public class PiCalculationException extends RuntimeException {

    public static final String INVALID_START_NEGATIVE = "Parameter 'start' must be greater than or equal to 0";
    public static final String INVALID_COUNT_ZERO_OR_NEGATIVE = "Parameter 'count' must be at least 1";
    public static final String INVALID_PARAMETER_TYPE = "Invalid parameter type. Expected a number";
    public static final String INVALID_INTERVAL = "Invalid interval: start and count must be non-negative";

    /**
     * Constructor for PiCalculationException
     * 
     * @param message Error message (use constants defined above)
     */
    public PiCalculationException(String message) {
        super(message);
    }
}
