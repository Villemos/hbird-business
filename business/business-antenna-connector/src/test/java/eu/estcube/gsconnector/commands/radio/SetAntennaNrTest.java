package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRadioConstants;

public class SetAntennaNrTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private SetAntennaNr createMessage = new SetAntennaNr();

    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.SET_ANTENNA_NR);
        command.addParameter(new TelemetryParameter("Antenna", 0));
        string.append("+Y 0\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
