package eu.estcube.gsconnector;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import eu.estcube.domain.JMSConstants;
import eu.estcube.domain.TelemetryObject;
import eu.estcube.domain.TelemetryRotatorConstants;

@Component(JMSConstants.GS_ROTATOR_PROTOCOL_DECODER)
public class DecoderTelemetryObjectRotator extends DecoderTelemetryObjectAbstract {

    private Map<String, String> hamlibToProtocolMap;

    public DecoderTelemetryObjectRotator() {
        hamlibToProtocolMap = new HashMap<String, String>();
        hamlibToProtocolMap.put("a_sp2a_lp", TelemetryRotatorConstants.AZIMUTH_SHORTPATH2LONGPATH);
        hamlibToProtocolMap.put("set_pos", TelemetryRotatorConstants.SET_POSITION);
        hamlibToProtocolMap.put("get_pos", TelemetryRotatorConstants.GET_POSITION);
        hamlibToProtocolMap.put("stop", TelemetryRotatorConstants.STOP);
        hamlibToProtocolMap.put("park", TelemetryRotatorConstants.PARK);
        hamlibToProtocolMap.put("move", TelemetryRotatorConstants.MOVE);
        hamlibToProtocolMap.put("dump_caps", TelemetryRotatorConstants.CAPABILITIES);
        hamlibToProtocolMap.put("reset", TelemetryRotatorConstants.RESET);
        hamlibToProtocolMap.put("set_conf", TelemetryRotatorConstants.SET_CONFIG);
        hamlibToProtocolMap.put("get_info", TelemetryRotatorConstants.GET_INFO);
        hamlibToProtocolMap.put("send_cmd", TelemetryRotatorConstants.SEND_RAW_CMD);
        hamlibToProtocolMap.put("Lonlat2loc", TelemetryRotatorConstants.LONLAT2LOC);
        hamlibToProtocolMap.put("Loc2lonlat", TelemetryRotatorConstants.LOC2LONLAT);
        hamlibToProtocolMap.put("d_sp2d_lp", TelemetryRotatorConstants.DISTANCE_SHORTPATH2LONGPATH);
        hamlibToProtocolMap.put("Dms2dec", TelemetryRotatorConstants.DMS2DEC);
        hamlibToProtocolMap.put("Dec2dms", TelemetryRotatorConstants.DEC2DMS);
        hamlibToProtocolMap.put("Dmmm2dec", TelemetryRotatorConstants.DMMM2DEC);
        hamlibToProtocolMap.put("dec2dmmm", TelemetryRotatorConstants.DEC2DMMM);
        hamlibToProtocolMap.put("Qrv", TelemetryRotatorConstants.QRB);
    }
    
    @Override
    protected TelemetryObject createHelp(String[] messageSplit) {
        TelemetryObject telemetryObject;
        HelpCommandRotator rotatorHelp = new HelpCommandRotator();
        telemetryObject = new TelemetryObject(TelemetryRotatorConstants.HELP, new Date());
        telemetryObject = rotatorHelp.createHelpList(telemetryObject, messageSplit);
        return telemetryObject;
    }

    @Override
    protected Map<String, String> getHamlibProtocolMap() {
        return hamlibToProtocolMap;
    }
    

    protected String getDeviceName(){
        return JMSConstants.GS_ROT_CTLD;
    }

}
