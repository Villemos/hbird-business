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
		
		request.setClass("parameter");
		str = producer.createRequest(request);
		assertTrue(str.equals("class:parameter"));

		request.setFrom(1l);
		str = producer.createRequest(request);
		assertTrue(str.equals("class:parameter AND timestamp:[1 TO *]"));
		
		request.setTo(5l);
		str = producer.createRequest(request);
		assertTrue(str.equals("class:parameter AND timestamp:[1 TO 5]"));
		
		request.addName("Name1");
		str = producer.createRequest(request);
		assertTrue(str.equals("class:parameter AND (name:Name1) AND timestamp:[1 TO 5]"));

		request.addName("Name2");
		str = producer.createRequest(request);
		assertTrue(str.equals("class:parameter AND (name:Name1 OR name:Name2) AND timestamp:[1 TO 5]"));

		request.addName("Name3");
		str = producer.createRequest(request);
		assertTrue(str.equals("class:parameter AND (name:Name1 OR name:Name2 OR name:Name3) AND timestamp:[1 TO 5]"));

		request.setIsStateOf("TestCommand1");
		str = producer.createRequest(request);
		assertTrue(str.equals("class:parameter AND (name:Name1 OR name:Name2 OR name:Name3) AND isStateOf:TestCommand1 AND timestamp:[1 TO 5]"));

	}
	
	@Test
	public void testOrbitalStateRequest() {
		SolrProducer producer = new SolrProducer(new SolrEndpoint(""));
		
		OrbitalStateRequest request = new OrbitalStateRequest("testSatellite");
		
		String str = producer.createOrbitalStateRequest(request);
		assertTrue(str.equals("ofSatellite:testSatellite AND class:orbitalstate"));
		
		request.setFromTime(1l);
		str = producer.createOrbitalStateRequest(request);
		assertTrue(str.equals("ofSatellite:testSatellite AND class:orbitalstate AND timestamp:[1 TO *]"));

		request.setToTime(5l);
		str = producer.createOrbitalStateRequest(request);
		assertTrue(str.equals("ofSatellite:testSatellite AND class:orbitalstate AND timestamp:[1 TO 5]"));
		
		request = new OrbitalStateRequest("testSatellite", 2l, 6l);
		str = producer.createOrbitalStateRequest(request);
		assertTrue(str.equals("ofSatellite:testSatellite AND class:orbitalstate AND timestamp:[2 TO 6]"));

		request = new OrbitalStateRequest("testSatellite", 2l, null);
		str = producer.createOrbitalStateRequest(request);
		assertTrue(str.equals("ofSatellite:testSatellite AND class:orbitalstate AND timestamp:[2 TO *]"));

		request = new OrbitalStateRequest("testSatellite", null, 6l);
		str = producer.createOrbitalStateRequest(request);
		assertTrue(str.equals("ofSatellite:testSatellite AND class:orbitalstate AND timestamp:[* TO 6]"));
	}
	
	@Test
	public void testParameterRequest() {
		SolrProducer producer = new SolrProducer(new SolrEndpoint(""));
		
		ParameterRequest request = new ParameterRequest("testParameter", 1);
		
		String str = producer.createRequest(request);
		assertTrue(str.equals("class:parameter AND (name:testParameter)"));
		
		request = new ParameterRequest("testParameter", 2l, 6l);
		str = producer.createRequest(request);
		assertTrue(str.equals("class:parameter AND (name:testParameter) AND timestamp:[2 TO 6]"));

	}
}
