package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRadioConstants;

public class GetParmTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private GetParm createMessage = new GetParm();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.GET_PARM);
        command.addParameter(new TelemetryParameter("Parm", "ANN"));
        string.append("+p ANN\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
