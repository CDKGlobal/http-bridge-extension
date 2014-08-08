package net.fawad.riemannbridge;
import com.singularity.ee.agent.systemagent.api.AManagedMonitor;
import com.singularity.ee.agent.systemagent.api.TaskExecutionContext;
import com.singularity.ee.agent.systemagent.api.TaskOutput;
import com.singularity.ee.agent.systemagent.api.exception.TaskExecutionException;
import org.apache.log4j.Logger;

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
        pool.execute(new RandomWriter(this));
        pool.execute(new CounterWriter(this));
        try {
            new CountDownLatch(1).await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new TaskOutput("Riemann Bridge task completed");
    }


}
