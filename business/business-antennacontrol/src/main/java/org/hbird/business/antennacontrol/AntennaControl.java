package org.hbird.business.antennacontrol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Body;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.hbird.exchange.core.DataSet;
import org.hbird.exchange.core.ILocationSpecific;
import org.hbird.exchange.core.Named;
import org.hbird.exchange.navigation.ContactData;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.queuemanagement.ClearQueue;
import org.hbird.queuemanagement.RemoveQueueElements;



public class AntennaControl {

	protected String forLocation = "";
	
	/** 
	 * 1. Receive data set.
	 * 3. Check that the data sets are complete, i.e. contract data exist from the start to the end.
	 * 4. Create and eject block command.
	 * 
	 * */
	public void process(@Body DataSet dataSet, CamelContext context) {
		
		/** Purge command queue for all other TLE based commands */
		ProducerTemplate producer = context.createProducerTemplate();
		producer.sendBody("direct:navigationinjection", new RemoveQueueElements(this.getClass().getCanonicalName(), "QueueManager", "TLE/*"));
		
		Map<String, List<Named>> locationSpecificData = new HashMap<String, List<Named>>();
		
		/** Find all contact data in between the start / stop events for each location. The dataset is sorted based on timestamp (convention). */
		for (Named entry : dataSet.getDataset()) {
			if (entry instanceof ILocationSpecific) {
				ILocationSpecific location = (ILocationSpecific) entry;
				if (locationSpecificData.containsKey(location.getLocation()) == false) {
					locationSpecificData.put(location.getLocation(), new ArrayList<Named>());
				}
				locationSpecificData.get(location.getLocation()).add(entry);
			}
		}
		
		/** For each location create the command block. */
	}	
}
