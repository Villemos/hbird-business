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
package org.hbird.business.groundstation.hamlib;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class HamlibNativeCommandTest {

    private static final String COMMAND = "+1";
    private static final String DERIVED_FROM = "Track";
    private static final long NOW = System.currentTimeMillis();

    private HamlibNativeCommand command;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        command = new HamlibNativeCommand(COMMAND, NOW, DERIVED_FROM, HamlibNativeCommand.STAGE_POST_TRACKING);
    }

    @Test
    public void testHamlibNativeCommand() throws Exception {
        assertEquals("", command.getIssuedBy());
        assertEquals(HamlibNativeCommand.class.getSimpleName(), command.getName());
        assertEquals(HamlibNativeCommand.DESCRIPTION, command.getDescription());
        assertEquals(NOW, command.getExecutionTime());
        assertEquals(DERIVED_FROM, command.getDerivedfrom());
        assertEquals(HamlibNativeCommand.STAGE_POST_TRACKING, command.getStage());
        assertNotNull(command.getCommandid());
        UUID.fromString(command.getCommandid());
    }

    @Test
    public void testGetCommandToExecute() throws Exception {
        testSetCommandToExecute();
    }

    @Test
    public void testSetCommandToExecute() throws Exception {
        assertEquals(COMMAND, command.getCommandToExecute());
        command.setCommandToExecute("+_");
        assertEquals("+_", command.getCommandToExecute());
        command.setCommandToExecute("+P");
        assertEquals("+P", command.getCommandToExecute());
    }

    @Test
    public void testGetStage() throws Exception {
        testSetStage();
    }

    @Test
    public void testSetStage() throws Exception {
        assertEquals(HamlibNativeCommand.STAGE_POST_TRACKING, command.getStage());
        command.setStage(HamlibNativeCommand.STAGE_TRACKING);
        assertEquals(HamlibNativeCommand.STAGE_TRACKING, command.getStage());
        command.setStage(HamlibNativeCommand.STAGE_PRE_TRACKING);
        assertEquals(HamlibNativeCommand.STAGE_PRE_TRACKING, command.getStage());
    }

    @Test
    public void testGetDerivedfrom() throws Exception {
        testSetDerivedfrom();
    }

    @Test
    public void testSetDerivedfrom() throws Exception {
        assertEquals(DERIVED_FROM, command.getDerivedfrom());
        command.setDerivedfrom("Halt");
        assertEquals("Halt", command.getDerivedfrom());
        command.setDerivedfrom("Stop");
        assertEquals("Stop", command.getDerivedfrom());
    }

    @Test
    public void testGetCommandid() throws Exception {
        testSetCommandid();
    }

    @Test
    public void testSetCommandid() throws Exception {
        assertNotNull(command.getCommandid());
        UUID.fromString(command.getCommandid());
        command.setCommandid("ID-1");
        assertEquals("ID-1", command.getCommandid());
        command.setCommandid("ID-2");
        assertEquals("ID-2", command.getCommandid());
    }
}
