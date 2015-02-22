package net.fawad.httplistener.tests;

import com.singularity.ee.agent.systemagent.api.TaskExecutionContext;
import com.singularity.ee.agent.systemagent.api.exception.TaskExecutionException;
import net.fawad.httplistener.HttpBridge;
import org.junit.Assert;
import org.junit.Test;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.mockito.Mockito.mock;

public class HttpBridgeTests {
    @Test
    public void testExecute() throws TaskExecutionException, InterruptedException, IOException {
        final ExecutorService executorService = Executors.newFixedThreadPool(1);
        final HttpBridge httpBridge = new HttpBridge();

        final Future<Object> serverFuture = executorService.submit(new Callable<Object>() {
            public Object call() throws Exception {
                httpBridge.execute(mock(Map.class), mock(TaskExecutionContext.class));
                return null;
            }
        });
        Thread.sleep(5000); // Assuming server spins up in this time
        final Representation representation = new ClientResource("http://localhost:8888/hostmetrics/metric").post(new Form() {{
            this.set("name", "test");
            this.set("value", "123");
            this.set("aggregationtype", "AVERAGE");
            this.set("rolluptype", "AVERAGE");
            this.set("clusterrolluptype", "INDIVIDUAL");
        }});
        final boolean cancelledNow = serverFuture.cancel(true);
        Assert.assertTrue(cancelledNow);
    }
}
