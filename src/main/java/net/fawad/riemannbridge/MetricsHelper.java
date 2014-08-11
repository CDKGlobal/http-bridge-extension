package net.fawad.riemannbridge;

import com.singularity.ee.agent.systemagent.api.AManagedMonitor;
import com.singularity.ee.agent.systemagent.api.MetricWriter;
import org.apache.log4j.Logger;

public class MetricsHelper {
    private final AManagedMonitor monitor;
    private static final Logger logger = Logger.getLogger(RandomWriter.class);

    public MetricsHelper(AManagedMonitor monitor){

        this.monitor = monitor;
    }
    public void printMetric(String metricName, long metricValue, String aggregation, String timeRollup, String cluster) {
        MetricWriter metricWriter = monitor.getMetricWriter(metricName,
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
