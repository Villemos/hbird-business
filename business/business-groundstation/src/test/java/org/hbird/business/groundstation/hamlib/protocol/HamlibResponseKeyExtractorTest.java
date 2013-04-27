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
package org.hbird.business.groundstation.hamlib.protocol;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class HamlibResponseKeyExtractorTest {

    private HamlibResponseKeyExtractor ex;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        ex = new HamlibResponseKeyExtractor();
    }

    @Test
    public void testGetKey() throws Exception {
        assertEquals("a", ex.getKey("a"));
        assertEquals("abc", ex.getKey("abc"));
        assertEquals("a", ex.getKey("a:"));
        assertEquals("abc", ex.getKey("abc:"));
        assertEquals("abc", ex.getKey("abc:def"));
        assertEquals("abc", ex.getKey("abc::"));
        assertEquals("abc", ex.getKey("abc:def:ghi"));
        assertEquals("", ex.getKey(":"));
        assertEquals("", ex.getKey("\nb:1"));
        assertEquals("", ex.getKey(":\nb:1"));
        assertEquals("a", ex.getKey("a:\nb:1"));
        assertEquals("abc", ex.getKey("abc:\nb:1"));
        assertEquals("abc", ex.getKey("abc:def\nb:1"));
        assertEquals("abc", ex.getKey("abc\ndef:ghi"));
    }

    @Test(expected = NullPointerException.class)
    public void testGetKeyFromNull() {
        ex.getKey(null);
    }
}
