package eu.estcube.gsconnector.commands.rotator;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRotatorConstants;

public class Dec2DmsTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private Dec2Dms createMessage = new Dec2Dms();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRotatorConstants.DEC2DMS);
        command.addParameter(new TelemetryParameter("Dec Degrees", 0.0));
        string.append("+d 0.0\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
