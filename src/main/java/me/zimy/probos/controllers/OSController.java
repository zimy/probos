package me.zimy.probos.controllers;

import me.zimy.probos.LRU;
import org.apache.commons.math3.distribution.UniformIntegerDistribution;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.util.Pair;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Dmitriy &lt;Zimy&gt; Yakovlev
 * @since 12/16/14.
 */
//@Controller
//@RequestMapping("/os")
public class OSController {
    @RequestMapping(method = RequestMethod.GET)
    public String getOsExample(Model model,
                               @RequestParam(required = false, defaultValue = "16") int pages,
                               @RequestParam(defaultValue = "32", required = false) int cycles,
                               @RequestParam(defaultValue = "6", required = false) int cache) {
        LRU lru = new LRU(cache);
        List<Pair<? extends List<Integer>, Pair<Integer, Integer>>> result = new ArrayList<>();
        int counter = 0;
        for (int page : new UniformIntegerDistribution(new MersenneTwister(), 0, pages - 1).sample(cycles)) {
            if (lru.put(page) != -1) {
                counter++;
            }
            result.add(new Pair<>(new ArrayList<>(Arrays.asList(lru.getCache())), new Pair<>(page, counter)));
        }
        model.addAttribute("caches", cache);
        model.addAttribute("simulationResult", result);
        return "os";
    }

    @RequestMapping(value = "/help", method = RequestMethod.GET)
    public String help() {
        return "help";
    }
}
