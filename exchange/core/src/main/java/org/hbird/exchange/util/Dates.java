/**
 * Licensed to the Hummingbird Foundation (HF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The HF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hbird.exchange.util;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Utility class for working with the dates.
 * 
 * Uses Joda time library.
 * 
 */
public class Dates {

    /** Default date format pattern. */
    public static final String DEFAULT_DATE_PATTERN = "yyyy-DDD HH:mm:ss.SSS";

    /** Date in file name format pattern. */
    public static final String DATE_IN_FILE_NAME_PATTERN = "yyyy-MM-dd'T'HHmmss-SSS";

    /**
     * {@link DateTimeFormatter} for default date format.
     * 
     * Output format is <tt>yyyy-DDD HH:mm:ss.SSS</tt>.
     * 
     * @see #DEFAULT_DATE_PATTERN
     */
    public static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormat.forPattern(DEFAULT_DATE_PATTERN)
            .withZoneUTC();

    /**
     * {@link DateTimeFormatter} for ISO 8601 date format.
     * 
     * Output format is <tt>yyyy-MM-dd'T'HH:mm:ss.SSSZ</tt>.
     */
    public static final DateTimeFormatter ISO_8601_DATE_FORMATTER = ISODateTimeFormat.dateTime().withZoneUTC();

    /**
     * {@link DateTimeFormatter} for ISO 8601 basic date format.
     * 
     * Output format is <tt>yyyyMMdd'T'HHmmss.SSSZ</tt>.
     */
    public static final DateTimeFormatter ISO_8601_BASIC_DATE_FORMATTER = ISODateTimeFormat.basicDateTime()
            .withZoneUTC();

    /**
     * {@link DateTimeFormatter} for dates in file names.
     * 
     * Output format is <tt>yyyy-MM-dd'T'HHmmss-SSS</tt>.
     * 
     * @see #DATE_IN_FILE_NAME_PATTERN
     */
    public static final DateTimeFormatter DATE_IN_FILE_NAME_FORMATTER = DateTimeFormat.forPattern(
            DATE_IN_FILE_NAME_PATTERN).withZoneUTC();

    /**
     * Formats given time stamp using default date formatter and UTC time zone.
     * 
     * @param timestamp time stamp to format
     * @return time stamp formatted with default date formatter and UTC time
     *         zone
     * @see #DEFAULT_DATE_FORMATTER
     */
    public static String toDefaultDateFormat(long timestamp) {
        return DEFAULT_DATE_FORMATTER.print(timestamp);
    }

    /**
     * Formats given time stamp using ISO 8601 date formatter and UTC time zone.
     * 
     * @param timestamp time stamp to format
     * @return time stamp formatted with ISO 8601 date formatter and UTC time
     *         zone
     * @see #ISO_8601_DATE_FORMATTER
     */
    public static String toIso8601DateFormat(long timestamp) {
        return ISO_8601_DATE_FORMATTER.print(timestamp);
    }

    /**
     * Formats given time stamp using ISO 8601 basic date formatter and UTC time
     * zone.
     * 
     * @param timestamp time stamp to format
     * @return time stamp formatted with ISO 8601 basic date formatter and UTC
     *         time zone
     * @see #ISO_8601_BASIC_DATE_FORMATTER
     */
    public static String toIso8601BasicDateFormat(long timestamp) {
        return ISO_8601_BASIC_DATE_FORMATTER.print(timestamp);
    }

    /**
     * Formats given time stamp using date in file name formatter and UTC time
     * zone.
     * 
     * Output of this method can be used in file names.
     * 
     * @param timestamp time stamp to format
     * @return time stamp formatted with date in file name formatter and UTC
     *         time zone
     * @see #DATE_IN_FILE_NAME_FORMATTER
     */
    public static String toDateInFileNameFormat(long timestamp) {
        return DATE_IN_FILE_NAME_FORMATTER.print(timestamp);
    }
}
