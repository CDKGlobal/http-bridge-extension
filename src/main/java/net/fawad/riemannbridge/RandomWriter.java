package net.fawad.riemannbridge;

import com.singularity.ee.agent.systemagent.api.AManagedMonitor;
import com.singularity.ee.agent.systemagent.api.MetricWriter;
import org.apache.log4j.Logger;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RandomWriter implements Runnable {
    private static final Random random = new Random();
    private final int frequencyInSeconds;
    private final MetricsHelper helper;
    private static final Logger logger = Logger.getLogger(RandomWriter.class);
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public RandomWriter(int frequencyInSeconds, MetricsHelper helper) {
        this.frequencyInSeconds = frequencyInSeconds;
        this.helper = helper;
    }

    @Override
    public void run() {
        logger.info("Random Writer started");
        final Runnable randomWriter = new Runnable() {
            public void run() {
                helper.printMetric("Custom Metric|Testing|" + "random", random.nextInt(100),
                        MetricWriter.METRIC_AGGREGATION_TYPE_AVERAGE,
                        MetricWriter.METRIC_TIME_ROLLUP_TYPE_AVERAGE,
                        MetricWriter.METRIC_CLUSTER_ROLLUP_TYPE_INDIVIDUAL
                );
            }
        };
        scheduler.scheduleAtFixedRate(randomWriter, 0, frequencyInSeconds, TimeUnit.SECONDS);
    }


}
