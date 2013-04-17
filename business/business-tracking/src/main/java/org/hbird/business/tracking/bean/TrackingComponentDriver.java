package org.hbird.business.tracking.bean;

import org.apache.camel.model.RouteDefinition;
import org.hbird.business.core.SoftwareComponentDriver;
import org.hbird.business.tracking.TrackingComponent;

public class TrackingComponentDriver extends SoftwareComponentDriver {

    @Override
    protected void doConfigure() {

        TrackingControl controller = new TrackingControl(part.getName(), ((TrackingComponent) part).getAntenna(), ((TrackingComponent) part).getSatellite());

        /** Create the route for triggering the calculation. */
        RouteDefinition route = from(addTimer("antennacontrol", 60000l)).bean(controller, "process");
        addInjectionRoute(route);
    }
}
