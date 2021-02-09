package com.sagnik.asyncstopdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

@Service
public class TaskCoordinator {
    private Map<String, TaskInProgress> taskNameVsTaskInProgress;
    private Integer maxParallelism;

    @Autowired
    public TaskCoordinator(@Value("${maxParallelism}") Integer maxParallelism) {
        this.maxParallelism = maxParallelism;
        this.taskNameVsTaskInProgress = new HashMap<>();
    }

    public void start(String name) {
        if (taskNameVsTaskInProgress.size() >= maxParallelism) {
            throw new RuntimeException("Reached maximum parallelism limit");
        }

        final ExecutorService executorService = Executors.newFixedThreadPool(1);

        final TaskInProgress taskInProgress = this.taskNameVsTaskInProgress.get(name);
        if (taskInProgress != null) {
            throw new RuntimeException("There is already an active task with name " + name);
        }

        final RunnableTask runnableTask = new RunnableTask(name);
        final CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(runnableTask, executorService);
        this.taskNameVsTaskInProgress.put(name, new TaskInProgress(completableFuture, runnableTask));

        completableFuture.whenComplete((res, err) -> {
            executorService.shutdownNow();
            taskNameVsTaskInProgress.remove(name);

            if (err instanceof TimeoutException) {
                System.out.println("Forcefully aborted task " + name);
            }
        });
    }

    public void stop(String name, int timeout) {
        final TaskInProgress taskInProgress = this.taskNameVsTaskInProgress.get(name);
        if (taskInProgress == null) {
            throw new RuntimeException("There is no active task with name " + name);
        }

        taskInProgress.getRunnableTask().stop();
        taskInProgress.getCompletableFuture().orTimeout(timeout, TimeUnit.MILLISECONDS); // force stop if doesnt complete in given timeout
    }
}
