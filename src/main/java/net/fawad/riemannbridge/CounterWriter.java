package net.fawad.riemannbridge;

import com.singularity.ee.agent.systemagent.api.AManagedMonitor;
import com.singularity.ee.agent.systemagent.api.MetricWriter;
import org.apache.log4j.Logger;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CounterWriter implements Runnable {
    private volatile int counter = 0;
    private final AManagedMonitor monitor;
    private static final Logger logger = Logger.getLogger(CounterWriter.class);
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public CounterWriter(AManagedMonitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        logger.info("Random Writer started");
        final Runnable writer = new Runnable() {
            public void run() {
                printMetric("counter", counter++,
                        MetricWriter.METRIC_AGGREGATION_TYPE_AVERAGE,
                        MetricWriter.METRIC_TIME_ROLLUP_TYPE_AVERAGE,
                        MetricWriter.METRIC_CLUSTER_ROLLUP_TYPE_COLLECTIVE
                );
            }
        };
        scheduler.scheduleAtFixedRate(writer, 0, 1, TimeUnit.SECONDS);
    }

    private void printMetric(String metricName, long metricValue, String aggregation, String timeRollup, String cluster) {
        MetricWriter metricWriter = monitor.getMetricWriter("Custom Metrics|Testing|" + metricName,
                aggregation,
                timeRollup,
                cluster
        );
        String value = Long.toString(metricValue);
        if (logger.isDebugEnabled()) {
            logger.debug("Sending [" + aggregation + "/" + timeRollup + "/" + cluster
                    + "] metric = " + metricName + " = " + value);
        }
        metricWriter.printMetric(value);
    }
}
