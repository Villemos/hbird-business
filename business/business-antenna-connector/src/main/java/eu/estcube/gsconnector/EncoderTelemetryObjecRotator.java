package eu.estcube.gsconnector;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import eu.estcube.domain.JMSConstants;
import eu.estcube.domain.TelemetryRadioConstants;
import eu.estcube.domain.TelemetryRotatorConstants;
import eu.estcube.gs.rotator.Park;
import eu.estcube.gs.rotator.Reset;
import eu.estcube.gs.rotator.SetConfig;
import eu.estcube.gs.rotator.SetPosition;
import eu.estcube.gsconnector.commands.*;
import eu.estcube.gsconnector.commands.rotator.*;

@Component(JMSConstants.GS_ROTATOR_PROTOCOL_ENCODER)
public class EncoderTelemetryObjecRotator extends EncoderTelemetryObjectAbstract {

    private Map<String, HamlibCommandBuilder> commandsHashMap;


    public EncoderTelemetryObjecRotator() {
        commandsHashMap = new HashMap<String, HamlibCommandBuilder>();
        commandsHashMap.put(TelemetryRotatorConstants.SET_POSITION, new SetPosition());
        commandsHashMap.put(TelemetryRotatorConstants.GET_POSITION, new GetPosition());
        commandsHashMap.put(TelemetryRotatorConstants.STOP, new Stop());
        commandsHashMap.put(TelemetryRotatorConstants.PARK, new Park());
        commandsHashMap.put(TelemetryRotatorConstants.MOVE, new Move());
        commandsHashMap.put(TelemetryRotatorConstants.CAPABILITIES, new Capabilities());
        commandsHashMap.put(TelemetryRotatorConstants.RESET, new Reset());
        commandsHashMap.put(TelemetryRotatorConstants.SET_CONFIG, new SetConfig());
        commandsHashMap.put(TelemetryRotatorConstants.GET_INFO, new GetInfo());
        commandsHashMap.put(TelemetryRotatorConstants.SEND_RAW_CMD, new SendRawCmd());
        commandsHashMap.put(TelemetryRotatorConstants.LONLAT2LOC, new LonLat2Loc());
        commandsHashMap.put(TelemetryRotatorConstants.LOC2LONLAT, new Loc2LonLat());
        commandsHashMap.put(TelemetryRotatorConstants.DMS2DEC, new Dms2Dec());
        commandsHashMap.put(TelemetryRotatorConstants.DEC2DMS, new Dec2Dms());
        commandsHashMap.put(TelemetryRotatorConstants.DMMM2DEC, new Dmm2Dec());
        commandsHashMap.put(TelemetryRotatorConstants.DEC2DMMM, new Dec2Dmm());
        commandsHashMap.put(TelemetryRotatorConstants.QRB, new QRB());
        commandsHashMap.put(TelemetryRotatorConstants.AZIMUTH_SHORTPATH2LONGPATH, new AzimuthShortPath2LongPath());
        commandsHashMap.put(TelemetryRotatorConstants.DISTANCE_SHORTPATH2LONGPATH, new DistanceShortPath2LongPath());
        commandsHashMap.put(TelemetryRadioConstants.HELP, new Help());

    }

    @Override
    protected Map<String, HamlibCommandBuilder> getCommandsHashMap() {
        return commandsHashMap;
    }
}
