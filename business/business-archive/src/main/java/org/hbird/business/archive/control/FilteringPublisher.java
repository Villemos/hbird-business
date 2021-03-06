package org.hbird.business.archive.control;

import org.apache.camel.Handler;
import org.hbird.business.api.IPublisher;
import org.hbird.exchange.core.EntityInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilteringPublisher implements IPublisher {

    private final Logger LOG = LoggerFactory.getLogger(FilteringPublisher.class);

    protected IPublisher delegate;
    protected Filter filter;

    public FilteringPublisher(IPublisher delegate, Filter filter) {
        this.delegate = delegate;
        this.filter = filter;
    }

    @Handler
    @Override
    public <T extends EntityInstance> T publish(T object) throws Exception {
        if (filter.passes(object)) {
            LOG.trace("{} passed the filter", object);
            return delegate.publish(object);
        }
        else {
            LOG.trace("{} didn't pass the filter", object);
            return object;
        }
    }

}
