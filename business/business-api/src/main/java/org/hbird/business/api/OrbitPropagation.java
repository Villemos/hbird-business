package org.hbird.business.api;

import java.util.List;

import org.hbird.exchange.core.DataSet;
import org.hbird.exchange.navigation.OrbitPredictionRequest;
import org.hbird.exchange.navigation.TlePropagationRequest;

public class OrbitPropagation extends HbirdApi implements IOrbitPrediction {

	public OrbitPropagation(String issuedBy) {
		super(issuedBy);
	}

	public DataSet requestOrbitPropagation(String satellite) {
		TlePropagationRequest request = new TlePropagationRequest(issuedBy, satellite);
		return sendRequest(request);
	}

	public DataSet requestOrbitPropagation(String satellite, long from, long to) {
		TlePropagationRequest request = new TlePropagationRequest(issuedBy, satellite, from, to);
		return sendRequest(request);
	}

	public DataSet requestOrbitPropagation(String satellite, String location, long from, long to) {
		TlePropagationRequest request = new TlePropagationRequest(issuedBy, satellite, location, from, to);
		return sendRequest(request);
	}

	public DataSet requestOrbitPropagation(String satellite, List<String> locations, long from, long to) {
		TlePropagationRequest request = new TlePropagationRequest(issuedBy, satellite, locations, from, to);
		return sendRequest(request);
	}

	
	
	
	public void requestOrbitPropagationStream(String satellite) {
		TlePropagationRequest request = new TlePropagationRequest(issuedBy, satellite);
		sendRequestStream(request);
	}

	public void requestOrbitPropagationStream(String satellite, long from, long to) {
		TlePropagationRequest request = new TlePropagationRequest(issuedBy, satellite, from, to);
		sendRequestStream(request);
	}

	public void requestOrbitPropagationStream(String satellite, String location, long from, long to) {
		TlePropagationRequest request = new TlePropagationRequest(issuedBy, satellite, location, from, to);
		sendRequestStream(request);
	}

	public void requestOrbitPropagationStream(String satellite, List<String> locations, long from, long to) {
		TlePropagationRequest request = new TlePropagationRequest(issuedBy, satellite, locations, from, to);
		sendRequestStream(request);
	}
	
	
	
	protected DataSet sendRequest(TlePropagationRequest request) {
		return template.requestBody(inject, request, DataSet.class);			
	}
	
	protected void sendRequestStream(TlePropagationRequest request) {
		request.addArgument("publish", true);
		template.sendBody(inject, request);
	}
}
