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

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.hbird.exchange.util.Dates;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 */
public class DatesTest {

    private static final String DATE_AS_STRING = "20130319230948211";

    private static Date date;

    @BeforeClass
    public static void setupBeforeClass() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        date = sdf.parse(DATE_AS_STRING);
    }

    @Test
    public void testToDefaultDateFormat() {
        assertEquals("2013-078 23:09:48.211", Dates.toDefaultDateFormat(date.getTime()));
    }

    @Test
    public void testToIso8601DateFormat() {
        assertEquals("2013-03-19T23:09:48.211Z", Dates.toIso8601DateFormat(date.getTime()));
    }

    @Test
    public void testToIso8601BasicDateFormat() {
        assertEquals("20130319T230948.211Z", Dates.toIso8601BasicDateFormat(date.getTime()));
    }

    @Test
    public void testToDateInFileNameFormat() {
        assertEquals("2013-03-19T230948-211", Dates.toDateInFileNameFormat(date.getTime()));
    }
}
