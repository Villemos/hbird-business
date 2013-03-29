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
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.hbird.exchange.core.Named;
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
public class NamedHelperTest {

    private static final String NAME_1 = "name-1";
    private static final String NAME_2 = "name-2";

    @Mock
    private Named named1;

    @Mock
    private Named named2;

    private InOrder inOrder;

    private Collection<Named> collection;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        inOrder = inOrder(named1, named2);
        collection = new ArrayList<Named>();
        collection.add(named1);
        collection.add(named2);
        when(named1.getName()).thenReturn(NAME_1);
        when(named2.getName()).thenReturn(NAME_2);
    }

    /**
     * Test method for {@link org.hbird.exchange.util.NamedHelper#toString(java.util.Collection)}.
     */
    @Test
    public void testToString() {
        String result = NamedHelper.toString(collection);
        assertEquals("[" + NAME_1 + "," + NAME_2 + "]", result);
        inOrder.verify(named1, times(1)).getName();
        inOrder.verify(named2, times(1)).getName();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for {@link org.hbird.exchange.util.NamedHelper#toString(java.util.Collection)}.
     */
    @Test
    public void testToStringEmptyCollection() {
        String result = NamedHelper.toString(Collections.<Named> emptyList());
        assertEquals("[]", result);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for {@link org.hbird.exchange.util.NamedHelper#toString(java.util.Collection)}.
     */
    @Test
    public void testToStringNull() {
        String result = NamedHelper.toString(null);
        assertEquals("null", result);
        inOrder.verifyNoMoreInteractions();
    }
}
