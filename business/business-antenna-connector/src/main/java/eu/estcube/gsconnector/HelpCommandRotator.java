package eu.estcube.gsconnector;

import java.util.HashMap;
import java.util.Map;

import eu.estcube.domain.TelemetryRotatorConstants;

public class HelpCommandRotator extends HelpCommandAbstract{

    private Map<String, String> helpHashMap;

    public HelpCommandRotator() {
        helpHashMap = new HashMap<String, String>();
        helpHashMap.put("P", TelemetryRotatorConstants.SET_POSITION);
        helpHashMap.put("p", TelemetryRotatorConstants.GET_POSITION);
        helpHashMap.put("K", TelemetryRotatorConstants.PARK);
        helpHashMap.put("S", TelemetryRotatorConstants.STOP);
        helpHashMap.put("M", TelemetryRotatorConstants.MOVE);
        helpHashMap.put("1", TelemetryRotatorConstants.CAPABILITIES);
        helpHashMap.put("R", TelemetryRotatorConstants.RESET);
        helpHashMap.put("C", TelemetryRotatorConstants.SET_CONFIG);
        helpHashMap.put("_", TelemetryRotatorConstants.GET_INFO);
        helpHashMap.put("w", TelemetryRotatorConstants.SEND_RAW_CMD);
        helpHashMap.put("L", TelemetryRotatorConstants.LONLAT2LOC);
        helpHashMap.put("l", TelemetryRotatorConstants.LOC2LONLAT);
        helpHashMap.put("D", TelemetryRotatorConstants.DMS2DEC);
        helpHashMap.put("d", TelemetryRotatorConstants.DEC2DMS);
        helpHashMap.put("E", TelemetryRotatorConstants.DMMM2DEC);
        helpHashMap.put("e", TelemetryRotatorConstants.DEC2DMMM);
        helpHashMap.put("B", TelemetryRotatorConstants.QRB);
        helpHashMap.put("A", TelemetryRotatorConstants.AZIMUTH_SHORTPATH2LONGPATH);
        helpHashMap.put("a", TelemetryRotatorConstants.DISTANCE_SHORTPATH2LONGPATH);
        helpHashMap.put("?", TelemetryRotatorConstants.HELP);
    }

    @Override
    protected Map<String, String> getHelpHashMap() {
        return helpHashMap;
    }
}
