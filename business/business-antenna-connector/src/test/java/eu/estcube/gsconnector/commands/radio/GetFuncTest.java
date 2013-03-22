package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRadioConstants;

public class GetFuncTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private GetFunc createMessage = new GetFunc();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.GET_FUNC);
        command.addParameter(new TelemetryParameter("Func", "FAGC"));
        string.append("+u FAGC\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
