package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRadioConstants;

public class SetSplitVfoTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private SetSplitVFO createMessage = new SetSplitVFO();

    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.SET_SPLIT_VFO);
        command.addParameter(new TelemetryParameter("Split", 0));
        command.addParameter(new TelemetryParameter("TX VFO", "VFO"));
        string.append("+S 0 VFO\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
