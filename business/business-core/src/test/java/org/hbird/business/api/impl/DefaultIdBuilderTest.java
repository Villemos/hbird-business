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
package org.hbird.business.api.impl;

import static org.junit.Assert.assertEquals;

import org.hbird.business.api.impl.DefaultIdBuilder;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class DefaultIdBuilderTest {

    private DefaultIdBuilder naming;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        naming = new DefaultIdBuilder();
    }

    @Test
    public void testEscape() throws Exception {
        assertEquals("", DefaultIdBuilder.escape(""));
        assertEquals(" ", DefaultIdBuilder.escape(" "));
        assertEquals("\n", DefaultIdBuilder.escape("\n"));
        assertEquals("A", DefaultIdBuilder.escape("A"));
        assertEquals("&#47;", DefaultIdBuilder.escape("/"));
        assertEquals("&#47;&#47;", DefaultIdBuilder.escape("//"));
        assertEquals("&#47;&#47;&#47;", DefaultIdBuilder.escape("///"));
        assertEquals("&#47;A", DefaultIdBuilder.escape("/A"));
        assertEquals("&#47;A&#47;B", DefaultIdBuilder.escape("/A/B"));
        assertEquals("A&#47;B", DefaultIdBuilder.escape("A/B"));
        assertEquals("A&#47;B&#47;C", DefaultIdBuilder.escape("A/B/C"));
    }

    @Test
    public void testBuildId() throws Exception {
        assertEquals("/A", naming.buildID(null, "A"));
        assertEquals("/A", naming.buildID("", "A"));
        assertEquals(" /A", naming.buildID(" ", "A"));
        assertEquals("A/B", naming.buildID("A", "B"));
        assertEquals("/A/B", naming.buildID("/A", "B"));
        assertEquals("/A/B/C", naming.buildID("/A/B", "C"));
        assertEquals("/&#47;A", naming.buildID(null, "/A"));
        assertEquals("/&#47;A", naming.buildID("", "/A"));
        assertEquals(" /&#47;A", naming.buildID(" ", "/A"));
        assertEquals("A/&#47;B", naming.buildID("A", "/B"));
        assertEquals("/A/&#47;B", naming.buildID("/A", "/B"));
        assertEquals("/A/B&#47;C", naming.buildID("/A", "B/C"));
    }
}
