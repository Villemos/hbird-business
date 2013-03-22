package eu.estcube.gsconnector.commands.rotator;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryRotatorConstants;
import eu.estcube.gs.rotator.Park;

public class ParkTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private Park createMessage = new Park();

    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRotatorConstants.PARK);
        string.append("+K\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
