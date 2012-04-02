package org.hbird.business.analysis;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TelemetryPrintout {
	
	private final static Logger LOG = LoggerFactory.getLogger(TelemetryPrintout.class);
	
	// TODO Can this be replaced with Camel Log?
	// http://camel.apache.org/log.html
	public static void logParameter(@Header("ParameterName") String name, @Body Object value) {
		LOG.info(name + ": " + value);
		System.out.println(name + ": " + value);
	}

}
