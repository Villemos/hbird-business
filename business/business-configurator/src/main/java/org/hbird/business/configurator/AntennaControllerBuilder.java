package org.hbird.business.configurator;

import org.hbird.business.antennacontrol.AntennaControl;
import org.hbird.business.queuemanagement.api.QueueManagerApi;
import org.hbird.business.solr.api.Catalogue;
import org.hbird.business.solr.api.DataAccess;
import org.hbird.exchange.configurator.StartAntennaControllerComponent;
import org.hbird.exchange.constants.StandardArguments;

public class AntennaControllerBuilder extends ComponentBuilder {

    @Override
    protected void doConfigure() {

        StartAntennaControllerComponent request = (StartAntennaControllerComponent) this.command;

        String componentname = request.getArgumentValue(StandardArguments.COMPONENT_NAME, String.class);

        AntennaControl controller = new AntennaControl(componentname, request.getGroundStationName(), request.getSatelliteName(), request.getQueueName());
        controller.setApi(new DataAccess(this.getComponentName()));
        controller.setQueueApi(new QueueManagerApi(this.getComponentName()));
        controller.setCatalogueApi(new Catalogue(this.getComponentName()));

        /** Create the route for triggering the calculation. */
        // no initial delay at the moment
        from("timer://antennacontrol." + componentname + "?fixedRate=true&period=60000").bean(controller, "process");

        /** Route for injecting the commands to the antenna schedule. */
        from(controller.getInjectUri()).to("activemq:queue:" + request.getQueueName());
    }
}
