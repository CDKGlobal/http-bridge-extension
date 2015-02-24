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
    private final CountDownLatch latch;

    public HttpBridge() {
        this(new CountDownLatch(1));
    }

    public HttpBridge(CountDownLatch latch) {
        this.latch = latch;
    }
    public TaskOutput execute(Map<String, String> args, TaskExecutionContext taskExecutionContext) throws TaskExecutionException {
        logger.info("Starting HTTP Bridge...");
        final MetricsHelper metricsHelper = new MetricsHelper(this);
        try {
            new WebListener(metricsHelper, Integer.valueOf(args.get("port"))).start();
        } catch (Throwable e) {
            throw new TaskExecutionException("Could not start web server", e);
        }
        waitForever();
        return new TaskOutput("HTTP Bridge task completed");
    }

    private void waitForever() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
