package org.hbird.exchange.configurator;

import java.util.List;

import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.core.CommandArgument;

public class StartAntennaControllerComponent extends StartComponent {

    public static final String DESCRIPTION = "Command to a configurator to start an antenna control component.";

    private static final long serialVersionUID = -8452914494565954568L;

    public StartAntennaControllerComponent(String componentname, String groundStationName, String satelliteName) {
        super(componentname, StartAntennaControllerComponent.class.getSimpleName(), DESCRIPTION);
        setArgumentValue(StandardArguments.GROUND_STATION_NAME, groundStationName);
        setArgumentValue(StandardArguments.SATELLITE_NAME, satelliteName);
        setArgumentValue(StandardArguments.QUEUE_NAME, "hbird.antennaschedule." + groundStationName);
    }

    /**
     * @see org.hbird.exchange.core.Command#getArgumentDefinitions()
     */
    @Override
    protected List<CommandArgument> getArgumentDefinitions(List<CommandArgument> args) {
        args = super.getArgumentDefinitions(args);
        args.add(new CommandArgument(StandardArguments.GROUND_STATION_NAME, "GroundStation name.", String.class, true));
        args.add(new CommandArgument(StandardArguments.SATELLITE_NAME, "Satellite name.", String.class, true));
        args.add(new CommandArgument(StandardArguments.QUEUE_NAME, "The name of the queue into which the schedule shall be send.", String.class, true));
        return args;
    }

    public String getGroundStationName() {
        return getArgumentValue(StandardArguments.GROUND_STATION_NAME, String.class);
    }

    public String getSatelliteName() {
        return getArgumentValue(StandardArguments.SATELLITE_NAME, String.class);
    }

    public String getQueueName() {
        return getArgumentValue(StandardArguments.QUEUE_NAME, String.class);
    }
}
