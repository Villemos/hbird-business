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
package org.hbird.business.groundstation.hamlib.rotator;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.inOrder;

import org.hbird.business.groundstation.configuration.RotatorDriverConfiguration;
import org.hbird.exchange.core.Part;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class HamlibRotatorPartTest {

    private static final String NAME = "ROTATOR";

    @Mock
    private RotatorDriverConfiguration configuration;

    private HamlibRotatorPart hamlibRotatorPart;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        hamlibRotatorPart = new HamlibRotatorPart(new Part("root", ""), NAME, configuration);
        inOrder = inOrder(configuration);
    }

    @After
    public void tearDown() {
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testHamlibRotatorPart() {
        assertEquals(NAME, hamlibRotatorPart.getName());
        assertEquals(configuration, hamlibRotatorPart.getConfiguration());
        assertEquals(HamlibRotatorPart.DESCRIPTION, hamlibRotatorPart.getDescription());
        assertEquals(HamlibRotatorDriver.class.getName(), hamlibRotatorPart.getDriverName());
    }
}
