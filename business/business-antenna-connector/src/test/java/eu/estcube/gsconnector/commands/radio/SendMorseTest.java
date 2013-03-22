package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRadioConstants;

public class SendMorseTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private SendMorse createMessage = new SendMorse();

    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.SEND_MORSE);
        command.addParameter(new TelemetryParameter("Morse", "..--.."));
        string.append("+b ..--..\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
