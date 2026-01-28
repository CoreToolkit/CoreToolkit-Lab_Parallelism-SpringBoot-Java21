
package edu.eci.arsw.parallelism.core;

import org.springframework.stereotype.Service;

@Service
public class PiDigitsService {

    public String calculateSequential(int start, int count) {
        return PiDigits.getDigitsHex(start, count);
    }
}
