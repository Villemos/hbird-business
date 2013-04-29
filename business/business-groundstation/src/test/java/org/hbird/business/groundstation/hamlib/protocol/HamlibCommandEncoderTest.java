package org.hbird.business.groundstation.hamlib.protocol;

import java.nio.charset.Charset;
import java.util.Arrays;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HamlibCommandEncoderTest extends TestCase {

    private HamlibCommandEncoder encoder;

    @Override
    @Before
    public void setUp() throws Exception {
        encoder = new HamlibCommandEncoder();
    }

    @Test
    public void testToBytesNull() {
        assertNull(encoder.toBytes(null));
    }

    @Test
    public void testToBytesBlankString() {
        assertNull(encoder.toBytes(""));
        assertNull(encoder.toBytes(" "));
        assertNull(encoder.toBytes("\n"));
        assertNull(encoder.toBytes("\t"));
        assertNull(encoder.toBytes("\r"));
        assertNull(encoder.toBytes("  "));
        assertNull(encoder.toBytes("\n\n"));
        assertNull(encoder.toBytes("\t\t"));
        assertNull(encoder.toBytes("\t \n"));
        assertNull(encoder.toBytes("\n \t  \t  \n\n\r"));
    }

    @Test
    public void testToBytes() {
        String[][] data = {
                { "a", "a\n" },
                { "a\n", "a\n" },
                { "+p 23.12 334.23", "+p 23.12 334.23\n" },
                { "+p 23.12 334.23\n", "+p 23.12 334.23\n" },
                { "+p 23.12 334.23\n ", "+p 23.12 334.23\n \n" },
                { "+p 23.12 334.23\n \n", "+p 23.12 334.23\n \n" },
                { "1", "1\n" },
                { "1\n", "1\n" },
                { "1\\n", "1\\n\n" },
                { "11\n11", "11\n11\n" },
                { "11\n11\n", "11\n11\n" },
        };

        for (String[] pair : data) {
            byte[] expected = pair[1].getBytes(Charset.forName(HamlibProtocolConstants.STRING_ENCODING));
            byte[] actual = encoder.toBytes(pair[0]);
            assertTrue("Failed on " + pair[0], Arrays.equals(expected, actual));
        }
    }

    @Test
    public void testToBytesWithObject() {
        Object obj = new Object();
        String str = obj.toString() + "\n";
        assertTrue(Arrays.equals(str.getBytes(Charset.forName(HamlibProtocolConstants.STRING_ENCODING)), encoder.toBytes(obj)));
    }
}
