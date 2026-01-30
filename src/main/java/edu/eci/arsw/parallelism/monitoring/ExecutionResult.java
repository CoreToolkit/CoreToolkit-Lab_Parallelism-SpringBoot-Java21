package edu.eci.arsw.parallelism.monitoring;

public class ExecutionResult {

    private final long timeNs;

    public ExecutionResult(long timeNs) {
        this.timeNs = timeNs;
    }

    public long getTimeNs() {
        return timeNs;
    }

    public long getTimeMs() {
        return timeNs / 1_000_000;
    }
}
