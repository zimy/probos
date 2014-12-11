package me.zimy.probos;

import java.util.List;

/**
 * @author Dmitriy &lt;Zimy&gt; Yakovlev
 * @since 12/11/14.
 */
public class SecondResult {
    List<Integer> histogram;
    int q5, q95;

    public List<Integer> getHistogram() {
        return histogram;
    }

    public void setHistogram(List<Integer> histogram) {
        this.histogram = histogram;
    }

    public int getQ5() {
        return q5;
    }

    public void setQ5(int q5) {
        this.q5 = q5;
    }

    public int getQ95() {
        return q95;
    }

    public void setQ95(int q95) {
        this.q95 = q95;
    }
}
