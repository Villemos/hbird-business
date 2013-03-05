package org.hbird.exchange.dataaccess;

import static org.hbird.exchange.dataaccess.Arguments.GROUND_STATION_NAME;
import static org.hbird.exchange.dataaccess.Arguments.SATELLITE_NAME;
import static org.hbird.exchange.dataaccess.Arguments.VISIBILITY;
import static org.hbird.exchange.dataaccess.Arguments.create;

import java.util.List;

import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.constants.StandardComponents;
import org.hbird.exchange.core.CommandArgument;
import org.hbird.exchange.navigation.LocationContactEvent;

public class LocationContactEventRequest extends DataRequest {

    private static final long serialVersionUID = -5990938785008117581L;

    public LocationContactEventRequest(String issuedBy, String location) {
        super(issuedBy, StandardComponents.ARCHIVE);
        setClass(LocationContactEvent.class.getSimpleName());
        setArgumentValue(StandardArguments.GROUND_STATION_NAME, location);
    }

    public LocationContactEventRequest(String issuedBy, String location, boolean visibility) {
        this(issuedBy, location);
        setClass(LocationContactEvent.class.getSimpleName());
        setArgumentValue(StandardArguments.VISIBILITY, visibility);
    }

    /**
     * @see org.hbird.exchange.dataaccess.DataRequest#getArgumentDefinitions()
     */
    @Override
    protected List<CommandArgument> getArgumentDefinitions() {
        List<CommandArgument> args = super.getArgumentDefinitions();
        args.add(create(SATELLITE_NAME));
        args.add(create(GROUND_STATION_NAME));
        args.add(create(VISIBILITY));
        return args;
    }

    public void setSatelliteName(String satellite) {
        setArgumentValue(StandardArguments.SATELLITE_NAME, satellite);
    }

    public void setLocation(String location) {
        setArgumentValue(StandardArguments.GROUND_STATION_NAME, location);
    }

    public String getSatelliteName() {
        return getArgumentValue(StandardArguments.SATELLITE_NAME, String.class);
    }

    public String getGroundStationName() {
        return getArgumentValue(StandardArguments.GROUND_STATION_NAME, String.class);
    }

    public boolean getVisibility() {
        return getArgumentValue(StandardArguments.VISIBILITY, Boolean.class);
    }
}
