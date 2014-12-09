package me.zimy.probos;

import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.interval.ConfidenceInterval;
import org.apache.commons.math3.util.FastMath;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

/**
 * Service that provides counting of situations when when confidence interval with confidence confidence is ok.
 *
 * @author Dmitriy &lt;Zimy&gt; Yakovlev
 * @since 12/8/14.
 */
@Service
public class MagicService {
    public static boolean checkInterval(ConfidenceInterval confidenceInterval, double checkingMean) {
        return checkingMean < confidenceInterval.getUpperBound() && checkingMean > confidenceInterval.getLowerBound();
    }

    @Async
    public Future<Integer> calculateMyDistribution(int n, double confidence, int iterations, double p, int success, int failure) {
        IntegerDistribution myDistribution = new BinomialDistribution(1, p);
        int counter = 0;
        for (int i = 0; i < iterations; i++) {
            DescriptiveStatistics statistics = new DescriptiveStatistics();
            for (int i1 = 0; i1 < n; i1++) {
                statistics.addValue(myDistribution.sample() == 1 ? success : failure);
            }
            ConfidenceInterval confidenceInterval = meanConfidence(statistics, confidence);
            if (checkInterval(confidenceInterval, success * p + failure * (1 - p))) {
                counter++;
            }
        }
        return new AsyncResult<>(counter);
    }

    @Async
    public Future<Integer> calculateNormalDistribution(int n, double confidence, int iterations, double m, double d) {
        RealDistribution normal = new NormalDistribution(new MersenneTwister(), m, d);
        int counter = 0;
        for (int i = 0; i < iterations; i++) {
            DescriptiveStatistics statistics = new DescriptiveStatistics(normal.sample(n));
            ConfidenceInterval confidenceInterval = meanConfidence(statistics, confidence);
            if (checkInterval(confidenceInterval, normal.getNumericalMean())) {
                counter++;
            }
        }
        return new AsyncResult<>(counter);
    }

    public ConfidenceInterval meanConfidence(DescriptiveStatistics statistics, double confidence) {
        RealDistribution student = new TDistribution(statistics.getN() - 1);
        double mean = statistics.getMean();
        double v = student.inverseCumulativeProbability(1 - (1 - confidence) / 2);
        double v1 = v * statistics.getStandardDeviation() / FastMath.sqrt(statistics.getN()) + 1E-6;
        return new ConfidenceInterval(mean - v1, mean + v1, confidence);
    }

}
