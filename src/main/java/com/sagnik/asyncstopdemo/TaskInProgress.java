package com.sagnik.asyncstopdemo;

import java.util.concurrent.CompletableFuture;

public class TaskInProgress {
    private final CompletableFuture<Void> completableFuture;
    private final RunnableTask runnableTask;

    public TaskInProgress(CompletableFuture<Void> completableFuture, RunnableTask runnableTask) {
        this.completableFuture = completableFuture;
        this.runnableTask = runnableTask;
    }

    public CompletableFuture<Void> getCompletableFuture() {
        return completableFuture;
    }

    public RunnableTask getRunnableTask() {
        return runnableTask;
    }
}
