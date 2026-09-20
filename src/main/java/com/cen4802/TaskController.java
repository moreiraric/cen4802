package com.cen4802;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TaskController {

    private final List<Task> tasks = new ArrayList<>();

    public TaskController() {
        tasks.add(new Task("Finish Java assignment"));
        tasks.add(new Task("Study Git and GitHub"));
        tasks.add(new Task("Submit project"));
    }

    @GetMapping("/")
    public String showTasks(Model model) {
        model.addAttribute("tasks", tasks);
        model.addAttribute("taskCount", tasks.size());
        return "index";
    }

    @PostMapping("/add")
    public String addTask(@RequestParam String description) {
        if (description != null && !description.trim().isEmpty()) {
            tasks.add(new Task(description.trim()));
        }

        return "redirect:/";
    }

    @PostMapping("/done")
    public String completeTask(@RequestParam int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.remove(index);
        }

        return "redirect:/";
    }
}
