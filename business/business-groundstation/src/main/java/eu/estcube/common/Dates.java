package eu.estcube.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Helper methods for working with {@link Date}s.
 */
public class Dates {

    /** Default date format pattern. */
    public static final String DEFAULT_DATE_FORMAT_PATTERN = "yyyy-DDD HH:mm:ss.SSS";

    /** Default time zone name. */
    public static final String DEFAULT_TIME_ZON_NAME = "UTC";

    /** Shared instance of default {@link DateFormat}. */
    private static final DateFormat DEFAULT_DATE_FORMAT = createDateFormat(DEFAULT_DATE_FORMAT_PATTERN,
            DEFAULT_TIME_ZON_NAME);

    /**
     * Creates new {@link DateFormat} instance.
     * 
     * @param patter date format pattern
     * @param timeZone time zone identifier
     */
    static DateFormat createDateFormat(String patter, String timeZone) {
        SimpleDateFormat sdf = new SimpleDateFormat(patter);
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        return sdf;
    }

    /**
     * Returns shared instance of default {@link DateFormat}.
     * 
     * @return shared instance of default {@link DateFormat}
     */
    public static DateFormat getDefaultDateFormat() {
        return DEFAULT_DATE_FORMAT;
    }
}
