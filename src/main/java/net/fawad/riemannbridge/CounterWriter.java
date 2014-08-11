package net.fawad.riemannbridge;

import com.singularity.ee.agent.systemagent.api.MetricWriter;
import org.apache.log4j.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CounterWriter implements Runnable {
    private final MetricsHelper helper;
    private volatile int counter = 0;
    private final int frequencyInSeconds;
    private static final Logger logger = Logger.getLogger(CounterWriter.class);
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public CounterWriter(int frequencyInSeconds, MetricsHelper helper) {
        this.helper = helper;
        this.frequencyInSeconds = frequencyInSeconds;
    }

    @Override
    public void run() {
        logger.info("Random Writer started");
        final Runnable writer = new Runnable() {
            public void run() {
                helper.printMetric("Custom Metrics|Testing|counter", counter++,
                        MetricWriter.METRIC_AGGREGATION_TYPE_AVERAGE,
                        MetricWriter.METRIC_TIME_ROLLUP_TYPE_AVERAGE,
                        MetricWriter.METRIC_CLUSTER_ROLLUP_TYPE_COLLECTIVE
                );
            }
        };
        scheduler.scheduleAtFixedRate(writer, 0, frequencyInSeconds, TimeUnit.SECONDS);
    }
}
