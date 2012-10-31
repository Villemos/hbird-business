/**
 * villemos solutions [space^] (http://www.villemos.com) 
 * Probe. Send. Act. Emergent solution. 
 * Copyright 2011 Gert Villemos
 * All Rights Reserved.
 * 
 * Released under the Apache license, version 2.0 (do what ever
 * you want, just dont claim ownership).
 * 
 * NOTICE:  All information contained herein is, and remains
 * the property of villemos solutions, and its suppliers
 * if any. The intellectual and technical concepts contained
 * herein are proprietary to villemos solutions
 * and its suppliers and may be covered by European and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * 
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from villemos solutions.
 * 
 * And it wouldn't be nice either.
 * 
 */
package org.hbird.business.importer;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.ScheduledPollEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImportEndpoint extends ScheduledPollEndpoint {

	private static final transient Logger LOG = LoggerFactory.getLogger(ImportEndpoint.class);

	protected String filename = null;

	public ImportEndpoint() {
	}

	public ImportEndpoint(String uri, ImportComponent component) {
		super(uri, component);
	}

	public ImportEndpoint(String endpointUri) {
		super(endpointUri);
	}

	public Producer createProducer() throws Exception {
		return new ImportProducer(this);
	}

	public Consumer createConsumer(Processor processor) throws Exception {
		return new ImportConsumer(this, processor);
	}

	public boolean isSingleton() {
		return true;
	}

	public void setFilename(String filename) {
		this.filename = filename;

		if (this.filename.endsWith(".xls") == false) {
			this.filename = this.filename + ".xls";
		}
	}

	public String getFilename() {
		return filename;
	}
	
	
}
