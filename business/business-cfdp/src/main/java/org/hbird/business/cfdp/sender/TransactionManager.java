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

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Body;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Headers;
import org.apache.camel.impl.DefaultExchange;
import org.hbird.exchange.cfdp.indications.Transaction;
import org.hbird.exchange.cfdp.requests.Cancel;
import org.hbird.exchange.cfdp.requests.Put;
import org.hbird.exchange.cfdp.requests.Report;
import org.hbird.exchange.cfdp.requests.Resume;
import org.hbird.exchange.cfdp.requests.Suspend;


public class TransactionManager {

	protected Map<String, TransferTransaction> transactions = new HashMap<String, TransferTransaction>();

	protected String pdus = "seda:pdus";
	protected String indications = "seda:indications";
	
	public void put(@Body Put put, @Headers Map<String, Object> headers, CamelContext context) {

		/** Create new Transaction. */
		TransferTransaction transaction = new TransferTransaction(put, context, pdus,indications);
		transactions.put(transaction.getID(), transaction);

		/** Start the transaction. */
		transaction.start();
		
		/** Send the transaction Indication. */
		sendIndication(new Transaction(transaction.getID()), context);
	}	
	
	protected void sendIndication(Object body, CamelContext context) {
		Exchange exchange = new DefaultExchange(context);
		exchange.getIn().setBody(body);
		context.createProducerTemplate().send("direct:indications", exchange);		
	}
	
	public void suspend(@Body Suspend suspend) {
		TransferTransaction transaction = transactions.get(suspend.transactionID);
		if (transaction != null) {
			transaction.suspend();
		}
	}
	
	public void resume(@Body Resume resume) {
		TransferTransaction transaction = transactions.get(resume.transactionID);
		if (transaction != null) {
			transaction.resume();
		}
	}
	
	public void cancel(@Body Cancel cancel) {
		TransferTransaction transaction = transactions.get(cancel.transactionID);
		if (transaction != null) {
			transaction.stop();
		}
	}

	public void report(@Body Report report) {
		TransferTransaction transaction = transactions.get(report.transactionID);
		if (transaction != null) {
			/** TODO */
		}		
	}
}
