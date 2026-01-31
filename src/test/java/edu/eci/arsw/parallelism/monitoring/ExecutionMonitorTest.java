package edu.eci.arsw.parallelism.monitoring;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExecutionMonitorTest {

    @Test
    void shouldMeasureExecutionTime() {
        ExecutionMonitor monitor = new ExecutionMonitor();

        ExecutionResult result = monitor.measure(() -> {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignored) {}
            return null;
        });

        assertTrue(result.getTimeNs() > 0);
    }

    @Test
    void shouldExecuteTheTaskExactlyOnce() {
        ExecutionMonitor monitor = new ExecutionMonitor();

        final int[] counter = {0};

        monitor.measure(() -> {
            counter[0]++;
            return null;
        });

        assertEquals(1, counter[0]);
    }
}
