package eu.estcube.common;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;

import org.apache.commons.lang3.ArrayUtils;

public class ByteUtil {

    /** Hex symbols used in HEX dump methods. */
    public static final char[] HEX_SYMBOLS = { '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' }; // sounds bit stupid but
                                                      // it's needed in fast hex
                                                      // dump methods

    public static final int CRC16_CCITT_INIT = 0xFFFF;

    /**
     * Computes CRC using 1 + x^5 + x^12 + x^16 (0x1021)
     * <p>
     * Taken from {@link http
     * ://introcs.cs.princeton.edu/java/51data/CRC16CCITT.java.html}
     * 
     * @param initialValue
     *        0xFFFF if calling for first time
     * @param bytes
     * @return 16 bit CRC-CCIIT
     */
    public static int crc16CCITT(int initialValue, byte[] bytes) {
        int crc = initialValue;
        int polynomial = 0x1021; // 0001 0000 0010 0001 (0, 5, 12)
        for (byte b : bytes) {
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit) {
                    crc ^= polynomial;
                }
            }
        }
        crc &= 0xffff;
        return crc;
    }

    /**
     * Converts 2 -> {0,1,0,0,0,0,0,0}.
     */
    public static int[] bitsLsbFirst(byte b) {
        return toBits(b, true);
    }

    /**
     * Converts 2 -> {0,0,0,0,0,0,1,0}.
     */
    public static int[] bitsMsbFirst(byte b) {
        return toBits(b, false);
    }

    /**
     * Converts the integer to binary string adding necessary padding zeros.
     * For testing purposes, performance is poor.
     * 
     * @param i The integer to convert.
     * @param bits The number of bits to be displayed.
     * @return The binary string converted.
     */
    public static String toBinaryString(int i, int bits) {
        String s = Integer.toBinaryString(i);
        while (s.length() < bits) {
            s = "0" + s;
        }
        return s;
    }

    /**
     * Converts byte to bits in the given order.
     * 
     * @param b The byte to convert.
     * @param lsbFirst If true LSB is first, otherwise MSB is first in returned
     *        array.
     */
    public static int[] toBits(byte b, boolean lsbFirst) {
        int[] bits = new int[8];
        int index;

        for (int i = 0; i < 8; i++) {
            index = (lsbFirst ? i : 8 - i - 1);
            bits[index] = (b & (1 << i)) > 0 ? 1 : 0;
        }
        return bits;
    }

    /**
     * Converts {0,1,0,0,0,0,0,0} to {0,0,0,0,0,0,1,0}
     */
    public static byte mirror(byte b) {
        byte res = 0;
        for (int i = 0; i < 8; i++) {
            // shift already mirrored bits
            res <<= 1;
            // add new bit from
            res |= b & 0x01;
            b >>= 1;
        }
        return res;
    }

    /**
     * Returns "df" for decimal 223.
     */
    public static String toHexString(byte b) {
        return new String(toHexChars(b));
    }

    public static char[] toHexChars(byte b) {
        int value = b & 0xFF; // zero out all above first 8 bits
        return new char[] {
                HEX_SYMBOLS[value >>> 4],
                HEX_SYMBOLS[value & 0x0F]
        };
    }

    /**
     * Returns "df ff" for decimal {223, 255}.
     */
    public static String toHexString(byte[] bytes) {
        return toHexString(bytes, bytes.length);
    }

    /**
     * Returns HEX string for given byte array.
     * 
     * Suitable for HEX dump.
     * Included byte count can be set using <tt>limit</tt>parameter.
     * In case limit is less than one or more than input byte array length all
     * the bytes are included to output.
     * 
     * In case limit is less than input byte array length and more than 0 some
     * of the bytes are not included in output and <tt> ...</tt> is added to the
     * end.
     * 
     * @param bytes byte array to use
     * @param limit number of bytes to include
     * @return String containing HEX representation of byte array
     */
    public static String toHexString(byte[] bytes, int limit) {
        int numberOfBytes = limit > 0 ? Math.min(limit, bytes.length) : bytes.length;
        char[] hexChars = new char[numberOfBytes * 3 - 1 + (numberOfBytes < bytes.length ? 4 : 0)];
        char[] tmp;
        for (int i = 0; i < numberOfBytes; i++) {
            tmp = toHexChars(bytes[i]);
            hexChars[i * 3] = tmp[0];
            hexChars[i * 3 + 1] = tmp[1];
            if (i < numberOfBytes - 1) {
                hexChars[i * 3 + 2] = ' ';
            }
        }
        if (numberOfBytes < bytes.length) {
            hexChars[hexChars.length - 4] = ' ';
            hexChars[hexChars.length - 3] = '.';
            hexChars[hexChars.length - 2] = '.';
            hexChars[hexChars.length - 1] = '.';
        }
        return new String(hexChars);
    }

    public static byte[] toBytesFromHexString(String hexDump) {
        String input = hexDump.replaceAll("\\s", "").toUpperCase();
        if (!input.matches("[0-9A-F]+")) {
            throw new IllegalArgumentException("Invalid symbols in hex dump");
        }
        if (input.length() % 2 != 0) {
            throw new IllegalArgumentException("Invalid number of symbols in hex dump");
        }
        return toBytesFromHexStringWithoutChecks(input);
    }

    static byte[] toBytesFromHexStringWithoutChecks(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * Converts a 4 byte array of unsigned bytes to an long
     * 
     * @param b an array of 4 unsigned bytes
     * @return a long representing the unsigned int
     */
    public static final long unsigned4bytesToLong(byte[] b) {
        if (b == null) {
            return 0;
        }
        long l = 0;
        l |= b[0] & 0xFF;
        l <<= 8;
        l |= b[1] & 0xFF;
        l <<= 8;
        l |= b[2] & 0xFF;
        l <<= 8;
        l |= b[3] & 0xFF;
        return l;
    }

    /**
     * Converts a two byte array to an integer
     * 
     * @param b a byte array of length 2
     * @return an int representing the unsigned short
     */
    public static final int unsigned2bytesToInt(byte[] b) {
        if (b == null) {
            return 0;
        }
        int i = 0;
        i |= b[0] & 0xFF;
        i <<= 8;
        i |= b[1] & 0xFF;
        return i;
    }

    /**
     * Converts the lower 22 bits of a three byte array to an integer
     * 
     * @param b a byte array of length 3
     * @return an int representing the unsigned short
     */
    public static final int unsigned22bitsToInt(byte[] b) {
        if (b == null) {
            return 0;
        }
        int i = 0;
        i |= b[0] & 0x3F;
        i <<= 8;
        i |= b[1] & 0xFF;
        i <<= 8;
        i |= b[2] & 0xFF;
        return i;
    }

    /**
     * Converts an unsigned long to an array of four unsigned bytes.
     */
    public static final byte[] longToUnsigned4bytes(long i) {
        byte[] bb = ByteBuffer.allocate(8).putLong(i).array();
        return ArrayUtils.subarray(bb, 4, 8);
    }

    /**
     * Converts an unsigned integer to an array of two unsigned bytes.
     */
    public static final byte[] intToUnsigned2bytes(int i) {
        byte[] bb = ByteBuffer.allocate(4).putInt(i).array();
        return ArrayUtils.subarray(bb, 2, 4);
    }

    /**
     * Converts an unsigned integer to an array of three unsigned bytes where
     * the
     * upper two bits are unused.
     */
    public static final byte[] intToUnsigned22bits(int i) {
        byte[] bb = ByteBuffer.allocate(4).putInt(i).array();
        bb[1] &= 0x3F;
        return ArrayUtils.subarray(bb, 1, 4);
    }

    /**
     * Helper for <code>Externalizable</code>.
     * Writes out the length followed by contents.
     */
    public static void write(byte[] buf, ObjectOutput out) throws IOException {
        if (buf == null) {
            out.writeInt(0);
        } else {
            out.writeInt(buf.length);
            out.write(buf);
        }
    }

    /**
     * Helper for <code>Externalizable</code>.
     * Reads the length followed by contents.
     */
    public static byte[] read(ObjectInput in) throws IOException {
        byte[] buf = null;
        int len = in.readInt();
        if (len > 0) {
            buf = new byte[len];
            in.read(buf);
        }
        return buf;
    }
}
