package eu.estcube.gsconnector.commands.rotator;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRotatorConstants;

public class LonLat2LocTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private LonLat2Loc createMessage = new LonLat2Loc();

    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRotatorConstants.LONLAT2LOC);
        command.addParameter(new TelemetryParameter("Longitude", 1));
        command.addParameter(new TelemetryParameter("Latitude", 1));
        command.addParameter(new TelemetryParameter("Loc Len", 1));
        string.append("+L 1 1 1\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
