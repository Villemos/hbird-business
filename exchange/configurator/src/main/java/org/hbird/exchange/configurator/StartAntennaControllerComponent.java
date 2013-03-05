package org.hbird.exchange.configurator;

import java.util.List;

import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.core.CommandArgument;
import org.hbird.exchange.navigation.GroundStation;
import org.hbird.exchange.navigation.Satellite;

public class StartAntennaControllerComponent extends StartComponent {

    public static final String DESCRIPTION = "Command to a configurator to start an antenna control component.";

    private static final long serialVersionUID = -8452914494565954568L;

    public StartAntennaControllerComponent(String componentname, String groundStation, String satellite) {
        super(componentname, StartAntennaControllerComponent.class.getSimpleName(), DESCRIPTION);
        setArgumentValue(StandardArguments.GROUND_STATION_NAME, groundStation);
        setArgumentValue(StandardArguments.SATELLITE_NAME, satellite);
        setArgumentValue(StandardArguments.QUEUE_NAME, "hbird.antennaschedule." + groundStation);
    }

    /**
     * @see org.hbird.exchange.core.Command#getArgumentDefinitions()
     */
    // TODO - 27.02.2013, kimmell - CHECK Satellite vs name of the satellite
    // TODO - 27.02.2013, kimmell - CHECK for string "groundStation"
    @Override
    protected List<CommandArgument> getArgumentDefinitions() {
        List<CommandArgument> args = super.getArgumentDefinitions();
        args.add(new CommandArgument(StandardArguments.GROUND_STATION_NAME, "GroundStation definition.", "GroundStation", "", null, true));
        args.add(new CommandArgument(StandardArguments.SATELLITE_NAME, "Satellite definition", "Satellite", "", null, true));
        args.add(new CommandArgument(StandardArguments.QUEUE_NAME, "The name of the queue into which the schedule shall be send.", "String", "", null, true));
        return args;
    }

    public GroundStation getGroundStation() {
        return getArgumentValue("groundStation", GroundStation.class);
    }

    public Satellite getSatellite() {
        return getArgumentValue(StandardArguments.SATELLITE_NAME, Satellite.class);
    }

    public String getQueueName() {
        return getArgumentValue(StandardArguments.QUEUE_NAME, String.class);
    }
}
