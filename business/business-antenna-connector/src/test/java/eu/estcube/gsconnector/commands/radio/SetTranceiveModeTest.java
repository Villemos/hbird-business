package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRadioConstants;

public class SetTranceiveModeTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private SetTranceiveMode createMessage = new SetTranceiveMode();

    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.SET_TRANCEIVE_MODE);
        command.addParameter(new TelemetryParameter("Transceive", "OFF"));
        string.append("+A OFF\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
