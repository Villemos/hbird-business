package eu.estcube.gsconnector;

import java.nio.charset.Charset;

import junit.framework.TestCase;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.junit.Test;

import eu.estcube.domain.JMSConstants;
import eu.estcube.domain.TelemetryObject;
import eu.estcube.domain.TelemetryRadioConstants;
import eu.estcube.domain.TelemetryRotatorConstants;

public class DecoderTelemetryObjectAbstractTest extends TestCase {

    private DecoderTelemetryObjectAbstract decoder;

    @Test
    public void testTelemetryObjectDecoderRadio() throws Exception {
        decoder = new DecoderTelemetryObjectRadio();
        ChannelBuffer buffer = ChannelBuffers.wrappedBuffer(new byte[] {});

        String input = "x:\n" + "a:\tb\n" + "c:\td\n" + "e:\t\tf\n" + "o e:\t\tf\n" + "m: 1\n" + "\n"
                + JMSConstants.GS_DEVICE_END_MESSAGE + " 0\n";
        Object msg = new Object();
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

        String help = "Commands (some may not be available for this rig):\n"
                + "F: set_freq        (Frequency)         f: get_freq        ()\n"
                + "M: set_mode        (Mode,Passband)     m: get_mode        ()\n"
                + "I: set_split_freq  (TX Frequency)      i: get_split_freq  ()\n"
                + "X: set_split_mode  (TX Mode,TX Passband) x: get_split_mode  ()\n"
                + "S: set_split_vfo   (Split,TX VFO)      s: get_split_vfo   ()\n"
                + "N: set_ts          (Tuning Step)       n: get_ts          ()\n"
                + "L: set_level       (Level,Level Value) l: get_level       (Level)\n"
                + "U: set_func        (Func,Func Status)  u: get_func        (Func)\n"
                + "P: set_parm        (Parm,Parm Value)   p: get_parm        (Parm)\n"
                + "G: vfo_op          (Mem/VFO Op)        g: scan            (Scan Fct,Scan Channel)\n"
                + "A: set_trn         (Transceive)        a: get_trn         ()\n"
                + "R: set_rptr_shift  (Rptr Shift)        r: get_rptr_shift  ()\n"
                + "O: set_rptr_offs   (Rptr Offset)       o: get_rptr_offs   ()\n"
                + "C: set_ctcss_tone  (CTCSS Tone)        c: get_ctcss_tone  ()\n"
                + "D: set_dcs_code    (DCS Code)          d: get_dcs_code    ()\n"
                + "?: set_ctcss_sql   (CTCSS Sql)         ?: get_ctcss_sql   ()\n"
                + "?: set_dcs_sql     (DCS Sql)           ?: get_dcs_sql     ()\n"
                + "V: set_vfo         (VFO)               v: get_vfo         ()\n"
                + "T: set_ptt         (PTT)               t: get_ptt         ()\n"
                + "E: set_mem         (Memory#)           e: get_mem         ()\n"
                + "H: set_channel     (Channel)           h: get_channel     (Channel)\n"
                + "B: set_bank        (Bank)              _: get_info        ()\n"
                + "J: set_rit         (RIT)               j: get_rit         ()\n"
                + "Z: set_xit         (XIT)               z: get_xit         ()\n"
                + "Y: set_ant         (Antenna)           y: get_ant         ()\n"
                + "?: set_powerstat   (Power Status)      ?: get_powerstat   ()\n"
                + "?: send_dtmf       (Digits)            ?: recv_dtmf       ()\n"
                + "*: reset           (Reset)             w: send_cmd        (Cmd)\n"
                + "b: send_morse      (Morse)             ?: get_dcd         ()\n"
                + "2: power2mW        (Power [0.0..1.0],Frequency,Mode)             4: mW2power        (Power mW,Frequency,Mode)\n"
                + "1: dump_caps       ()                  3: dump_conf       ()\n"
                + "?: dump_state      ()                  ?: chk_vfo        ()\n" + "get_info:?Info: None?RPRT 0\n";

        buffer = ChannelBuffers.wrappedBuffer(help.getBytes());
        msg = ((ChannelBuffer) buffer).toString(Charset.forName(JMSConstants.ENCODING_STRING_FORMAT));
        telemetryObject = (TelemetryObject) decoder.decode(null, null, msg);
        assertTrue(telemetryObject.getParams().get(0).getName().equals(TelemetryRadioConstants.SET_FREQUENCY));
        assertTrue(telemetryObject.getParams().get(0).getValue().equals("Frequency"));
        assertTrue(telemetryObject.getParams().get(1).getName().equals(TelemetryRadioConstants.GET_FREQUENCY));
        assertTrue(telemetryObject.getParams().get(1).getValue().equals(""));
        assertTrue(telemetryObject.getParams().get(telemetryObject.getParams().size() - 1).getName()
                .equals(TelemetryRadioConstants.HELP));
        assertTrue(telemetryObject.getParams().get(telemetryObject.getParams().size() - 1).getValue().equals(""));
    }

    @Test
    public void testTelemetryObjectRotatorDecoder() throws Exception {
        decoder = new DecoderTelemetryObjectRotator();
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
