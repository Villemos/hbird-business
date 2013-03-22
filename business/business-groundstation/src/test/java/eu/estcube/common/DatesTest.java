package eu.estcube.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.text.DateFormat;

import org.junit.Test;

/**
 * 
 */
public class DatesTest {

    /**
     * Test method for
     * {@link eu.estcube.common.Dates#createDateFormat(java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testCreateDateFormat() {
        DateFormat format1 = Dates.createDateFormat(Dates.DEFAULT_DATE_FORMAT_PATTERN, Dates.DEFAULT_TIME_ZON_NAME);
        DateFormat format2 = Dates.createDateFormat(Dates.DEFAULT_DATE_FORMAT_PATTERN, Dates.DEFAULT_TIME_ZON_NAME);
        assertNotNull(format1);
        assertNotNull(format2);
        assertEquals(Dates.DEFAULT_TIME_ZON_NAME, format1.getTimeZone().getID());
        assertEquals(Dates.DEFAULT_TIME_ZON_NAME, format2.getTimeZone().getID());
        assertNotSame(format1, format2);
    }

    /**
     * Test method for {@link eu.estcube.common.Dates#getDefaultDateFormat()}.
     */
    @Test
    public void testGetDefaultDateFormat() {
        DateFormat format1 = Dates.getDefaultDateFormat();
        DateFormat format2 = Dates.getDefaultDateFormat();
        assertNotNull(format1);
        assertNotNull(format2);
        assertEquals(Dates.DEFAULT_TIME_ZON_NAME, format1.getTimeZone().getID());
        assertEquals(Dates.DEFAULT_TIME_ZON_NAME, format2.getTimeZone().getID());
        assertEquals(format1, format2);
    }
}
