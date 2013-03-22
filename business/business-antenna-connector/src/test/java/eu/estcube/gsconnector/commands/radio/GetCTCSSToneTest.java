package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryRadioConstants;

public class GetCTCSSToneTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private GetCTCSSTone createMessage = new GetCTCSSTone();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.GET_CTCSS_TONE);
        string.append("+c\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
