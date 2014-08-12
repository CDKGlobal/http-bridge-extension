package net.fawad.riemannbridge;

import org.apache.log4j.Logger;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

public class MetricResource extends ServerResource {
    private static final Logger logger = Logger.getLogger(MetricResource.class);
    private MetricsHelper helper;

    @Override
    public void doInit() throws ResourceException {
        this.helper = (MetricsHelper) getContext().getAttributes().get(WebListener.METRICS_HELPER_NAME);
    }

    @Post
    public Representation create(Representation entity) throws ResourceException {
        logger.debug("Handling POST" + entity);
        final Form form = new Form(entity);
        final MetricInfo metricInfo = new MetricInfo(form.getFirstValue("name"), Long.parseLong(form.getFirstValue("value")), form.getFirstValue("aggregationtype"), form.getFirstValue("rolluptype"), form.getFirstValue("clusterrolluptype"));
        helper.printMetric(metricInfo.getName(), metricInfo.getValue(),
                metricInfo.getAggregationType(),
                metricInfo.getRollupType(),
                metricInfo.getClusterRollupType()
        );
        return new StringRepresentation("Update accepted " + metricInfo, MediaType.TEXT_PLAIN);
    }
}