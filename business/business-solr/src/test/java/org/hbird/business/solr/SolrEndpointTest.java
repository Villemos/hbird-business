package org.hbird.business.solr;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class SolrEndpointTest {

    @Test
    public void testDefaultSolrServerUrl() {
        SolrEndpoint endpoint = new SolrEndpoint();
        assertEquals("http://localhost:8080/apache-solr-3.5.0/", endpoint.getSolrServerUrl());
    }

    @Test
    public void testSolrServerUrlFromSystemProperties() {
        String url = "http://localhost:8383/apache-solr-3.5.1-beta/";
        System.setProperty("solr.url", url);
        SolrEndpoint endpoint = new SolrEndpoint();
        assertEquals(url, endpoint.getSolrServerUrl());
    }
}
