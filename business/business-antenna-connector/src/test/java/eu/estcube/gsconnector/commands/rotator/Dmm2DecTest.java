package eu.estcube.gsconnector.commands.rotator;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRotatorConstants;

public class Dmm2DecTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private Dmm2Dec createMessage = new Dmm2Dec();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRotatorConstants.DMMM2DEC);
        command.addParameter(new TelemetryParameter("Degrees", 0));
        command.addParameter(new TelemetryParameter("Dec Minutes", 0.0));
        command.addParameter(new TelemetryParameter("S/W", 0));
        string.append("+E 0 0.0 0\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
