package eu.estcube.gsconnector.commands.rotator;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRotatorConstants;
import eu.estcube.gs.rotator.SetConfig;

public class SetConfigTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private SetConfig createMessage = new SetConfig();

    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRotatorConstants.SET_CONFIG);
        command.addParameter(new TelemetryParameter("Token", 0));
        command.addParameter(new TelemetryParameter("Value", 0));
        string.append("+C 0 0\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
