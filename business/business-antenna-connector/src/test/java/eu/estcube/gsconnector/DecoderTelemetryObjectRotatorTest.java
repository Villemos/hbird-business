package eu.estcube.gsconnector;

import java.nio.charset.Charset;

import junit.framework.TestCase;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.junit.Before;
import org.junit.Test;

import eu.estcube.domain.JMSConstants;
import eu.estcube.domain.TelemetryObject;
import eu.estcube.domain.TelemetryRotatorConstants;

public class DecoderTelemetryObjectRotatorTest extends TestCase {

    private DecoderTelemetryObjectRotator decoder;

    @Before
    public void setUp() throws Exception {
        decoder = new DecoderTelemetryObjectRotator();
    }

    @Test
    public void testTelemetryObjectDecoder() throws Exception {
        ChannelBuffer buffer = ChannelBuffers.wrappedBuffer(new byte[] {});

        Object msg = new Object();
        String input = "x:\n" + "a:\tb\n" + "c:\td\n" + "e:\t\tf\n" + "o e:\t\tf\n" + "m: 1\n" + "\n"
                + JMSConstants.GS_DEVICE_END_MESSAGE + " 0\n";
        buffer = ChannelBuffers.wrappedBuffer(input.getBytes());
        msg = ((ChannelBuffer) buffer).toString(Charset.forName(JMSConstants.ENCODING_STRING_FORMAT));
        TelemetryObject telemetryObject = (TelemetryObject) decoder.decode(null, null, msg);

        assertTrue(telemetryObject.getParams().get(0).getName().equals("x"));
        assertTrue(telemetryObject.getParams().get(0).getValue().equals(""));
        assertTrue(telemetryObject.getParams().get(1).getName().equals("a"));
        assertTrue(telemetryObject.getParams().get(1).getValue().equals("b"));
        assertTrue(telemetryObject.getParams().get(2).getName().equals("c"));
        assertTrue(telemetryObject.getParams().get(2).getValue().equals("d"));
        assertTrue(telemetryObject.getParams().get(3).getName().equals("e"));
        assertTrue(telemetryObject.getParams().get(3).getValue().equals("f"));
        assertTrue(telemetryObject.getParams().get(4).getName().equals("o e"));
        assertTrue(telemetryObject.getParams().get(4).getValue().equals("f"));
        assertTrue(telemetryObject.getParams().get(5).getName().equals("m"));
        assertTrue(telemetryObject.getParams().get(5).getValue().equals("1"));

        String help = "Commands (some may not be available for this rig):\n" + "P: set_pos     (Azimuth, Elevation)\n"
                + "p: get_pos     ()\n" + "K: park        ()\n" + "S: stop        ()\n" + "R: reset       (Reset)\n"
                + "M: move        (Direction, Speed)\n" + "C: set_conf    (Token, Value)\n" + "_: get_info    ()\n"
                + "w: send_cmd    (Cmd)\n" + "1: dump_caps   ()\n" + "?: dump_state  ()\n"
                + "L: lonlat2loc  (Longitude, Latitude, Loc Len [2-12])\n" + "l: loc2lonlat  (Locator)\n"
                + "D: dms2dec     (Degrees, Minutes, Seconds, S/W)\n" + "d: dec2dms     (Dec Degrees)\n"
                + "E: dmmm2dec    (Degrees, Dec Minutes, S/W)\n" + "e: dec2dmmm    (Dec Deg)\n"
                + "B: qrb         (Lon 1, Lat 1, Lon 2, Lat 2)\n" + "A: a_sp2a_lp   (Short Path Deg)\n"
                + "a: d_sp2d_lp   (Short Path km)\n" + "get_info:?Info: None?RPRT 0\n";
        buffer = ChannelBuffers.wrappedBuffer(help.getBytes());
        msg = ((ChannelBuffer) buffer).toString(Charset.forName(JMSConstants.ENCODING_STRING_FORMAT));
        telemetryObject = (TelemetryObject) decoder.decode(null, null, msg);
        assertTrue(telemetryObject.getParams().get(0).getName().equals(TelemetryRotatorConstants.SET_POSITION));
        assertTrue(telemetryObject.getParams().get(0).getValue().equals("Azimuth, Elevation"));
        assertTrue(telemetryObject.getParams().get(telemetryObject.getParams().size() - 1).getName()
                .equals(TelemetryRotatorConstants.DISTANCE_SHORTPATH2LONGPATH));
        assertTrue(telemetryObject.getParams().get(telemetryObject.getParams().size() - 1).getValue()
                .equals("Short Path km"));

    }
}
