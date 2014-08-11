package net.fawad.riemannbridge;

import com.singularity.ee.agent.systemagent.api.AManagedMonitor;
import com.singularity.ee.agent.systemagent.api.TaskExecutionContext;
import com.singularity.ee.agent.systemagent.api.TaskOutput;
import com.singularity.ee.agent.systemagent.api.exception.TaskExecutionException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RiemannBridge extends AManagedMonitor {
    private static final Logger logger = Logger.getLogger(RiemannBridge.class);
    private final ExecutorService pool = Executors.newFixedThreadPool(10);

    @Override
    public TaskOutput execute(Map<String, String> args, TaskExecutionContext taskExecutionContext) throws TaskExecutionException {
        logger.info("Riemann Bridge started");
        final MetricsHelper metricsHelper = new MetricsHelper(this);
        try {
            new WebListener(metricsHelper).start();
        } catch (IOException e) {
            throw new TaskExecutionException("Could not start web server", e);
        }
        pool.execute(new RandomWriter(10, metricsHelper));
        pool.execute(new CounterWriter(10, metricsHelper));
        waitForever();
        return new TaskOutput("Riemann Bridge task completed");
    }

    private void waitForever() {
        try {
            new CountDownLatch(1).await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
