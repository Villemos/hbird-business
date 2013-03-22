package eu.estcube.gsconnector;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import eu.estcube.domain.JMSConstants;
import eu.estcube.domain.TelemetryObject;
import eu.estcube.domain.TelemetryRadioConstants;

@Component(JMSConstants.GS_RADIO_PROTOCOL_DECODER)
public class DecoderTelemetryObjectRadio extends DecoderTelemetryObjectAbstract {

    private Map<String, String> hamlibToProtocolMap;

    public DecoderTelemetryObjectRadio() {

        hamlibToProtocolMap = new HashMap<String, String>();
        hamlibToProtocolMap.put("set_freq", TelemetryRadioConstants.SET_FREQUENCY);
        hamlibToProtocolMap.put("get_freq", TelemetryRadioConstants.GET_FREQUENCY);
        hamlibToProtocolMap.put("set_mode", TelemetryRadioConstants.SET_MODE);
        hamlibToProtocolMap.put("set_level", TelemetryRadioConstants.SET_LEVEL);
        hamlibToProtocolMap.put("set_vfo", TelemetryRadioConstants.SET_VFO);
        hamlibToProtocolMap.put("set_ptt", TelemetryRadioConstants.SET_PTT);
        hamlibToProtocolMap.put("get_ptt", TelemetryRadioConstants.GET_PTT);
        hamlibToProtocolMap.put("set_ant", TelemetryRadioConstants.SET_ANTENNA_NR);
        hamlibToProtocolMap.put("dump_caps", TelemetryRadioConstants.CAPABILITIES);
        hamlibToProtocolMap.put("dump_conf", TelemetryRadioConstants.CONFIGURATION);
        hamlibToProtocolMap.put("get_mode", TelemetryRadioConstants.GET_MODE);
        hamlibToProtocolMap.put("set_split_freq", TelemetryRadioConstants.SET_TRANSMIT_FREQUENCY);
        hamlibToProtocolMap.put("get_split_freq", TelemetryRadioConstants.GET_TRANSMIT_FREQUENCY);
        hamlibToProtocolMap.put("set_split_mode", TelemetryRadioConstants.SET_TRANSMIT_MODE);
        hamlibToProtocolMap.put("get_split_mode", TelemetryRadioConstants.GET_TRANSMIT_MODE);
        hamlibToProtocolMap.put("set_split_vfo", TelemetryRadioConstants.SET_SPLIT_VFO);
        hamlibToProtocolMap.put("get_split_vfo", TelemetryRadioConstants.GET_SPLIT_VFO);
        hamlibToProtocolMap.put("set_ts", TelemetryRadioConstants.SET_TUNING_STEP);
        hamlibToProtocolMap.put("get_ts", TelemetryRadioConstants.GET_TUNING_STEP);
        hamlibToProtocolMap.put("get_level", TelemetryRadioConstants.GET_LEVEL);
        hamlibToProtocolMap.put("set_func", TelemetryRadioConstants.SET_FUNC);
        hamlibToProtocolMap.put("get_func", TelemetryRadioConstants.GET_FUNC);
        hamlibToProtocolMap.put("set_parm", TelemetryRadioConstants.SET_PARM);
        hamlibToProtocolMap.put("get_parm", TelemetryRadioConstants.GET_PARM);
        hamlibToProtocolMap.put("vfo_op", TelemetryRadioConstants.VFO_OPERATION);
        hamlibToProtocolMap.put("scan", TelemetryRadioConstants.SCAN);
        hamlibToProtocolMap.put("set_trn", TelemetryRadioConstants.SET_TRANCEIVE_MODE);
        hamlibToProtocolMap.put("get_trn", TelemetryRadioConstants.GET_TRANCEIVE_MODE);
        hamlibToProtocolMap.put("set_rptr_shift", TelemetryRadioConstants.SET_RPTR_SHIFT);
        hamlibToProtocolMap.put("get_rptr_shift", TelemetryRadioConstants.GET_RPTR_SHIFT);
        hamlibToProtocolMap.put("set_rptr_offs", TelemetryRadioConstants.SET_RPTR_OFFSET);
        hamlibToProtocolMap.put("get_rptr_offs", TelemetryRadioConstants.GET_RPTR_OFFSET);
        hamlibToProtocolMap.put("set_ctcss_tone", TelemetryRadioConstants.SET_CTCSS_TONE);
        hamlibToProtocolMap.put("get_ctcss_tone", TelemetryRadioConstants.GET_CTCSS_TONE);
        hamlibToProtocolMap.put("set_dcs_code", TelemetryRadioConstants.SET_DCS_CODE);
        hamlibToProtocolMap.put("get_dcs_code", TelemetryRadioConstants.GET_DCS_CODE);
        hamlibToProtocolMap.put("get_vfo", TelemetryRadioConstants.GET_VFO);
        hamlibToProtocolMap.put("set_mem", TelemetryRadioConstants.SET_MEMORY_CHANNELNR);
        hamlibToProtocolMap.put("get_mem", TelemetryRadioConstants.GET_MEMORY_CHANNELNR);
        hamlibToProtocolMap.put("set_channel", TelemetryRadioConstants.SET_MEMORY_CHANNELDATA);
        hamlibToProtocolMap.put("get_channel", TelemetryRadioConstants.GET_MEMORY_CHANNELDATA);
        hamlibToProtocolMap.put("set_bank", TelemetryRadioConstants.SET_MEMORYBANK_NR);
        hamlibToProtocolMap.put("get_info", TelemetryRadioConstants.GET_INFO);
        hamlibToProtocolMap.put("set_rit", TelemetryRadioConstants.SET_RIT);
        hamlibToProtocolMap.put("get_rit", TelemetryRadioConstants.GET_RIT);
        hamlibToProtocolMap.put("set_xit", TelemetryRadioConstants.SET_XIT);
        hamlibToProtocolMap.put("get_xit", TelemetryRadioConstants.GET_XIT);
        hamlibToProtocolMap.put("get_ant", TelemetryRadioConstants.GET_ANTENNA_NR);
        hamlibToProtocolMap.put("reset", TelemetryRadioConstants.RESET);
        hamlibToProtocolMap.put("send_cmd", TelemetryRadioConstants.SEND_RAW_CMD);
        hamlibToProtocolMap.put("send_morse", TelemetryRadioConstants.SEND_MORSE);
        hamlibToProtocolMap.put("power2mW", TelemetryRadioConstants.CONVERT_POWER2MW);
        hamlibToProtocolMap.put("mW2power", TelemetryRadioConstants.CONVERT_MW2POWER);

    }

    @Override
    protected Map<String, String> getHamlibProtocolMap() {
        return hamlibToProtocolMap;
    }

    @Override
    protected TelemetryObject createHelp(String[] messageSplit) {
        TelemetryObject telemetryObject;
        HelpCommandRadio radioHelp = new HelpCommandRadio();
        telemetryObject = new TelemetryObject(TelemetryRadioConstants.HELP, new Date());
        telemetryObject = radioHelp.createHelpList(telemetryObject, messageSplit);
        return telemetryObject;
    }
    
    protected String getDeviceName(){
        return JMSConstants.GS_RIG_CTLD;
    }
}
