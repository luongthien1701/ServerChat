package com.example.demo.worker;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class DBWorker {
    private final BlockingQueue<DbTask> queue = new LinkedBlockingQueue<>();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @PostConstruct
    public void start() {
        executorService.submit(() -> {
            while (true) {
                try {
                    DbTask task = queue.take();
                    task.execute();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

    public void submit(DbTask task) {
        queue.offer(task);
    }
}
