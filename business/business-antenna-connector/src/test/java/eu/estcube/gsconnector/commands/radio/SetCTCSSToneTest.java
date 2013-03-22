package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRadioConstants;

public class SetCTCSSToneTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private SetCTCSSTone createMessage = new SetCTCSSTone();

    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.SET_CTCSS_TONE);
        command.addParameter(new TelemetryParameter("CTCSS Tone", 0.01));
        string.append("+C 0.01\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
