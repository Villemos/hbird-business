package eu.estcube.gsconnector.commands.rotator;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRotatorConstants;

public class MoveTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private Move createMessage = new Move();

    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRotatorConstants.MOVE);
        command.addParameter(new TelemetryParameter("Direction", 2));
        command.addParameter(new TelemetryParameter("Speed", 1));
        string.append("+M 2 1\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
