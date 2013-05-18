package org.hbird.exchange.dataaccess;

import static org.hbird.exchange.dataaccess.Arguments.GROUND_STATION_ID;
import static org.hbird.exchange.dataaccess.Arguments.SATELLITE_ID;
import static org.hbird.exchange.dataaccess.Arguments.create;

import java.util.List;

import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.core.CommandArgument;
import org.hbird.exchange.navigation.LocationContactEvent;

public class LocationContactEventRequest extends DataRequest {

    private static final long serialVersionUID = -5990938785008117581L;

    public static final String DESCRIPTION = "A request to retrieve location contact events of a satellite / ground station.";

    public LocationContactEventRequest(String ID) {
        super(ID, LocationContactEventRequest.class.getSimpleName());
        setClass(LocationContactEvent.class.getSimpleName());
        setDescription(DESCRIPTION);
    }

    /**
     * @see org.hbird.exchange.dataaccess.DataRequest#getArgumentDefinitions()
     */
    @Override
    protected List<CommandArgument> getArgumentDefinitions(List<CommandArgument> args) {
        args = super.getArgumentDefinitions(args);
        args.add(create(SATELLITE_ID));
        args.add(create(GROUND_STATION_ID));
        return args;
    }

    public void setSatelliteID(String satelliteID) {
        setArgumentValue(StandardArguments.SATELLITE_ID, satelliteID);
    }

    public void setGroundStationID(String groundStationID) {
        setArgumentValue(StandardArguments.GROUND_STATION_ID, groundStationID);
    }

    public String getSatelliteID() {
        return getArgumentValue(StandardArguments.SATELLITE_ID, String.class);
    }

    public String getGroundStationID() {
        return getArgumentValue(StandardArguments.GROUND_STATION_ID, String.class);
    }
}
