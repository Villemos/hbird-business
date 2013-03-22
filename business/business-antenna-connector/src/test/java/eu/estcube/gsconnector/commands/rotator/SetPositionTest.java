package eu.estcube.gsconnector.commands.rotator;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRotatorConstants;
import eu.estcube.gs.rotator.SetPosition;

public class SetPositionTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private SetPosition createMessage = new SetPosition();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRotatorConstants.SET_POSITION);
        command.addParameter(new TelemetryParameter("Azimuth", 0));
        command.addParameter(new TelemetryParameter("Elevation", 0));
        string.append("+P 0 0\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
