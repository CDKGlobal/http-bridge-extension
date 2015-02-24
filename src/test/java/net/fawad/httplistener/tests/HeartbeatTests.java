package net.fawad.httplistener.tests;

import com.singularity.ee.agent.systemagent.api.MetricWriter;
import net.fawad.httplistener.Heartbeat;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class HeartbeatTests {
    @Test
    public void testStart() {
        final MetricWriter metricWriter = mock(MetricWriter.class);
        final Heartbeat heartbeat = new Heartbeat(mock(ScheduledExecutorService.class),metricWriter, 0);
        heartbeat.execute();
        verify(metricWriter, times(1)).printMetric(any(String.class));
    }
    @Test
    public void testSchedule() {
        final MetricWriter metricWriter = mock(MetricWriter.class);
        final ScheduledExecutorService scheduledExecutorService = mock(ScheduledExecutorService.class);
        final Heartbeat heartbeat = new Heartbeat(scheduledExecutorService, metricWriter, 2);
        final ArgumentCaptor<Runnable> runnableArgumentCaptor = ArgumentCaptor.forClass(Runnable.class);
        heartbeat.start();
        verify(scheduledExecutorService, times(1)).scheduleAtFixedRate(runnableArgumentCaptor.capture(), eq(0l), eq(2l), eq(TimeUnit.SECONDS));
        runnableArgumentCaptor.getValue().run();
        verify(metricWriter, times(1)).printMetric(any(String.class));
    }
}
