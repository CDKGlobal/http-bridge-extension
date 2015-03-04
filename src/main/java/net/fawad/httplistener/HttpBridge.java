package net.fawad.httplistener;

import com.singularity.ee.agent.systemagent.api.AManagedMonitor;
import com.singularity.ee.agent.systemagent.api.MetricWriter;
import com.singularity.ee.agent.systemagent.api.TaskExecutionContext;
import com.singularity.ee.agent.systemagent.api.TaskOutput;
import com.singularity.ee.agent.systemagent.api.exception.TaskExecutionException;
import org.apache.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class HttpBridge extends AManagedMonitor {
    private static final Logger logger = Logger.getLogger(HttpBridge.class);
    private final CountDownLatch latch;
    private static final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);

    public HttpBridge() {
        this(new CountDownLatch(1));
    }

    public HttpBridge(CountDownLatch latch) {
        this.latch = latch;
    }

    public TaskOutput execute(Map<String, String> args, TaskExecutionContext taskExecutionContext) throws TaskExecutionException {
        logger.info("Starting HTTP Bridge...");
        String hostName;
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            hostName = "UnknownHostName";
        }
        new Heartbeat(scheduledExecutorService, this.getMetricWriter("Custom Metrics|HttpBridge|" + hostName + "|running",
                MetricWriter.METRIC_AGGREGATION_TYPE_OBSERVATION,
                MetricWriter.METRIC_TIME_ROLLUP_TYPE_CURRENT,
                MetricWriter.METRIC_CLUSTER_ROLLUP_TYPE_INDIVIDUAL
        ), 30).start();
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
