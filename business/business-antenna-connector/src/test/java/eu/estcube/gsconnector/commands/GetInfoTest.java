package eu.estcube.gsconnector.commands;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryRadioConstants;

public class GetInfoTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private GetInfo createMessage = new GetInfo();

    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.GET_INFO);
        string.append("+_\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
