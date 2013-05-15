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
package org.hbird.business.core.naming;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class DefaultNamingTest {

    private DefaultNaming naming;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        naming = new DefaultNaming();
    }

    @Test
    public void testEscape() throws Exception {
        assertEquals("", DefaultNaming.escape(""));
        assertEquals(" ", DefaultNaming.escape(" "));
        assertEquals("\n", DefaultNaming.escape("\n"));
        assertEquals("A", DefaultNaming.escape("A"));
        assertEquals("&#47;", DefaultNaming.escape("/"));
        assertEquals("&#47;&#47;", DefaultNaming.escape("//"));
        assertEquals("&#47;&#47;&#47;", DefaultNaming.escape("///"));
        assertEquals("&#47;A", DefaultNaming.escape("/A"));
        assertEquals("&#47;A&#47;B", DefaultNaming.escape("/A/B"));
        assertEquals("A&#47;B", DefaultNaming.escape("A/B"));
        assertEquals("A&#47;B&#47;C", DefaultNaming.escape("A/B/C"));
    }

    @Test
    public void testBuildId() throws Exception {
        assertEquals("/A", naming.buildId(null, "A"));
        assertEquals("/A", naming.buildId("", "A"));
        assertEquals(" /A", naming.buildId(" ", "A"));
        assertEquals("A/B", naming.buildId("A", "B"));
        assertEquals("/A/B", naming.buildId("/A", "B"));
        assertEquals("/A/B/C", naming.buildId("/A/B", "C"));
        assertEquals("/&#47;A", naming.buildId(null, "/A"));
        assertEquals("/&#47;A", naming.buildId("", "/A"));
        assertEquals(" /&#47;A", naming.buildId(" ", "/A"));
        assertEquals("A/&#47;B", naming.buildId("A", "/B"));
        assertEquals("/A/&#47;B", naming.buildId("/A", "/B"));
        assertEquals("/A/B&#47;C", naming.buildId("/A", "B/C"));
    }
}
