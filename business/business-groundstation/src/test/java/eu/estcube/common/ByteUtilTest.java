package eu.estcube.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

public class ByteUtilTest {

    @Test
    public void testToBits() {
        int[] r;

        r = ByteUtil.toBits((byte) 2, true);
        assertEquals(0, r[0]);
        assertEquals(1, r[1]);
        assertEquals(0, r[2]);
        assertEquals(0, r[3]);
        assertEquals(0, r[4]);
        assertEquals(0, r[5]);
        assertEquals(0, r[6]);
        assertEquals(0, r[7]);

        r = ByteUtil.toBits((byte) 2, false);
        assertEquals(0, r[0]);
        assertEquals(0, r[1]);
        assertEquals(0, r[2]);
        assertEquals(0, r[3]);
        assertEquals(0, r[4]);
        assertEquals(0, r[5]);
        assertEquals(1, r[6]);
        assertEquals(0, r[7]);

        r = ByteUtil.toBits((byte) 0xFF, true);
        assertEquals(1, r[0]);
        assertEquals(1, r[1]);
        assertEquals(1, r[2]);
        assertEquals(1, r[3]);
        assertEquals(1, r[4]);
        assertEquals(1, r[5]);
        assertEquals(1, r[6]);
        assertEquals(1, r[7]);

        r = ByteUtil.toBits((byte) 0xFF, false);
        assertEquals(1, r[0]);
        assertEquals(1, r[1]);
        assertEquals(1, r[2]);
        assertEquals(1, r[3]);
        assertEquals(1, r[4]);
        assertEquals(1, r[5]);
        assertEquals(1, r[6]);
        assertEquals(1, r[7]);

        r = ByteUtil.toBits((byte) 0xBB, true);
        assertEquals(1, r[0]);
        assertEquals(1, r[1]);
        assertEquals(0, r[2]);
        assertEquals(1, r[3]);
        assertEquals(1, r[4]);
        assertEquals(1, r[5]);
        assertEquals(0, r[6]);
        assertEquals(1, r[7]);

        r = ByteUtil.toBits((byte) 0xBB, false);
        assertEquals(1, r[0]);
        assertEquals(0, r[1]);
        assertEquals(1, r[2]);
        assertEquals(1, r[3]);
        assertEquals(1, r[4]);
        assertEquals(0, r[5]);
        assertEquals(1, r[6]);
        assertEquals(1, r[7]);

    }

    @Test
    public void testBitsLsbFirst() {
        int[] r;

        r = ByteUtil.bitsLsbFirst((byte) 2);
        assertEquals(0, r[0]);
        assertEquals(1, r[1]);
        assertEquals(0, r[2]);
        assertEquals(0, r[3]);
        assertEquals(0, r[4]);
        assertEquals(0, r[5]);
        assertEquals(0, r[6]);
        assertEquals(0, r[7]);
    }

    @Test
    public void testBitsMsbFirst() {
        int[] r;

        r = ByteUtil.bitsMsbFirst((byte) 2);
        assertEquals(0, r[0]);
        assertEquals(0, r[1]);
        assertEquals(0, r[2]);
        assertEquals(0, r[3]);
        assertEquals(0, r[4]);
        assertEquals(0, r[5]);
        assertEquals(1, r[6]);
        assertEquals(0, r[7]);
    }

    @Test
    public void testToHexStringDump() {
        byte[] bytes = new byte[] { 0x0D, 0x0E, 0x0A, 0x0D, 0x0C, 0x00, 0x0D, 0x0E };
        assertEquals("0D 0E 0A 0D 0C 00 0D 0E", ByteUtil.toHexString(bytes));
        assertEquals("0D 0E 0A 0D 0C 00 0D 0E", ByteUtil.toHexString(bytes, 8));
        assertEquals("0D 0E 0A 0D 0C 00 0D ...", ByteUtil.toHexString(bytes, 7));
        assertEquals("0D 0E 0A 0D 0C 00 ...", ByteUtil.toHexString(bytes, 6));
        assertEquals("0D 0E 0A 0D 0C ...", ByteUtil.toHexString(bytes, 5));
        assertEquals("0D 0E 0A 0D ...", ByteUtil.toHexString(bytes, 4));
        assertEquals("0D 0E 0A ...", ByteUtil.toHexString(bytes, 3));
        assertEquals("0D 0E ...", ByteUtil.toHexString(bytes, 2));
        assertEquals("0D ...", ByteUtil.toHexString(bytes, 1));
        assertEquals("0D 0E 0A 0D 0C 00 0D 0E", ByteUtil.toHexString(bytes, 0));
        assertEquals("0D 0E 0A 0D 0C 00 0D 0E", ByteUtil.toHexString(bytes, -1));
        assertEquals("0D 0E 0A 0D 0C 00 0D 0E", ByteUtil.toHexString(bytes, 9));
    }

    @Test
    public void testToHexStringDumpByte() {
        for (byte b = Byte.MIN_VALUE; b < Byte.MAX_VALUE; b++) {
            String hex = ByteUtil.toHexString(b);
            String expected = Integer.toHexString(b & 0xFF).toUpperCase();
            if (expected.length() == 1) {
                expected = "0" + expected;
            }
            assertEquals(expected, hex);
        }
    }

    @Test
    public void testToHexStringDumpChars() {
        for (byte b = Byte.MIN_VALUE; b < Byte.MAX_VALUE; b++) {
            char[] hex = ByteUtil.toHexChars(b);
            String expected = Integer.toHexString(b & 0xFF).toUpperCase();
            if (expected.length() == 1) {
                expected = "0" + expected;
            }
            assertEquals(2, hex.length);
            assertEquals(expected, new String(hex));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBytesfromHexStringInvalidSymbols() {
        ByteUtil.toBytesFromHexString("0G");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBytesfromHexStringInvalidNumberOfSymbols() {
        ByteUtil.toBytesFromHexString("0C 1");
    }

    @Test
    public void testToBytesfromHex() {
        byte[] bytes = { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F };
        assertTrue(Arrays.equals(bytes,
                ByteUtil.toBytesFromHexString("00 01 02 03 04 05 06 07 08 09 0A 0B 0C 0D 0E 0F")));
        assertTrue(Arrays.equals(bytes,
                ByteUtil.toBytesFromHexString("00 01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e 0f")));
        assertTrue(Arrays.equals(bytes,
                ByteUtil.toBytesFromHexString("00 01 02 03 04 05 06 07 08 09 0A 0b 0C 0d 0E 0f")));
        assertTrue(Arrays.equals(bytes, ByteUtil.toBytesFromHexString("000102030405060708090A0B0C0D0E0F")));
        assertTrue(Arrays.equals(bytes, ByteUtil
                .toBytesFromHexString("00 01\t02\n03\t04\r 05\n06070809\t\t0A\n\r0B\n0C\r\n\r \n0D0E\t\t\t\t0F")));
    }

    @Test
    public void testToBytesFromHexStringWithoutChecks() {
        byte[] bytes = { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F };
        assertTrue(Arrays.equals(bytes,
                ByteUtil.toBytesFromHexString("000102030405060708090A0B0C0D0E0F")));
    }

    @Test
    public void testToHexToBytes() {
        Random random = new Random();
        for (int i = 0; i < 1024; i++) {
            byte[] bytes = new byte[1024];
            random.nextBytes(bytes);
            String hex = ByteUtil.toHexString(bytes);
            byte[] result = ByteUtil.toBytesFromHexString(hex);
            assertTrue(Arrays.equals(bytes, result));
        }
    }
}
