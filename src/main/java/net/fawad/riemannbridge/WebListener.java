package net.fawad.riemannbridge;

import com.singularity.ee.agent.systemagent.api.MetricWriter;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebListener {
    private static final Logger logger = Logger.getLogger(WebListener.class);
    private static final int port = 8888;
    private final MetricsHelper helper;

    public WebListener(MetricsHelper helper){

        this.helper = helper;
    }
    public void start() throws IOException {
        logger.info("Starting web server on port " + port);
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/metrics", new MyHandler(helper));
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    private static class MetricInfo {
        private final String name;
        private final long value;
        private final String aggregationType;
        private final String rollupType;
        private final String clusterRollupType;

        public MetricInfo(String name, long value, String aggregationType, String rollupType, String clusterRollupType) {
            assert(name != null);
            assert(aggregationType != null);
            assert(rollupType != null);
            assert(clusterRollupType != null);
            this.name = name;
            this.value = value;
            this.aggregationType = aggregationType;
            this.rollupType = rollupType;
            this.clusterRollupType = clusterRollupType;
        }

        public String getName() {
            return name;
        }

        public long getValue() {
            return value;
        }

        public String getAggregationType() {
            return aggregationType;
        }

        public String getRollupType() {
            return rollupType;
        }

        public String getClusterRollupType() {
            return clusterRollupType;
        }

        @Override
        public String toString() {
            return "MetricInfo{" +
                    "name='" + name + '\'' +
                    ", value=" + value +
                    ", aggregationType='" + aggregationType + '\'' +
                    ", rollupType='" + rollupType + '\'' +
                    ", clusterRollupType='" + clusterRollupType + '\'' +
                    '}';
        }
    }

    private static class MyHandler implements HttpHandler {
        private final MetricsHelper helper;

        public MyHandler(MetricsHelper helper) {

            this.helper = helper;
        }

        public void handle(HttpExchange exchange) throws IOException {
            logger.debug("Handling request");
            if (exchange.getRequestMethod().equals("GET")) {
                InputStream is = exchange.getRequestBody();
                String response = processRequest(exchange.getRequestURI(), is);
                sendResponse(exchange, response, 200, null);
            } else {
                sendResponse(exchange, "Unknown HTTP method", 501, null);
            }

        }

        private void sendResponse(HttpExchange exchange, String response, int code, Map<String, String> headers) throws IOException {
            if (headers != null) {
                final Headers responseHeaders = exchange.getResponseHeaders();
                for (String key : headers.keySet()) {
                    responseHeaders.set(key, headers.get(key));
                }
            }
            exchange.sendResponseHeaders(code, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

        private String processRequest(URI requestUri, InputStream is) {
            final MetricInfo metricInfo = getMetricInfo(requestUri);
            helper.printMetric(metricInfo.getName(), metricInfo.getValue(),
                    metricInfo.getAggregationType(),
                    metricInfo.getRollupType(),
                    metricInfo.getClusterRollupType()
            );
            return "Received request to publish metric " + metricInfo;
        }

        private MetricInfo getMetricInfo(URI requestUri) {
            final List<NameValuePair> parametersList = URLEncodedUtils.parse(requestUri, Charset.defaultCharset().name());
            Map<String, String> parameters = new HashMap<String, String>();
            for (NameValuePair param : parametersList) {
                parameters.put(param.getName(), param.getValue());
            }
            return new MetricInfo(parameters.get("name"), Long.parseLong(parameters.get("value")), parameters.get("aggregationtype"), parameters.get("rolluptype"), parameters.get("clusterrolluptype"));
        }
    }


}
