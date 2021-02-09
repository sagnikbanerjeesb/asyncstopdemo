package com.sagnik.asyncstopdemo;

import java.util.concurrent.atomic.AtomicBoolean;

public class RunnableTask implements Runnable {
    private final String name;
    private final AtomicBoolean requestedToStop;

    public RunnableTask(String name) {
        this.name = name;
        this.requestedToStop = new AtomicBoolean(false);
    }

    public void stop() {
        this.requestedToStop.set(true);
    }

    @Override
    public void run() {
        while (!requestedToStop.get()) {
            System.out.println("Executing task " + this.name);
            try {
                Thread.sleep(3000);
            } catch (Exception e) {}
        }
        System.out.println("Gracefully stopped task " + this.name);
    }
}
