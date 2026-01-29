
package edu.eci.arsw.parallelism.core;

import edu.eci.arsw.parallelism.api.PiCalculationException;
import org.springframework.stereotype.Service;

@Service
public class PiDigitsService {

    public String calculateSequential(int start, int count) {
        // Validate both parameters first for combined error
        if (start < 0 && count < 1) {
            throw new PiCalculationException(PiCalculationException.INVALID_INTERVAL);
        }

        // Validate parameters individually using centralized error messages
        if (start < 0) {
            throw new PiCalculationException(PiCalculationException.INVALID_START_NEGATIVE);
        }
        if (count < 1) {
            throw new PiCalculationException(PiCalculationException.INVALID_COUNT_ZERO_OR_NEGATIVE);
        }

        return PiDigits.getDigitsHex(start, count);
    }
}
