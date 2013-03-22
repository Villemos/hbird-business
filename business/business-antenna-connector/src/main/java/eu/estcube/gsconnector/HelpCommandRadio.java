package eu.estcube.gsconnector;

import java.util.HashMap;
import java.util.Map;

import eu.estcube.domain.TelemetryRadioConstants;

public class HelpCommandRadio extends HelpCommandAbstract{

    private Map<String, String> helpHashMap;

    public HelpCommandRadio() {
        helpHashMap = new HashMap<String, String>();
        helpHashMap.put("F", TelemetryRadioConstants.SET_FREQUENCY);
        helpHashMap.put("f", TelemetryRadioConstants.GET_FREQUENCY);
        helpHashMap.put("M", TelemetryRadioConstants.SET_MODE);
        helpHashMap.put("m", TelemetryRadioConstants.GET_MODE);
        helpHashMap.put("L", TelemetryRadioConstants.SET_LEVEL);
        helpHashMap.put("l", TelemetryRadioConstants.GET_LEVEL);
        helpHashMap.put("V", TelemetryRadioConstants.SET_VFO);
        helpHashMap.put("T", TelemetryRadioConstants.SET_PTT);
        helpHashMap.put("t", TelemetryRadioConstants.GET_PTT);
        helpHashMap.put("Y", TelemetryRadioConstants.SET_ANTENNA_NR);
        helpHashMap.put("1", TelemetryRadioConstants.CAPABILITIES);
        helpHashMap.put("3", TelemetryRadioConstants.CONFIGURATION);
        helpHashMap.put("l", TelemetryRadioConstants.SET_TRANSMIT_FREQUENCY);
        helpHashMap.put("i", TelemetryRadioConstants.GET_TRANSMIT_FREQUENCY);
        helpHashMap.put("X", TelemetryRadioConstants.SET_TRANSMIT_MODE);
        helpHashMap.put("x", TelemetryRadioConstants.GET_TRANSMIT_MODE);
        helpHashMap.put("S", TelemetryRadioConstants.SET_SPLIT_VFO);
        helpHashMap.put("s", TelemetryRadioConstants.GET_SPLIT_VFO);
        helpHashMap.put("N", TelemetryRadioConstants.SET_TUNING_STEP);
        helpHashMap.put("n", TelemetryRadioConstants.GET_TUNING_STEP);
        helpHashMap.put("U", TelemetryRadioConstants.SET_FUNC);
        helpHashMap.put("u", TelemetryRadioConstants.GET_FUNC);
        helpHashMap.put("P", TelemetryRadioConstants.SET_PARM);
        helpHashMap.put("p", TelemetryRadioConstants.GET_PARM);
        helpHashMap.put("G", TelemetryRadioConstants.VFO_OPERATION);
        helpHashMap.put("g", TelemetryRadioConstants.SCAN);
        helpHashMap.put("A", TelemetryRadioConstants.SET_TRANCEIVE_MODE);
        helpHashMap.put("a", TelemetryRadioConstants.GET_TRANCEIVE_MODE);
        helpHashMap.put("R", TelemetryRadioConstants.SET_RPTR_SHIFT);
        helpHashMap.put("r", TelemetryRadioConstants.GET_RPTR_SHIFT);
        helpHashMap.put("O", TelemetryRadioConstants.SET_RPTR_OFFSET);
        helpHashMap.put("o", TelemetryRadioConstants.GET_RPTR_OFFSET);
        helpHashMap.put("C", TelemetryRadioConstants.SET_CTCSS_TONE);
        helpHashMap.put("c", TelemetryRadioConstants.GET_CTCSS_TONE);
        helpHashMap.put("D", TelemetryRadioConstants.SET_DCS_CODE);
        helpHashMap.put("d", TelemetryRadioConstants.GET_DCS_CODE);
        helpHashMap.put("v", TelemetryRadioConstants.GET_VFO);
        helpHashMap.put("E", TelemetryRadioConstants.SET_MEMORY_CHANNELNR);
        helpHashMap.put("e", TelemetryRadioConstants.GET_MEMORY_CHANNELNR);
        helpHashMap.put("H", TelemetryRadioConstants.SET_MEMORY_CHANNELDATA);
        helpHashMap.put("h", TelemetryRadioConstants.GET_MEMORY_CHANNELDATA);
        helpHashMap.put("B", TelemetryRadioConstants.SET_MEMORYBANK_NR);
        helpHashMap.put("_", TelemetryRadioConstants.GET_INFO);
        helpHashMap.put("J", TelemetryRadioConstants.SET_RIT);
        helpHashMap.put("j", TelemetryRadioConstants.GET_RIT);
        helpHashMap.put("Z", TelemetryRadioConstants.SET_XIT);
        helpHashMap.put("z", TelemetryRadioConstants.GET_XIT);
        helpHashMap.put("y", TelemetryRadioConstants.GET_ANTENNA_NR);
        helpHashMap.put("*", TelemetryRadioConstants.RESET);
        helpHashMap.put("w", TelemetryRadioConstants.SEND_RAW_CMD);
        helpHashMap.put("b", TelemetryRadioConstants.SEND_MORSE);
        helpHashMap.put("2", TelemetryRadioConstants.CONVERT_POWER2MW);
        helpHashMap.put("4", TelemetryRadioConstants.CONVERT_MW2POWER);
        helpHashMap.put("?", TelemetryRadioConstants.HELP);

    }

    @Override
    protected Map<String, String> getHelpHashMap() {
        return helpHashMap;
    }

}
