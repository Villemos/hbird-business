package eu.estcube.gsconnector.commands.rotator;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryRotatorConstants;

public class GetPositionTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private GetPosition createMessage = new GetPosition();

    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRotatorConstants.GET_POSITION);
        string.append("+p\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
