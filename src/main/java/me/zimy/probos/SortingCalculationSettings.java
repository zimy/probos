package me.zimy.probos;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * @author Dmitriy &lt;Zimy&gt; Yakovlev
 * @since 12/9/14.
 */
@Service
public class SortingCalculationSettings {
    @Autowired
    SortService sortService;

    @Async
    public Future<List<Pair<Integer, Integer>>> calculate() throws InterruptedException, ExecutionException {
        List<Pair<Future<Integer>, Future<Integer>>> pairs = new ArrayList<>(100);
        Queue<Future<Integer>> queue = new ConcurrentLinkedDeque<>();
        ExecutorService executorService = Executors.newCachedThreadPool();
        List<Pair<Integer, Integer>> result = new ArrayList<>(100);
        final UniformRealDistribution distribution = new UniformRealDistribution(new MersenneTwister(), 0, 1);
        for (int i = 0; i < 100; i++) {
            final double[] sample = distribution.sample(20);
            final double[] copyOf = Arrays.copyOf(sample, sample.length);
            Future<Integer> futureSelection = executorService.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    return sortService.selectionSort(sample);
                }
            });
            Future<Integer> futureInsertion = executorService.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    return sortService.insertionSort(copyOf);
                }
            });
            queue.add(futureInsertion);
            queue.add(futureSelection);
            pairs.add(Pair.create(futureInsertion, futureSelection));
        }
        while (!queue.isEmpty()) {
            Thread.sleep(20);
            while ((!queue.isEmpty()) && queue.peek().isDone()) {
                queue.poll();
            }
        }
        for (Pair<Future<Integer>, Future<Integer>> pair : pairs) {
            result.add(Pair.create(pair.getFirst().get(), pair.getSecond().get()));
        }
        return new AsyncResult<>(result);
    }
}
