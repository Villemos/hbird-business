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
package org.hbird.business.groundstation.base;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.core.Parameter;
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
public class OnChangeTest {

    private static final String NAME = "NAME";

    @Mock
    private Parameter param;

    @Mock
    private Map<String, Object> headers;

    private OnChange onChange;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        onChange = new OnChange();
        inOrder = inOrder(param, headers);
        when(param.getName()).thenReturn(NAME);
    }

    @Test
    public void testProcessNewParam() throws Exception {
        when(param.getValue()).thenReturn(0);
        onChange.process(param, headers);
        inOrder.verify(param, times(1)).getName();
        inOrder.verify(param, times(1)).getValue();
        inOrder.verify(headers, times(1)).put(StandardArguments.VALUE_HAS_CHANGED, true);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testProcessNoChange() throws Exception {
        when(param.getValue()).thenReturn(0, 0);
        onChange.process(param, headers);
        onChange.process(param, headers);
        inOrder.verify(param, times(1)).getName();
        inOrder.verify(param, times(1)).getValue();
        inOrder.verify(headers, times(1)).put(StandardArguments.VALUE_HAS_CHANGED, true);

        inOrder.verify(param, times(1)).getName();
        inOrder.verify(param, times(1)).getValue();
        inOrder.verify(headers, times(1)).put(StandardArguments.VALUE_HAS_CHANGED, false);

        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testProcessChangedValue() throws Exception {
        when(param.getValue()).thenReturn(0, 1);
        onChange.process(param, headers);
        onChange.process(param, headers);
        inOrder.verify(param, times(1)).getName();
        inOrder.verify(param, times(1)).getValue();
        inOrder.verify(headers, times(1)).put(StandardArguments.VALUE_HAS_CHANGED, true);

        inOrder.verify(param, times(1)).getName();
        inOrder.verify(param, times(1)).getValue();
        inOrder.verify(headers, times(1)).put(StandardArguments.VALUE_HAS_CHANGED, true);

        inOrder.verifyNoMoreInteractions();
    }

}
