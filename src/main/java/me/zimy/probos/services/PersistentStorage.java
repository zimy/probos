package me.zimy.probos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * This is storage for making statistics FAST.
 * It if firstly filled on startup time, and then is re-generated on every http-request.
 *
 * @author Dmitriy &lt;Zimy&gt; Yakovlev
 * @since 12/11/14.
 */
@Service
public class PersistentStorage {
    Integer m400 = 0, m10 = 0, n400 = 0, n10 = 0;
    volatile SecondResult secondResult = new SecondResult();
    @Autowired
    private MagicService magicService;
    @Autowired
    private SortingCalculationSettings sortingCalculationSettings;

    @Async
    public void recalculate() throws ExecutionException, InterruptedException {
        Future<Integer> fn400 = magicService.calculateNormalDistribution(400, 0.9, 1000, 0, 1);
        Future<Integer> fn10 = magicService.calculateNormalDistribution(10, 0.9, 1000, 0, 1);
        Future<Integer> fm400 = magicService.calculateMyDistribution(400, 0.9, 1000, 0.5, 3, -3);
        Future<Integer> fm10 = magicService.calculateMyDistribution(10, 0.9, 1000, 0.5, 3, -3);
        Future<SecondResult> stats = sortingCalculationSettings.calculate();
        while (!(fn400.isDone() && fm400.isDone() && fn10.isDone() && fm10.isDone() && stats.isDone())) {
            Thread.sleep(20);
        }
        m400 = fm400.get();
        n400 = fn400.get();
        m10 = fm10.get();
        n10 = fn10.get();
        secondResult = stats.get();
    }

    public Integer getM400() {
        return m400;
    }

    public Integer getM10() {
        return m10;
    }

    public Integer getN400() {
        return n400;
    }

    public Integer getN10() {
        return n10;
    }

    public SecondResult getSecondResult() {
        return secondResult;
    }

    @PostConstruct
    public void postConstruct() {
        try {
            recalculate();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
