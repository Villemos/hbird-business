package org.hbird.exchange.movementcontrol;

import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.CommandArgument;

public class PointingRequest extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5150542578352950463L;

	{
		arguments.put("azimuth", new CommandArgument("satellite", "The satellite comming / leaving contact.", "String", "Name", null, false));
		arguments.put("elevation", new CommandArgument("location", "The location.", "String", "Name", null, true));
		arguments.put("doppler", new CommandArgument("contactevent", "Whether the contact event is a start of contact (true) or end of contact (false).", "boolean", "", true, true));
		arguments.put("dopplershift", new CommandArgument("contactevent", "Whether the contact event is a start of contact (true) or end of contact (false).", "boolean", "", true, true));
	}

	public PointingRequest(String issuedBy, String destination, double azimuth, double elevation, double doppler, double dopplerShift) {
		super(issuedBy, destination, "PointTo", "A command to point an antenna to a point (azimuth / elevation).");
		arguments.get("azimuth").value = azimuth;
		arguments.get("elevation").value = elevation;
		arguments.get("doppler").value = doppler;
		arguments.get("dopplershift").value = dopplerShift;
	}
}
