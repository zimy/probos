package me.zimy.probos.services;

import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;

/**
 * @author Dmitriy &lt;Zimy&gt; Yakovlev
 * @since 12/9/14.
 */
@Service
public class SortingCalculationSettings {
    Logger logger = LoggerFactory.getLogger(SortingCalculationSettings.class);
    @Autowired
    SortService sortService;

    @Async
    public Future<SecondResult> calculate() throws InterruptedException, ExecutionException {
        List<Pair<Future<Integer>, Future<Integer>>> pairs = new ArrayList<>(100);
        Queue<Future<Integer>> queue = new ConcurrentLinkedDeque<>();
        ExecutorService executorService = Executors.newCachedThreadPool();

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
        logger.info("Futures created");
        while (!queue.isEmpty()) {
            Thread.sleep(20);
            while ((!queue.isEmpty()) && queue.peek().isDone()) {
                queue.poll();
            }
        }

        DescriptiveStatistics forA = new DescriptiveStatistics();
        DescriptiveStatistics forB = new DescriptiveStatistics();
        List<Integer> as = new ArrayList<>(), bs = new ArrayList<>();
        for (Pair<Future<Integer>, Future<Integer>> pair : pairs) {
            Integer d1 = pair.getFirst().get();
            Integer d2 = pair.getSecond().get();
            forA.addValue(d1);
            forB.addValue(d2);
            as.add(d1);
            bs.add(d2);
        }

        Collections.sort(as);
        int min = as.get(0);
        int max = as.get(as.size() - 1) + 1;
        double step = (max - min) / (double) 7;
        List<Integer> parts = new ArrayList<>(7);

        for (int i = 0; i < 7; i++) {
            parts.add(0);
        }

        for (Integer a : as) {
            int position = (int) ((a - min) / step);
            parts.set(position, parts.get(position) + 1);
        }

        int K5 = 5;
        int K95 = 94;


        DescriptiveStatistics deltas = new DescriptiveStatistics();

        for (int i = 0; i < 100; i++) {
            deltas.addValue(as.get(i) - bs.get(i));
        }

        AbstractRealDistribution studen100 = new TDistribution(new MersenneTwister(), 99);

        double T = 10 * deltas.getMean() / deltas.getStandardDeviation();
        double Tcritical = studen100.inverseCumulativeProbability(1 - (1 - 0.95) / 2);


        SecondResult secondResult = new SecondResult();
        secondResult.setHistogram(parts);
        secondResult.setQ5(as.get(K5));
        secondResult.setQ95(as.get(K95));
        secondResult.setT(T);
        secondResult.setTCritical(Tcritical);
        return new AsyncResult<>(secondResult);
    }
}
