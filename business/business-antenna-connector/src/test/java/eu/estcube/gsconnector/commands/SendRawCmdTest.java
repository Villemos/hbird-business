package eu.estcube.gsconnector.commands;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRadioConstants;

public class SendRawCmdTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private SendRawCmd createMessage = new SendRawCmd();

    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.SEND_RAW_CMD);
        command.addParameter(new TelemetryParameter("Cmd", "/x95"));
        string.append("+w /x95\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
