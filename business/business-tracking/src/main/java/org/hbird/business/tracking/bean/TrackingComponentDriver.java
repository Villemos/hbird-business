package org.hbird.business.tracking.bean;

import org.apache.camel.model.RouteDefinition;
import org.hbird.business.api.ApiFactory;
import org.hbird.business.core.SoftwareComponentDriver;
import org.hbird.business.tracking.TrackingComponent;

public class TrackingComponentDriver extends SoftwareComponentDriver {

    @Override
    protected void doConfigure() {

        TrackingControl controller = new TrackingControl(part.getQualifiedName(), ((TrackingComponent) part).getAntenna(), ((TrackingComponent) part).getSatellite());
        controller.setApi(ApiFactory.getDataAccessApi(part.getQualifiedName()));
        controller.setQueueApi(ApiFactory.getQueueManagementApi(part.getQualifiedName()));
        controller.setCatalogueApi(ApiFactory.getCatalogueApi(part.getQualifiedName()));

        /** Create the route for triggering the calculation. */
        RouteDefinition route = from(addTimer("antennacontrol", 60000l)).bean(controller, "process");
        addInjectionRoute(route);
    }
}
