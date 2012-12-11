package org.hbird.business.systemtest;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.log4j.Logger;
import org.hbird.exchange.configurator.StartCommandComponent;
import org.hbird.exchange.configurator.StartMonitoringDataArchiveComponent;
import org.hbird.exchange.dataaccess.CommitRequest;
import org.hbird.exchange.dataaccess.DeletionRequest;

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
			LOG.error("SYSTEM TEST: " + message + " (FAILED)");
			System.exit(1);
		}
		else {
			LOG.info("SYSTEM TEST: " + message + " (OK)");
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


	protected boolean monitoringArchiveStarted = false;
	public void startMonitoringArchive() throws InterruptedException {

		if (monitoringArchiveStarted == false) {
			LOG.info("Issuing command for start of a parameter archive.");

			StartMonitoringDataArchiveComponent request = new StartMonitoringDataArchiveComponent("ParameterArchive");
			injection.sendBody(request);

			/** Give the component time to startup. */
			Thread.sleep(1000);
			
			/** TODO Send command to the archive to delete all data. */
			injection.sendBody(new DeletionRequest("SystemTest", "ParameterArchive", "*:*"));	

	        /** Send command to commit all changes. */
			injection.sendBody(new CommitRequest("SystemTest", "ParameterArchive"));	

			monitoringArchiveStarted = true;
		}		
	}
	
	protected boolean commandingChainStarted = false;
	public void startCommandingChain() throws InterruptedException {

		if (commandingChainStarted == false) {
			LOG.info("Issuing command for start of a commanding chain.");

			/** Create command component. */
			injection.sendBody(new StartCommandComponent("CommandingChain1"));
			
			Thread.sleep(2000);
			commandingChainStarted = true;
		}
	}
}
