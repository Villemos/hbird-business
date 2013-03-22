package eu.estcube.gsconnector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;

import eu.estcube.domain.TelemetryObject;
import eu.estcube.domain.TelemetryRotatorConstants;


public class HelpCommandRotatorTest {

    private HelpCommandRotator rotatorHelp = new HelpCommandRotator();
    
    @Test
    public void testCreateHelpList()
    {
        TelemetryObject telemetryObject = new TelemetryObject("Rotator reply", new Date());
        String[] messageSplit = {null, "P: set_pos     (Azimuth, Elevation)", 
                "p: get_pos     ()", 
                "get_info:?Info: None?RPRT 0"
                };
        telemetryObject = rotatorHelp.createHelpList(new TelemetryObject("Rotator reply", new Date()), messageSplit);
        assertEquals(TelemetryRotatorConstants.SET_POSITION, telemetryObject.getParams().get(0).getName());
        assertEquals("Azimuth, Elevation", telemetryObject.getParams().get(0).getValue());
        
        assertEquals(TelemetryRotatorConstants.GET_POSITION, telemetryObject.getParams().get(1).getName());
        assertEquals("", telemetryObject.getParams().get(1).getValue());
        
        assertTrue(telemetryObject.getParams().size()==2);
        
    }

}
