package org.hbird.business.configurator;

import org.hbird.business.antennacontrol.AntennaControl;
import org.hbird.business.api.ApiFactory;
import org.hbird.exchange.configurator.StartAntennaControllerComponent;
import org.hbird.exchange.constants.StandardArguments;

public class AntennaControllerBuilder extends ComponentBuilder {

    @Override
    protected void doConfigure() {

        StartAntennaControllerComponent request = (StartAntennaControllerComponent) this.command;

        String componentname = request.getArgumentValue(StandardArguments.COMPONENT_NAME, String.class);

        AntennaControl controller = new AntennaControl(componentname, request.getGroundStationName(), request.getSatelliteName(), request.getQueueName());
        controller.setApi(ApiFactory.getDataAccessApi(this.getComponentName()));
        controller.setQueueApi(ApiFactory.getQueueManagementApi(this.getComponentName()));
        controller.setCatalogueApi(ApiFactory.getCatalogueApi(this.getComponentName()));

        /** Create the route for triggering the calculation. */
        // no initial delay at the moment
        from("timer://antennacontrol." + componentname + "?fixedRate=true&period=60000").bean(controller, "process");

        /** Route for injecting the commands to the antenna schedule. */
        from(controller.getInjectUri()).to("activemq:queue:" + request.getQueueName());
    }
}
