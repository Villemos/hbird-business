package org.hbird.business.configurator;

import org.hbird.business.antennacontrol.AntennaControl;
import org.hbird.exchange.configurator.StartAntennaControllerComponent;

public class AntennaControllerBuilder extends ComponentBuilder {

	@Override
	protected void doConfigure() {
	
		StartAntennaControllerComponent request = (StartAntennaControllerComponent) this.command;

		String componentname = (String) request.getArgument("componentname");
		
		AntennaControl controller = new AntennaControl(componentname, request.getLocation(), request.getSatellite(), request.getQueueName());
		
		/** Create the route for triggering the calculation. */
		from("timer://antennacontrol." + componentname + "?fixedRate=true&period=60000").bean(controller, "process");
		
		/** Route for injecting the commands to the antenna schedule. */
		from(controller.getInjectUri()).to("activemq:queue:" + request.getQueueName());
	}
}
