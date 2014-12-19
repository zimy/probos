package me.zimy.probos.controllers;

import me.zimy.probos.services.PersistentStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.concurrent.ExecutionException;

/**
 * Main and only HTTP controller
 *
 * @author Dmitriy &lt;Zimy&gt; Yakovlev
 * @since 12/9/14.
 */
@Controller
@RequestMapping("/")
public class ProbabilityController {

    @Autowired
    PersistentStorage persistentStorage;

    @RequestMapping("/")
    public String getProbabilityResults(Model model) throws InterruptedException, ExecutionException {
        model.addAttribute("my400", persistentStorage.getM400());
        model.addAttribute("my10", persistentStorage.getM10());
        model.addAttribute("n400", persistentStorage.getN400());
        model.addAttribute("n10", persistentStorage.getN10());
        model.addAttribute("secondResult", persistentStorage.getSortingResults());
        persistentStorage.recalculate();
        return "answers";
    }
}
