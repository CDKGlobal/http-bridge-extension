package net.fawad.httplistener;

import com.singularity.ee.agent.systemagent.api.AManagedMonitor;
import com.singularity.ee.agent.systemagent.api.TaskExecutionContext;
import com.singularity.ee.agent.systemagent.api.TaskOutput;
import com.singularity.ee.agent.systemagent.api.exception.TaskExecutionException;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class HttpBridge extends AManagedMonitor {
    private static final Logger logger = Logger.getLogger(HttpBridge.class);

    public TaskOutput execute(Map<String, String> args, TaskExecutionContext taskExecutionContext) throws TaskExecutionException {
        logger.info("HTTP Bridge started");
        final MetricsHelper metricsHelper = new MetricsHelper(this);
        try {
            new WebListener(metricsHelper).start();
        } catch (Throwable e) {
            throw new TaskExecutionException("Could not start web server", e);
        }
        waitForever();
        return new TaskOutput("HTTP Bridge task completed");
    }

    private void waitForever() {
        try {
            new CountDownLatch(1).await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
