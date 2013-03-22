package eu.estcube.gsconnector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;

import eu.estcube.domain.TelemetryObject;
import eu.estcube.domain.TelemetryRadioConstants;


public class HelpCommandRadioTest {

    private HelpCommandRadio radioHelp = new HelpCommandRadio();
    
    @Test
    public void testCreateHelpList()
    {
        TelemetryObject telemetryObject = new TelemetryObject("Rotator reply", new Date());
        String[] messageSplit = {null, "F: set_freq        (Frequency)         f: get_freq        ()", 
                "get_info:?Info: None?RPRT 0"
                };
        telemetryObject = radioHelp.createHelpList(new TelemetryObject("Rotator reply", new Date()), messageSplit);
        assertEquals(TelemetryRadioConstants.SET_FREQUENCY, telemetryObject.getParams().get(0).getName());
        assertEquals("Frequency", telemetryObject.getParams().get(0).getValue());
        
        assertEquals(TelemetryRadioConstants.GET_FREQUENCY, telemetryObject.getParams().get(1).getName());
        assertEquals("", telemetryObject.getParams().get(1).getValue());
        
        assertTrue(telemetryObject.getParams().size()==2);
        
    }

}
