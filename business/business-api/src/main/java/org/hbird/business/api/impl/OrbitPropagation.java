/**
 * Licensed to the Hummingbird Foundation (HF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The HF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hbird.business.api.impl;

import java.util.List;

import org.hbird.business.api.IOrbitPrediction;
import org.hbird.exchange.core.DataSet;
import org.hbird.exchange.dataaccess.TlePropagationRequest;

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
