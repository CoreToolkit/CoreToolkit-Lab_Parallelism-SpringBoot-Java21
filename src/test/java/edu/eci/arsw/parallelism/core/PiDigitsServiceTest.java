package edu.eci.arsw.parallelism.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.TimeUnit;

class PiDigitsServiceTest {

    private final PiDigitsService service = new PiDigitsService();

    @Test
    void testCalculateSequential() {
        String result = service.calculateSequential(0, 5);
        assertNotNull(result);
        assertEquals(5, result.length());
        assertTrue(result.matches("[0-9A-F]+"));
    }

    @Test
    void testCalculateSequentialLargeCount() {
        String result = service.calculateSequential(0, 100);
        assertNotNull(result);
        assertEquals(100, result.length());
        assertTrue(result.matches("[0-9A-F]+"));
    }

    @Test
    void testCalculateParallel() {
        String result = service.calculateParallel(0, 100, 4, "threads");
        assertNotNull(result);
        assertEquals(100, result.length());
        assertTrue(result.matches("[0-9A-F]+"));
    }

    @Test
    void testCalculateParallelEqualsSequential() {
        String result = service.calculateParallel(0, 100, 4, "threads");
        assertNotNull(result);
        assertEquals(100, result.length());
        assertTrue(result.matches("[0-9A-F]+"));
        assertEquals(service.calculateSequential(0, 100), result);
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testCalculateParallelDoesNotDeadlock() {
        String result = service.calculateParallel(0, 1000, 100, "threads");
        assertNotNull(result);
    }

    @Test
    void testMultipleParallelExecutionsProduceSameResult() {
        String expected = service.calculateSequential(0, 100);

        for (int threads : new int[] { 2, 4, 8, 16, 100 }) {
            String result = service.calculateParallel(0, 100, threads, "threads");
            assertEquals(expected, result,
                    "Failed with " + threads + " threads");
        }
    }

    @Test
    void comparePerformanceShouldReturnValidStructure() {
        Map<String, Object> result = service.comparePerformance(0, 1000);

        assertTrue(result.containsKey("sequential_ms"));
        assertTrue(result.containsKey("parallel_ms"));
        assertTrue(result.containsKey("available_processors"));

        assertTrue(result.get("sequential_ms") instanceof Long);
        assertTrue(result.get("parallel_ms") instanceof Map);
    }

    @Test
    void parallelResultsShouldContainThreadConfigurations() {
        Map<String, Object> result = service.comparePerformance(0, 1000);
        Map<String, Long> parallel =
                (Map<String, Long>) result.get("parallel_ms");

        assertTrue(parallel.containsKey("threads_1"));
        assertTrue(parallel.containsKey("threads_200"));
        assertTrue(parallel.containsKey("threads_500"));
    }
}