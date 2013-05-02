package org.hbird.exchange.groundstation;

import org.hbird.exchange.core.Command;

public class PointingRequest extends Command {

    public static final String DESCRIPTION = "A command to point an antenna to a point (azimuth / elevation).";

    private static final long serialVersionUID = 4093011979495519927L;

    public PointingRequest(String ID) {
        super(ID, PointingRequest.class.getSimpleName());
        setDescription(DESCRIPTION);
    }
}
