/**
 * Created by Villemos Solutions (www.villemos.com), 2012.
 * 
 * Licensed to the Hummingbird Foundation (HF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
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
package org.hbird.business.cfdp.sender;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultExchange;
import org.hbird.exchange.cfdp.pdus.EofPdu;
import org.hbird.exchange.cfdp.pdus.FilePdu;
import org.hbird.exchange.cfdp.pdus.MetadataPdu;
import org.hbird.exchange.cfdp.requests.Put;


public class TransferTransaction extends Thread {

	protected Put put = null;

	protected String ID = UUID.randomUUID().toString();

	protected String pdus = "seda:pdus";

	protected String indications = null;

	protected CamelContext context = null;

	protected int segmentSize = 10000;
	
	public TransferTransaction(Put originalPutRequest, CamelContext context, String pdus, String indications) {
		this.put = originalPutRequest;
		this.context = context;
		this.pdus = pdus;
		this.indications = indications;
	}

	public String getID() {
		return ID;
	}

	public void run() {

		try {
			/** Get the file. */
			File file = new File(put.sourceFileName);
			
			/** Send metadata PDU. */
			send(pdus, createExchange(new MetadataPdu(put.destination, ID, false, file.length(), put.sourceFileName, put.destinationFileName, put.messagesToUser)));

			/** Send file segments. */
			InputStream is = new FileInputStream(file);
			int numRead = 0;
			byte[] bytes = new byte[segmentSize];
			long index = 0;
			while ((numRead=is.read(bytes, 0, segmentSize)) >= 0) {
				byte[] segment = null;
				if (numRead == segmentSize) {
					segment = bytes;
				}
				else {
					segment = new byte[numRead];
					System.arraycopy(bytes, 0, segment, 0, numRead);
				}
				send(pdus, createExchange(new FilePdu(put.destination, ID, segment, numRead, index)));					
				index++;
			}

			/** Send EOF PDU. */
			send(pdus, createExchange(new EofPdu(put.destination, ID, file.length())));
		}
		catch (Exception e) {
			// TODO
		} 
		
	}

	protected void send(String endpoint, Exchange exchange) {
		context.createProducerTemplate().send(endpoint, exchange);
	}
	
	protected Exchange createExchange(Object body) {
		Exchange exchange = new DefaultExchange(context);
		exchange.getIn().setBody(body);
		return exchange;
	}
}
