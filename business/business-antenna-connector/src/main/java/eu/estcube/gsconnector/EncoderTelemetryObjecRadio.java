package eu.estcube.gsconnector;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import eu.estcube.domain.JMSConstants;
import eu.estcube.domain.TelemetryRadioConstants;
import eu.estcube.gs.radio.nativecommands.SetFrequency;
import eu.estcube.gsconnector.commands.*;
import eu.estcube.gsconnector.commands.radio.*;

@Component(JMSConstants.GS_RADIO_PROTOCOL_ENCODER)
public class EncoderTelemetryObjecRadio extends EncoderTelemetryObjectAbstract {
    private Map<String, HamlibCommandBuilder> commandsHashMap;

    public EncoderTelemetryObjecRadio() {
        commandsHashMap = new HashMap<String, HamlibCommandBuilder>();
        commandsHashMap.put(TelemetryRadioConstants.SET_FREQUENCY, new SetFrequency());
        commandsHashMap.put(TelemetryRadioConstants.GET_FREQUENCY, new GetFrequency());
        commandsHashMap.put(TelemetryRadioConstants.SET_MODE, new SetMode());
        commandsHashMap.put(TelemetryRadioConstants.SET_LEVEL, new SetLevel());
        commandsHashMap.put(TelemetryRadioConstants.SET_VFO, new SetVFO());
        commandsHashMap.put(TelemetryRadioConstants.SET_PTT, new SetPTT());
        commandsHashMap.put(TelemetryRadioConstants.GET_PTT, new GetPTT());
        commandsHashMap.put(TelemetryRadioConstants.SET_ANTENNA_NR, new SetAntennaNr());
        commandsHashMap.put(TelemetryRadioConstants.CAPABILITIES, new Capabilities());
        commandsHashMap.put(TelemetryRadioConstants.CONFIGURATION, new Configuration());
        commandsHashMap.put(TelemetryRadioConstants.GET_MODE, new GetMode());
        commandsHashMap.put(TelemetryRadioConstants.SET_TRANSMIT_FREQUENCY, new SetTransmitFrequency());
        commandsHashMap.put(TelemetryRadioConstants.GET_TRANSMIT_FREQUENCY, new GetTransmitFrequency());
        commandsHashMap.put(TelemetryRadioConstants.SET_TRANSMIT_MODE, new SetTransmitMode());
        commandsHashMap.put(TelemetryRadioConstants.GET_TRANSMIT_MODE, new GetTransmitMode());
        commandsHashMap.put(TelemetryRadioConstants.SET_SPLIT_VFO, new SetSplitVFO());
        commandsHashMap.put(TelemetryRadioConstants.GET_SPLIT_VFO, new GetSplitVFO());
        commandsHashMap.put(TelemetryRadioConstants.SET_TUNING_STEP, new SetTuningStep());
        commandsHashMap.put(TelemetryRadioConstants.GET_TUNING_STEP, new GetTuningStep());
        commandsHashMap.put(TelemetryRadioConstants.GET_LEVEL, new GetLevel());
        commandsHashMap.put(TelemetryRadioConstants.SET_FUNC, new SetFunc());
        commandsHashMap.put(TelemetryRadioConstants.GET_FUNC, new GetFunc());
        commandsHashMap.put(TelemetryRadioConstants.SET_PARM, new SetParm());
        commandsHashMap.put(TelemetryRadioConstants.GET_PARM, new GetParm());
        commandsHashMap.put(TelemetryRadioConstants.VFO_OPERATION, new VfoOperation());
        commandsHashMap.put(TelemetryRadioConstants.SCAN, new Scan());
        commandsHashMap.put(TelemetryRadioConstants.SET_TRANCEIVE_MODE, new SetTranceiveMode());
        commandsHashMap.put(TelemetryRadioConstants.GET_TRANCEIVE_MODE, new GetTranceiveMode());
        commandsHashMap.put(TelemetryRadioConstants.SET_RPTR_SHIFT, new SetRprtShift());
        commandsHashMap.put(TelemetryRadioConstants.GET_RPTR_SHIFT, new GetRptrShift());
        commandsHashMap.put(TelemetryRadioConstants.SET_RPTR_OFFSET, new SetRptrOffset());
        commandsHashMap.put(TelemetryRadioConstants.GET_RPTR_OFFSET, new GetRptrOffset());
        commandsHashMap.put(TelemetryRadioConstants.SET_CTCSS_TONE, new SetCTCSSTone());
        commandsHashMap.put(TelemetryRadioConstants.GET_CTCSS_TONE, new GetCTCSSTone());
        commandsHashMap.put(TelemetryRadioConstants.SET_DCS_CODE, new SetDCSCode());
        commandsHashMap.put(TelemetryRadioConstants.GET_DCS_CODE, new GetDCSCode());
        commandsHashMap.put(TelemetryRadioConstants.SET_MEMORY_CHANNELNR, new SetMemoryChannelNr());
        commandsHashMap.put(TelemetryRadioConstants.GET_MEMORY_CHANNELNR, new GetMemoryChannelNr());
        commandsHashMap.put(TelemetryRadioConstants.SET_MEMORY_CHANNELDATA, new SetMemoryChannelData());
        commandsHashMap.put(TelemetryRadioConstants.GET_MEMORY_CHANNELDATA, new GetMemoryChannelData());
        commandsHashMap.put(TelemetryRadioConstants.SET_MEMORYBANK_NR, new SetMemoryBankNr());
        commandsHashMap.put(TelemetryRadioConstants.GET_INFO, new GetInfo());
        commandsHashMap.put(TelemetryRadioConstants.SET_RIT, new SetRIT());
        commandsHashMap.put(TelemetryRadioConstants.GET_RIT, new GetRit());
        commandsHashMap.put(TelemetryRadioConstants.GET_VFO, new GetVFO());
        commandsHashMap.put(TelemetryRadioConstants.SET_XIT, new SetXit());
        commandsHashMap.put(TelemetryRadioConstants.GET_XIT, new GetXit());
        commandsHashMap.put(TelemetryRadioConstants.GET_ANTENNA_NR, new GetAntennaNr());
        commandsHashMap.put(TelemetryRadioConstants.RESET, new Reset());
        commandsHashMap.put(TelemetryRadioConstants.SEND_RAW_CMD, new SendRawCmd());
        commandsHashMap.put(TelemetryRadioConstants.SEND_MORSE, new SendMorse());
        commandsHashMap.put(TelemetryRadioConstants.CONVERT_POWER2MW, new ConvertPower2MW());
        commandsHashMap.put(TelemetryRadioConstants.CONVERT_MW2POWER, new ConvertMW2Power());
        commandsHashMap.put(TelemetryRadioConstants.HELP, new Help());

    }

    @Override
    protected Map<String, HamlibCommandBuilder> getCommandsHashMap() {
        return commandsHashMap;
    }
}
