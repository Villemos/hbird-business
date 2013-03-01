package org.hbird.exchange.movementcontrol;

import java.util.ArrayList;
import java.util.List;

import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.CommandArgument;

public class PointingRequest extends Command {

    public static final String DESCRIPTION = "A command to point an antenna to a point (azimuth / elevation).";

    private static final long serialVersionUID = 1222993693772995662L;

    public PointingRequest(String issuedBy, String destination, double azimuth, double elevation, double doppler, double dopplerShift) {
        super(issuedBy, destination, "PointTo", DESCRIPTION);
        setArgumentValue(StandardArguments.AZIMUTH, azimuth);
        setArgumentValue(StandardArguments.ELEVATION, elevation);
        setArgumentValue(StandardArguments.DOPPLER, doppler);
        setArgumentValue(StandardArguments.DOPPLER_SHIFT, dopplerShift);
    }

    /**
     * @see org.hbird.exchange.core.Command#getArgumentDefinitions()
     */
    @Override
    protected List<CommandArgument> getArgumentDefinitions() {
        List<CommandArgument> args = new ArrayList<CommandArgument>();
        args.add(new CommandArgument(StandardArguments.AZIMUTH, "The satellite comming / leaving contact.", "String", "Name", null, false));
        args.add(new CommandArgument(StandardArguments.ELEVATION, "The location.", "String", "Name", null, true));
        args.add(new CommandArgument(StandardArguments.DOPPLER, "Whether the contact event is a start of contact (true) or end of contact (false).", "boolean",
                "", true, true));
        args.add(new CommandArgument(StandardArguments.DOPPLER_SHIFT, "Whether the contact event is a start of contact (true) or end of contact (false).",
                "boolean", "", true, true));
        return args;
    }
}
