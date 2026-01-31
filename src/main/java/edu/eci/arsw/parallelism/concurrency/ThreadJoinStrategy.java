package edu.eci.arsw.parallelism.concurrency;

import edu.eci.arsw.parallelism.api.PiCalculationException;
import edu.eci.arsw.parallelism.core.PiDigits;
import java.util.List;
import java.util.ArrayList;

public class ThreadJoinStrategy implements ParallelStrategy {
    
    // Decides whether or not to use sequential
    @Override
    public String calculate(int start, int count, int threads) {
        // Validation
        if (shouldUseSequential(count, threads)) {
            return PiDigits.getDigitsHex(start, count);
        }
        
        // Divides work 
        WorkSegment[] segments = divideWork(start, count, threads);
        
        String[] results = new String[threads];
        Thread[] workers = new Thread[threads];
        
        // Creates and starts threads
        for (int i = 0; i < threads; i++) {
            final int index = i;
            workers[i] = new Thread(() -> {
                WorkSegment segment = segments[index];
                results[index] = PiDigits.getDigitsHex(
                    segment.getStart(), 
                    segment.getCount()
                );
            });
            workers[i].start();
        }
        
        waitForAllThreads(workers);
        
        // Combines results
        return combineResults(results, count);
    }
    
    /**
     * Decides whether we should use sequential 
     */
    private boolean shouldUseSequential(int count, int threads) {
        // pocos hilos -> secuencial
        return threads <= 1;
    }
    
    /**
     * Divides the work in balanced segments
     */
    private WorkSegment[] divideWork(int start, int count, int threads) {
        WorkSegment[] segments = new WorkSegment[threads];
        int baseSize = count / threads;
        int extra = count % threads;
        int current = start;
        
        for (int i = 0; i < threads; i++) {
            int segmentSize = baseSize + (i < extra ? 1 : 0);
            segments[i] = new WorkSegment(current, segmentSize);
            current += segmentSize;
        }
        
        return segments;
    }
    
    /**
     * Waits for every thread to finish
     */
    private void waitForAllThreads(Thread[] threads) {
        List<Thread> failedThreads = new ArrayList<>();
        
        for (int i = 0; i < threads.length; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                failedThreads.add(threads[i]);
            }
        }
        
        // If some thread failed, throw exception
        if (!failedThreads.isEmpty()) {
            throw new RuntimeException(
                "Threads interrupted: " + failedThreads.size() + " threads failed");
        }
    }
    
    /**
     * Combines results from all threads into a single string and verifies
     */
    private String combineResults(String[] results, int expectedLength) {
        StringBuilder combined = new StringBuilder(expectedLength);
        int actualLength = 0;
        
        for (String result : results) {
            if (result == null) {
                throw new RuntimeException("A thread returned null result");
            }
            combined.append(result);
            actualLength += result.length();
        }
        
        // Verifies correct length(improvised checksum >:)   )
        if (actualLength != expectedLength) {
            throw new PiCalculationException(PiCalculationException.CALCULATION_ERROR);
        }
        
        return combined.toString();
    }
    
    @Override
    public String name() {
        return "threads";
    }
    
    /**
     * Represents a work segment for a thread
     */
    private static class WorkSegment {
        private final int start;
        private final int count;
        
        public WorkSegment(int start, int count) {
            this.start = start;
            this.count = count;
        }
        
        public int getStart() { return start; }
        public int getCount() { return count; }
    }
}