package org.hbird.business.systemtest;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.log4j.Logger;

public abstract class Tester {

	private static org.apache.log4j.Logger LOG = Logger.getLogger(Tester.class);
	
    @Produce(uri = "direct:injection")
    protected ProducerTemplate injection;

    protected Listener monitoringListener = null;
    
    protected Listener commandingListener = null;
    
    protected Listener failedCommandRequestListener = null;
    
	protected void azzert(boolean assertion) {
		if (assertion == false) {
			LOG.error("FAILED.");
		}
	}
	
	protected void azzert(boolean assertion, String message) {
		if (assertion == false) {
			LOG.error(message + " (FAILED)");
			System.exit(1);
		}
		else {
			LOG.info(message + " (OK)");
		}
	}

	public Listener getMonitoringListener() {
		return monitoringListener;
	}

	public void setMonitoringListener(Listener monitoringListener) {
		this.monitoringListener = monitoringListener;
	}

	public Listener getCommandingListener() {
		return commandingListener;
	}

	public void setCommandingListener(Listener commandingListener) {
		this.commandingListener = commandingListener;
	}

	public Listener getFailedCommandRequestListener() {
		return failedCommandRequestListener;
	}

	public void setFailedCommandRequestListener(
			Listener failedCommandRequestListener) {
		this.failedCommandRequestListener = failedCommandRequestListener;
	}

	
	
}
