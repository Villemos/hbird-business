package org.hbird.business.systemtest;

import org.apache.camel.Exchange;
import org.apache.log4j.Logger;

public class Finisher extends Tester {

	private static org.apache.log4j.Logger LOG = Logger.getLogger(Finisher.class);

	public void process(Exchange exchange) throws InterruptedException {
		LOG.info("System Test done.");
		
		Thread.sleep(2000);
		System.exit(1);
	}	
}
