package org.hbird.business.solr;

import static org.junit.Assert.*;

import org.hbird.exchange.dataaccess.DataRequest;
import org.hbird.exchange.dataaccess.OrbitalStateRequest;
import org.hbird.exchange.dataaccess.ParameterRequest;
import org.junit.Test;

public class SolrProducerTest {

	@Test
	public void testDataRequest() {
		SolrProducer producer = new SolrProducer(new SolrEndpoint(""));
		
		DataRequest request = new DataRequest("testIssuedBy", "testDestination", "testRetrieval", "A test retrieval request");
		
		String str = producer.createRequest(request);
		
		request.setClass("testclazz");
		str = producer.createRequest(request);
		assertTrue(str.equals("class:testclazz"));

		request.setFrom(1l);
		str = producer.createRequest(request);
		assertTrue(str.equals("class:testclazz AND timestamp:[1 TO *]"));
		
		request.setTo(5l);
		str = producer.createRequest(request);
		assertTrue(str.equals("class:testclazz AND timestamp:[1 TO 5]"));
		
		request.addName("Name1");
		str = producer.createRequest(request);
		assertTrue(str.equals("class:testclazz AND (name:Name1) AND timestamp:[1 TO 5]"));

		request.addName("Name2");
		str = producer.createRequest(request);
		assertTrue(str.equals("class:testclazz AND (name:Name1 OR name:Name2) AND timestamp:[1 TO 5]"));

		request.addName("Name3");
		str = producer.createRequest(request);
		assertTrue(str.equals("class:testclazz AND (name:Name1 OR name:Name2 OR name:Name3) AND timestamp:[1 TO 5]"));

		request.setIsStateOf("TestCommand1");
		str = producer.createRequest(request);
		assertTrue(str.equals("class:testclazz AND (name:Name1 OR name:Name2 OR name:Name3 OR isStateOf:TestCommand1) AND timestamp:[1 TO 5]"));

	}
	
	@Test
	public void testParameterRequest() {
		SolrProducer producer = new SolrProducer(new SolrEndpoint(""));
		
		ParameterRequest request = new ParameterRequest("testParameter", 1);
		
		String str = producer.createRequest(request);
		assertTrue(str.equals("class:Parameter AND (name:testParameter)"));
		
		request = new ParameterRequest("testParameter", 2l, 6l);
		str = producer.createRequest(request);
		assertTrue(str.equals("class:Parameter AND (name:testParameter) AND timestamp:[2 TO 6]"));

	}
}
