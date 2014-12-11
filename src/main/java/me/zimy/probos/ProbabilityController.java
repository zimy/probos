package me.zimy.probos;

import me.zimy.probos.services.MagicService;
import me.zimy.probos.services.SecondResult;
import me.zimy.probos.services.SortingCalculationSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author Dmitriy &lt;Zimy&gt; Yakovlev
 * @since 12/9/14.
 */
@Controller
@RequestMapping("/")
public class ProbabilityController {

    @Autowired
    private MagicService magicService;

    @Autowired
    private SortingCalculationSettings sortingCalculationSettings;

    @RequestMapping("/")
    public String getProbabilityResults(Model model) throws InterruptedException, ExecutionException {
        Future<Integer> integerFuture = magicService.calculateNormalDistribution(400, 0.9, 1000, 0, 1);
        Future<Integer> integerFuture1 = magicService.calculateNormalDistribution(10, 0.9, 1000, 0, 1);
        Future<Integer> integerFuture2 = magicService.calculateMyDistribution(400, 0.9, 1000, 0.5, 3, -3);
        Future<Integer> integerFuture3 = magicService.calculateMyDistribution(10, 0.9, 1000, 0.5, 3, -3);
        Future<SecondResult> calculate = sortingCalculationSettings.calculate();
        while (!(integerFuture.isDone() && integerFuture2.isDone() && integerFuture1.isDone() && integerFuture3.isDone() && calculate.isDone())) {
            Thread.sleep(20);
        }

        model.addAttribute("my400", integerFuture2.get());
        model.addAttribute("my10", integerFuture3.get());
        model.addAttribute("n400", integerFuture.get());
        model.addAttribute("n10", integerFuture1.get());
        model.addAttribute("secondResult", calculate.get());
        return "answers";
    }
}
