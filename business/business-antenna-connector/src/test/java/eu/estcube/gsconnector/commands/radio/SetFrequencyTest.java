package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRadioConstants;
import eu.estcube.gs.radio.nativecommands.SetFrequency;

public class SetFrequencyTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private SetFrequency createMessage = new SetFrequency();

    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.SET_FREQUENCY);
        command.addParameter(new TelemetryParameter("Frequency", 0));
        string.append("+F 0\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
