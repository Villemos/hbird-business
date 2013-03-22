package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRadioConstants;

public class SetDCSCodeTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private SetDCSCode createMessage = new SetDCSCode();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.SET_DCS_CODE);
        command.addParameter(new TelemetryParameter("DCS Code", 6));
        string.append("+D 6\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
