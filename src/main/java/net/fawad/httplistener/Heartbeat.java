package net.fawad.httplistener;

import com.singularity.ee.agent.systemagent.api.MetricWriter;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Heartbeat{
    private final ScheduledExecutorService scheduledExecutorService;
    private final MetricWriter writer;
    private final long frequencySeconds;

    public Heartbeat(ScheduledExecutorService scheduledExecutorService, MetricWriter writer, long frequencySeconds) {
        this.scheduledExecutorService = scheduledExecutorService;
        this.writer = writer;
        this.frequencySeconds = frequencySeconds;
    }

    public void execute() {
        writer.printMetric("1");
    }

    public void start() {
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                execute();
            }
        }, 0, frequencySeconds, TimeUnit.SECONDS);
    }
}
