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
/**
 * 
 */
package org.hbird.exchange.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class PartTest {

    private static final String ROOT_1 = "A";
    private static final String ROOT_2 = "B";
    private static final String CHILD_1 = "C";
    private static final String CHILD_2 = "D";
    private static final String DESCRIPTION = "description";
    private static final List<Command> COMMANDS = new ArrayList<Command>();
    private static final String SEPARATOR = "@";

    private Part root1;
    private Part root2;
    private Part root1Child1;
    private Part root1Child2;
    private Part root2Child1;
    private Part root2Child2;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        root1 = new Part(ROOT_1, ROOT_1, DESCRIPTION);
        root2 = new Part(ROOT_2, ROOT_2, DESCRIPTION);
        root1Child1 = new Part(CHILD_1, CHILD_1, DESCRIPTION, root1);
        root1Child2 = new Part(CHILD_2, CHILD_2, DESCRIPTION, root1);
        root2Child1 = new Part(CHILD_1, CHILD_1, DESCRIPTION, root2);
        root2Child2 = new Part(CHILD_2, CHILD_2, DESCRIPTION, root2);
        COMMANDS.clear();
    }

    /**
     * Test method for {@link org.hbird.exchange.core.Part#getName()}.
     */
    @Test
    public void testgetName() {
        assertEquals(ROOT_1, root1.getName());
        assertEquals(ROOT_2, root2.getName());
        assertEquals(CHILD_1, root1Child1.getName());
        assertEquals(CHILD_2, root1Child2.getName());
        assertEquals(CHILD_1, root2Child1.getName());
        assertEquals(CHILD_2, root2Child2.getName());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.Part#getName(java.lang.String)}.
     */
    @Test
    public void testgetNameString() {
        assertEquals(ROOT_1, root1.getName());
        assertEquals(ROOT_2, root2.getName());
        assertEquals(CHILD_1, root1Child1.getName());
        assertEquals(CHILD_2, root1Child2.getName());
        assertEquals(CHILD_1, root2Child1.getName());
        assertEquals(CHILD_2, root2Child2.getName());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.Part#Part(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testPartStringString() {
        Part part = new Part(ROOT_1, ROOT_1, DESCRIPTION);
        assertEquals(ROOT_1, part.getName());
        assertEquals(DESCRIPTION, part.getDescription());
        assertNull(part.getIsPartOf());
        assertEquals(ROOT_1, part.getName());
        assertEquals(ROOT_1, part.getName());
        assertNotNull(part.getID());
    }

    /**
     * Test method for
     * {@link org.hbird.exchange.core.Part#Part(java.lang.String, java.lang.String, org.hbird.exchange.interfaces.IPart)}
     * .
     */
    @Test
    public void testPartStringStringIPart() {
        assertEquals(CHILD_1, root1Child1.getName());
        assertEquals(DESCRIPTION, root1Child1.getDescription());
        assertEquals(root1.getID(), root1Child1.getIsPartOf());
        assertEquals(CHILD_1, root1Child1.getName());
        assertEquals(CHILD_2, root1Child2.getName());
        assertNotNull(root1Child1.getID());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.Part#Part(java.lang.String, java.lang.String, java.util.List)}.
     */
    @Test
    public void testPartStringStringListOfCommand() {
        Part part = new Part(ROOT_1, ROOT_1, DESCRIPTION);
        assertEquals(ROOT_1, part.getName());
        assertEquals(DESCRIPTION, part.getDescription());
        assertNull(part.getIsPartOf());
        assertEquals(ROOT_1, part.getName());
        assertEquals(ROOT_1, part.getName());
        assertNotNull(part.getID());
    }

    /**
     * Test method for
     * {@link org.hbird.exchange.core.Part#Part(java.lang.String, java.lang.String, org.hbird.exchange.interfaces.IPart, java.util.List)}
     * .
     */
    @Test
    public void testPartStringStringIPartListOfCommand() {
        Part part = new Part(CHILD_1, CHILD_1, DESCRIPTION, root1);
        assertEquals(CHILD_1, part.getName());
        assertEquals(DESCRIPTION, part.getDescription());
        assertEquals(root1.getID(), part.getIsPartOf());
        assertEquals(CHILD_1, part.getName());
        assertNotNull(part.getID());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.Part#getIsPartOf()}.
     */
    @Test
    public void testGetIsPartOf() {
        assertNull(root1.getIsPartOf());
        assertNull(root2.getIsPartOf());
        assertEquals(root1.getID(), root1Child1.getIsPartOf());
        assertEquals(root1.getID(), root1Child2.getIsPartOf());
        assertEquals(root2.getID(), root2Child1.getIsPartOf());
        assertEquals(root2.getID(), root2Child2.getIsPartOf());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.Part#setIsPartOf(org.hbird.exchange.interfaces.IPart)}.
     */
    @Test
    public void testSetIsPartOf() {
        assertEquals(root1.getID(), root1Child1.getIsPartOf());
        root1Child1.setIsPartOf("name:type:timestamp");
        assertEquals("name:type:timestamp", root1Child1.getIsPartOf());
        root1Child1.setIsPartOf(root2);
        assertEquals(root2.getID(), root1Child1.getIsPartOf());
    }

}
