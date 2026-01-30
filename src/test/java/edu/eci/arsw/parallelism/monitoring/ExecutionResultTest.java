package edu.eci.arsw.parallelism.monitoring;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ExecutionResultTest {

    @Test
    void shouldReturnTimeInNanoseconds() {
        ExecutionResult result = new ExecutionResult(1_500_000);
        assertEquals(1_500_000, result.getTimeNs());
    }

    @Test
    void shouldConvertNanosecondsToMilliseconds() {
        ExecutionResult result = new ExecutionResult(2_000_000);
        assertEquals(2, result.getTimeMs());
    }

    @Test
    void millisecondsShouldBeZeroIfLessThanOneMs() {
        ExecutionResult result = new ExecutionResult(500_000);
        assertEquals(0, result.getTimeMs());
    }
}
