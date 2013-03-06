package org.hbird.exchange.movementcontrol;

import java.util.List;

import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.CommandArgument;

public class PointingRequest extends Command {

    public static final String DESCRIPTION = "A command to point an antenna to a point (azimuth / elevation).";

    private static final long serialVersionUID = 4093011979495519927L;

    public PointingRequest(String issuedBy, String destination, double azimuth, double elevation, double doppler) {
        super(issuedBy, destination, "PointTo", DESCRIPTION);
        setArgumentValue(StandardArguments.AZIMUTH, azimuth);
        setArgumentValue(StandardArguments.ELEVATION, elevation);
        setArgumentValue(StandardArguments.DOPPLER, doppler);
    }

    /**
     * @see org.hbird.exchange.core.Command#getArgumentDefinitions()
     */
    @Override
    protected List<CommandArgument> getArgumentDefinitions(List<CommandArgument> args) {
        args.add(new CommandArgument(StandardArguments.AZIMUTH, "The azimuth to point the antenna", Double.class, "degree", null, true));
        args.add(new CommandArgument(StandardArguments.ELEVATION, "The elevation to point the antenna.", Double.class, "degree", null, true));
        args.add(new CommandArgument(StandardArguments.DOPPLER, "Value to use for calculating Doppler shift for the radio frequencies", Double.class, "", null,
                false));
        return args;
    }
}
