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
package org.hbird.business.groundstation.hamlib.radio;

import static org.junit.Assert.assertEquals;

import org.hbird.business.groundstation.configuration.RadioDriverConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class HamlibRadioPartTest {

    public static final String NAME = "RADIO 2";

    @Mock
    private RadioDriverConfiguration config;

    private HamlibRadioPart part;

    private InOrder inOrder;

    @Before
    public void setup() {
        part = new HamlibRadioPart(NAME, config);
        inOrder = Mockito.inOrder(config);
    }

    @After
    public void tearDown() {
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testHamlibRadioPart() {
        assertEquals(NAME, part.getName());
        assertEquals(config, part.getConfiguration());
        assertEquals(HamlibRadioPart.DESCRIPTION, part.getDescription());
        assertEquals(HamlibRadioDriver.class.getName(), part.getDriverName());
    }
}
