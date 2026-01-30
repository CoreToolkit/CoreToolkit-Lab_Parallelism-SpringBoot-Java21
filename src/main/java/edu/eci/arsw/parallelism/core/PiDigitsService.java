
package edu.eci.arsw.parallelism.core;

import edu.eci.arsw.parallelism.api.PiCalculationException;
import org.springframework.stereotype.Service;
import edu.eci.arsw.parallelism.concurrency.ParallelStrategy;
import edu.eci.arsw.parallelism.concurrency.ThreadJoinStrategy;
import edu.eci.arsw.parallelism.monitoring.ExecutionMonitor;
import edu.eci.arsw.parallelism.monitoring.ExecutionResult;

import java.util.LinkedHashMap;
import java.util.Map;



@Service
public class PiDigitsService {

    public String calculateSequential(int start, int count) {

        return PiDigits.getDigitsHex(start, count);
    }

    public String calculateParallel(int start, int count, Integer threads, String strategy) {

        if (threads < 1) {
            throw new PiCalculationException(PiCalculationException.INVALID_THREADS_LESS_THAN_ONE);
        }

        ParallelStrategy s = new ThreadJoinStrategy();

        return s.calculate(start, count, threads);
    }

    public Void Validate(int start, int count, Integer threads, String strategy) throws PiCalculationException {
        if (start < 0 && count < 1) {
            throw new PiCalculationException(PiCalculationException.INVALID_INTERVAL);
        }

        if (start < 0) {
            throw new PiCalculationException(PiCalculationException.INVALID_START_NEGATIVE);
        }
        if (count < 1) {
            throw new PiCalculationException(PiCalculationException.INVALID_COUNT_ZERO_OR_NEGATIVE);
        }

        return null;
    }

    public String calculate(int start, int count, Integer threads, String strategy) {
        Validate(start, count, threads, strategy);

        if (strategy == null || strategy.isBlank()) {
            return calculateSequential(start, count);
        }

        if (strategy.equals("threads")) {
            if (threads == null) {
                throw new PiCalculationException(PiCalculationException.MISSING_THREADS);
            }
            return calculateParallel(start, count, threads, strategy);
        } else {
            throw new PiCalculationException(PiCalculationException.INVALID_STRATEGY);
        }
    }


    public Map<String, Object> comparePerformance(int start, int count) {

        ExecutionMonitor monitor = new ExecutionMonitor();
        Map<String, Object> response = new LinkedHashMap<>();

        int cores = Runtime.getRuntime().availableProcessors();

        ExecutionResult seq = monitor.measure(() ->
                calculateSequential(start, count)
        );

        response.put("sequential_ms", seq.getTimeMs());

        int[] threadConfigs = {
                1,
                cores,
                cores * 2,
                200,
                500
        };

        Map<String, Long> parallelResults = new LinkedHashMap<>();

        for (int t : threadConfigs) {
            ExecutionResult par = monitor.measure(() ->
                    calculateParallel(start, count, t, "threads")
            );
            parallelResults.put("threads_" + t, par.getTimeMs());
        }

        response.put("parallel_ms", parallelResults);
        response.put("available_processors", cores);

        return response;
    }
}