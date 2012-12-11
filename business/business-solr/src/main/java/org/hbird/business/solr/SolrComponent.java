package org.hbird.business.solr;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

/**
 * Represents the component that manages {@link SolrEndpoint}.
 */
public class SolrComponent extends DefaultComponent {

	
    /* (non-Javadoc)
     * @see org.apache.camel.impl.DefaultComponent#createEndpoint(java.lang.String, java.lang.String, java.util.Map)
     */
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        Endpoint endpoint = new SolrEndpoint(uri, this);
        setProperties(endpoint, parameters);
        return endpoint;
    }
}
