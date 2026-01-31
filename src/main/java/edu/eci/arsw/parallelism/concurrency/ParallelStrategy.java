package edu.eci.arsw.parallelism.concurrency;

public interface ParallelStrategy {
    String calculate(int start, int count, int threads);
    String name();
}