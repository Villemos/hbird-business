package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRadioConstants;

public class SetParmTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private SetParm createMessage = new SetParm();

    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.SET_PARM);
        command.addParameter(new TelemetryParameter("Parm", "ANN"));
        command.addParameter(new TelemetryParameter("Parm Value", ".."));
        string.append("+P ANN ..\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
