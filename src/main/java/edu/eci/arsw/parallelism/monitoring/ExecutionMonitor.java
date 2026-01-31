package edu.eci.arsw.parallelism.monitoring;

import java.util.function.Supplier;

public class ExecutionMonitor {

    public ExecutionResult measure(Supplier<?> task) {
        long start = System.nanoTime();
        task.get();
        long end = System.nanoTime();
        return new ExecutionResult(end - start);
    }
}
