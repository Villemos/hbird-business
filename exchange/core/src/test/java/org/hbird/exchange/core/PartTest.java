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
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.hbird.exchange.constants.StandardComponents;
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
        root1 = new Part(ROOT_1, DESCRIPTION);
        root2 = new Part(ROOT_2, DESCRIPTION);
        root1Child1 = new Part(CHILD_1, DESCRIPTION, root1);
        root1Child2 = new Part(CHILD_2, DESCRIPTION, root1);
        root2Child1 = new Part(CHILD_1, DESCRIPTION, root2);
        root2Child2 = new Part(CHILD_2, DESCRIPTION, root2);
        COMMANDS.clear();
    }

    /**
     * Test method for {@link org.hbird.exchange.core.Part#getQualifiedName()}.
     */
    @Test
    public void testGetQualifiedName() {
        assertEquals(Named.DEFAULT_QUALIFIED_NAME_SEPARATOR + ROOT_1, root1.getQualifiedName());
        assertEquals(Named.DEFAULT_QUALIFIED_NAME_SEPARATOR + ROOT_2, root2.getQualifiedName());
        assertEquals(Named.DEFAULT_QUALIFIED_NAME_SEPARATOR + ROOT_1 + Named.DEFAULT_QUALIFIED_NAME_SEPARATOR + CHILD_1, root1Child1.getQualifiedName());
        assertEquals(Named.DEFAULT_QUALIFIED_NAME_SEPARATOR + ROOT_1 + Named.DEFAULT_QUALIFIED_NAME_SEPARATOR + CHILD_2, root1Child2.getQualifiedName());
        assertEquals(Named.DEFAULT_QUALIFIED_NAME_SEPARATOR + ROOT_2 + Named.DEFAULT_QUALIFIED_NAME_SEPARATOR + CHILD_1, root2Child1.getQualifiedName());
        assertEquals(Named.DEFAULT_QUALIFIED_NAME_SEPARATOR + ROOT_2 + Named.DEFAULT_QUALIFIED_NAME_SEPARATOR + CHILD_2, root2Child2.getQualifiedName());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.Part#getQualifiedName(java.lang.String)}.
     */
    @Test
    public void testGetQualifiedNameString() {
        assertEquals(SEPARATOR + ROOT_1, root1.getQualifiedName(SEPARATOR));
        assertEquals(SEPARATOR + ROOT_2, root2.getQualifiedName(SEPARATOR));
        assertEquals(SEPARATOR + ROOT_1 + SEPARATOR + CHILD_1, root1Child1.getQualifiedName(SEPARATOR));
        assertEquals(SEPARATOR + ROOT_1 + SEPARATOR + CHILD_2, root1Child2.getQualifiedName(SEPARATOR));
        assertEquals(SEPARATOR + ROOT_2 + SEPARATOR + CHILD_1, root2Child1.getQualifiedName(SEPARATOR));
        assertEquals(SEPARATOR + ROOT_2 + SEPARATOR + CHILD_2, root2Child2.getQualifiedName(SEPARATOR));
    }

    /**
     * Test method for {@link org.hbird.exchange.core.Part#Part(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testPartStringString() {
        Part part = new Part(ROOT_1, DESCRIPTION);
        assertEquals(ROOT_1, part.getName());
        assertEquals(DESCRIPTION, part.getDescription());
        assertNotNull(part.getCommands());
        assertEquals(0, part.getCommands().size());
        assertNull(part.getIsPartOf());
        assertEquals(StandardComponents.SYSTEM, part.getIssuedBy());
        assertEquals(Named.DEFAULT_QUALIFIED_NAME_SEPARATOR + ROOT_1, part.getQualifiedName());
        assertEquals(SEPARATOR + ROOT_1, part.getQualifiedName(SEPARATOR));
        assertTrue(part.getTimestamp() <= System.currentTimeMillis() && part.getTimestamp() > System.currentTimeMillis() - 3 * 1000L);
        assertEquals(Part.class.getSimpleName(), part.getType());
        assertNotNull(part.getUuid());
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
        assertNotNull(root1Child1.getCommands());
        assertEquals(0, root1Child1.getCommands().size());
        assertEquals(root1, root1Child1.getIsPartOf());
        assertEquals(StandardComponents.SYSTEM, root1Child1.getIssuedBy());
        assertEquals(Named.DEFAULT_QUALIFIED_NAME_SEPARATOR + ROOT_1 + Named.DEFAULT_QUALIFIED_NAME_SEPARATOR + CHILD_1, root1Child1.getQualifiedName());
        assertEquals(SEPARATOR + ROOT_1 + SEPARATOR + CHILD_1, root1Child1.getQualifiedName(SEPARATOR));
        assertTrue(root1Child1.getTimestamp() <= System.currentTimeMillis() && root1Child1.getTimestamp() > System.currentTimeMillis() - 3 * 1000L);
        assertEquals(Part.class.getSimpleName(), root1Child1.getType());
        assertNotNull(root1Child1.getUuid());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.Part#Part(java.lang.String, java.lang.String, java.util.List)}.
     */
    @Test
    public void testPartStringStringListOfCommand() {
        Part part = new Part(ROOT_1, DESCRIPTION, COMMANDS);
        assertEquals(ROOT_1, part.getName());
        assertEquals(DESCRIPTION, part.getDescription());
        assertEquals(COMMANDS, part.getCommands());
        assertNull(part.getIsPartOf());
        assertEquals(StandardComponents.SYSTEM, part.getIssuedBy());
        assertEquals(Named.DEFAULT_QUALIFIED_NAME_SEPARATOR + ROOT_1, part.getQualifiedName());
        assertEquals(SEPARATOR + ROOT_1, part.getQualifiedName(SEPARATOR));
        assertTrue(part.getTimestamp() <= System.currentTimeMillis() && part.getTimestamp() > System.currentTimeMillis() - 3 * 1000L);
        assertEquals(Part.class.getSimpleName(), part.getType());
        assertNotNull(part.getUuid());
    }

    /**
     * Test method for
     * {@link org.hbird.exchange.core.Part#Part(java.lang.String, java.lang.String, org.hbird.exchange.interfaces.IPart, java.util.List)}
     * .
     */
    @Test
    public void testPartStringStringIPartListOfCommand() {
        Part part = new Part(CHILD_1, DESCRIPTION, root1, COMMANDS);
        assertEquals(CHILD_1, part.getName());
        assertEquals(DESCRIPTION, part.getDescription());
        assertEquals(COMMANDS, part.getCommands());
        assertEquals(root1, part.getIsPartOf());
        assertEquals(StandardComponents.SYSTEM, part.getIssuedBy());
        assertEquals(Named.DEFAULT_QUALIFIED_NAME_SEPARATOR + ROOT_1 + Named.DEFAULT_QUALIFIED_NAME_SEPARATOR + CHILD_1, part.getQualifiedName());
        assertEquals(SEPARATOR + ROOT_1 + SEPARATOR + CHILD_1, part.getQualifiedName(SEPARATOR));
        assertTrue(part.getTimestamp() <= System.currentTimeMillis() && part.getTimestamp() > System.currentTimeMillis() - 3 * 1000L);
        assertEquals(Part.class.getSimpleName(), part.getType());
        assertNotNull(part.getUuid());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.Part#getIsPartOf()}.
     */
    @Test
    public void testGetIsPartOf() {
        assertNull(root1.getIsPartOf());
        assertNull(root2.getIsPartOf());
        assertEquals(root1, root1Child1.getIsPartOf());
        assertEquals(root1, root1Child2.getIsPartOf());
        assertEquals(root2, root2Child1.getIsPartOf());
        assertEquals(root2, root2Child2.getIsPartOf());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.Part#setIsPartOf(org.hbird.exchange.interfaces.IPart)}.
     */
    @Test
    public void testSetIsPartOf() {
        assertEquals(root1, root1Child1.getIsPartOf());
        root1Child1.setIsPartOf(null);
        assertEquals(root1, root1Child1.getIsPartOf());
        root1Child1.setIsPartOf(root2);
        assertEquals(root2, root1Child1.getIsPartOf());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.Part#getCommands()}.
     */
    @Test
    public void testGetCommands() {
        testSetCommands();
    }

    /**
     * Test method for {@link org.hbird.exchange.core.Part#setCommands(java.util.List)}.
     */
    @Test
    public void testSetCommands() {
        assertNotNull(root1.getCommands());
        assertEquals(0, root1.getCommands().size());
        assertNotSame(COMMANDS, root1.getCommands());
        root1.setCommands(null);
        assertNull(root1.getCommands());
        root1.setCommands(COMMANDS);
        assertEquals(COMMANDS, root1.getCommands());
        Command c = new Command("test", "/dev/null", "dummy", "Just 4 test");
        COMMANDS.add(c);
        assertEquals(1, root1.getCommands().size());
        assertEquals(c, root1.getCommands().get(0));
    }
}
