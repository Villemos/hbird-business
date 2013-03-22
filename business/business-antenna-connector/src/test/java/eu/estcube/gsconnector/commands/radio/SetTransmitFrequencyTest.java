package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRadioConstants;

public class SetTransmitFrequencyTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private SetTransmitFrequency createMessage = new SetTransmitFrequency();

    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.SET_TRANSMIT_FREQUENCY);
        command.addParameter(new TelemetryParameter("TX Frequency", 0));
        string.append("+l 0\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
