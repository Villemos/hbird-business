package eu.estcube.gsconnector.commands.rotator;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryRotatorConstants;

public class StopTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private Stop createMessage = new Stop();

    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRotatorConstants.STOP);
        string.append("+S\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
