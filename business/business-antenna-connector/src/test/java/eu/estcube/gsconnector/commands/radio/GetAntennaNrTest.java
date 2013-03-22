package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryRadioConstants;

public class GetAntennaNrTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private GetAntennaNr createMessage = new GetAntennaNr();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.GET_ANTENNA_NR);
        string.append("+y\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
