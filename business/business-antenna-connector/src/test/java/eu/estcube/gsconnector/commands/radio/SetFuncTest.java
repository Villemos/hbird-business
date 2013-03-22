package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRadioConstants;

public class SetFuncTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private SetFunc createMessage = new SetFunc();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.SET_FUNC);
        command.addParameter(new TelemetryParameter("Func", "FAGC"));
        command.addParameter(new TelemetryParameter("Func Status", 0));
        string.append("+U FAGC 0\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
