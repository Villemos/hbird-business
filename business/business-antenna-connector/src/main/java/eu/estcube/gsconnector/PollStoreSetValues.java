package eu.estcube.gsconnector;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

import eu.estcube.domain.JMSConstants;
import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryRadioConstants;
import eu.estcube.domain.TelemetryRotatorConstants;
import eu.estcube.gs.radio.nativecommands.SetFrequency;
import eu.estcube.gs.rotator.SetPosition;
import eu.estcube.gsconnector.commands.HamlibCommandBuilder;
import eu.estcube.gsconnector.commands.radio.SetAntennaNr;
import eu.estcube.gsconnector.commands.radio.SetCTCSSTone;
import eu.estcube.gsconnector.commands.radio.SetDCSCode;
import eu.estcube.gsconnector.commands.radio.SetMemoryChannelNr;
import eu.estcube.gsconnector.commands.radio.SetMode;
import eu.estcube.gsconnector.commands.radio.SetPTT;
import eu.estcube.gsconnector.commands.radio.SetRIT;
import eu.estcube.gsconnector.commands.radio.SetRprtShift;
import eu.estcube.gsconnector.commands.radio.SetRptrOffset;
import eu.estcube.gsconnector.commands.radio.SetSplitVFO;
import eu.estcube.gsconnector.commands.radio.SetTranceiveMode;
import eu.estcube.gsconnector.commands.radio.SetTransmitFrequency;
import eu.estcube.gsconnector.commands.radio.SetTransmitMode;
import eu.estcube.gsconnector.commands.radio.SetTuningStep;
import eu.estcube.gsconnector.commands.radio.SetVFO;

@Component("storeSetValues")
public final class PollStoreSetValues {
    private static Map<String, TelemetryCommand> requiredParameterValueDataStore;

    public static Map<String, TelemetryCommand> getRequiredParameterValueDataStore() {
        return requiredParameterValueDataStore;
    }

    private Map<String, HamlibCommandBuilder> commandsHashMap;

    public PollStoreSetValues() {
        requiredParameterValueDataStore = new HashMap<String, TelemetryCommand>();
        commandsHashMap = new HashMap<String, HamlibCommandBuilder>();
        commandsHashMap.put(TelemetryRotatorConstants.SET_POSITION, new SetPosition());
        commandsHashMap.put(TelemetryRadioConstants.SET_ANTENNA_NR, new SetAntennaNr());
        commandsHashMap.put(TelemetryRadioConstants.SET_CTCSS_TONE, new SetCTCSSTone());
        commandsHashMap.put(TelemetryRadioConstants.SET_DCS_CODE, new SetDCSCode());
        commandsHashMap.put(TelemetryRadioConstants.SET_FREQUENCY, new SetFrequency());
        commandsHashMap.put(TelemetryRadioConstants.SET_MEMORY_CHANNELNR, new SetMemoryChannelNr());
        commandsHashMap.put(TelemetryRadioConstants.SET_MODE, new SetMode());
        commandsHashMap.put(TelemetryRadioConstants.SET_PTT, new SetPTT());
        commandsHashMap.put(TelemetryRadioConstants.SET_RIT, new SetRIT());
        commandsHashMap.put(TelemetryRadioConstants.SET_RPTR_OFFSET, new SetRptrOffset());
        commandsHashMap.put(TelemetryRadioConstants.SET_RPTR_SHIFT, new SetRprtShift());
        commandsHashMap.put(TelemetryRadioConstants.SET_SPLIT_VFO, new SetSplitVFO());
        commandsHashMap.put(TelemetryRadioConstants.SET_TRANCEIVE_MODE, new SetTranceiveMode());
        commandsHashMap.put(TelemetryRadioConstants.SET_TRANSMIT_FREQUENCY, new SetTransmitFrequency());
        commandsHashMap.put(TelemetryRadioConstants.SET_TRANSMIT_MODE, new SetTransmitMode());
        commandsHashMap.put(TelemetryRadioConstants.SET_TUNING_STEP, new SetTuningStep());
        commandsHashMap.put(TelemetryRadioConstants.SET_VFO, new SetVFO());
    }

    public void storeData(Exchange exchange) {
        TelemetryCommand command = null;
        /*
         * No other types of messages may go past here, other than TelemetryCommands. So if somehow a message reaches here thats
         * not telemetryCommand, then it will be destroyed.
         */
        try {
            command = (TelemetryCommand) exchange.getIn().getBody();
        } catch (ClassCastException e) {
            exchange.getIn().setHeaders(null);
            exchange.getIn().setBody(null);
            return;
        }
        if (commandsHashMap.get(command.getCommandName()) != null) {
            requiredParameterValueDataStore.put(command.getCommandName(), command);
            exchange.getIn().setHeader(JMSConstants.HEADER_POLLING, JMSConstants.POLL_SET_COMMAND);
        }

        if (exchange.getIn().getHeader(JMSConstants.HEADER_POLLING) == null) {
            exchange.getIn().setHeader(JMSConstants.HEADER_POLLING, JMSConstants.POLL_NOT_REQUIRED);
        }
    }
}