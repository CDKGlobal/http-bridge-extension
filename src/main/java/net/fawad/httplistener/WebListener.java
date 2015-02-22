package net.fawad.httplistener;

import org.apache.log4j.Logger;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;

public class WebListener extends Application {
    public static final String METRICS_HELPER_NAME = "metricshelper";
    private static final Logger logger = Logger.getLogger(WebListener.class);
    private static final int port = 8888;
    private final MetricsHelper helper;

    public WebListener(MetricsHelper helper) {
        this.helper = helper;
    }

    public void start() throws Exception {
        Component component = new Component();
        component.getServers().add(Protocol.HTTP, port);
        component.getDefaultHost().attach("/hostmetrics", new WebListenerApplication());
        component.start();
        logger.info("Restlet server started");
    }

    private class WebListenerApplication extends Application {
        @Override
        public synchronized Restlet createInboundRoot() {
            Router router = new Router(getContext());

            logger.info("Helper: " + helper);
            router.getContext().getAttributes().put(METRICS_HELPER_NAME, helper);
            router.attach("/metric", MetricResource.class);
            return router;
        }
    }
}
