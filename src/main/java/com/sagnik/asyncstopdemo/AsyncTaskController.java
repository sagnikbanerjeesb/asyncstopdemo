package com.sagnik.asyncstopdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AsyncTaskController {
    @Autowired
    private TaskCoordinator taskCoordinator;

    @GetMapping("/start/{name}")
    public String start(@PathVariable String name) {
        try {
            taskCoordinator.start(name);
            return "Started";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping("/stop/{name}")
    public String stop(@PathVariable String name, @RequestParam(value = "timeout", defaultValue = "2000") Integer timeout) {
        try {
            taskCoordinator.stop(name, timeout);
            return "Stop requested";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
