package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRadioConstants;

public class SetMemoryBankNrTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private SetMemoryBankNr createMessage = new SetMemoryBankNr();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.SET_MEMORYBANK_NR);
        command.addParameter(new TelemetryParameter("Bank", ".."));
        string.append("+B ..\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
