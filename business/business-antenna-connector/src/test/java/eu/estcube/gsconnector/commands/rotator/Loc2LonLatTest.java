package eu.estcube.gsconnector.commands.rotator;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRotatorConstants;

public class Loc2LonLatTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private Loc2LonLat createMessage = new Loc2LonLat();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRotatorConstants.LOC2LONLAT);
        command.addParameter(new TelemetryParameter("Locator", "te"));
        string.append("+l te\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
