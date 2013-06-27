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

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.hbird.exchange.constants.StandardArguments;
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
public class SetHamlibNativeCommandHeadersTest {

    private static final String STAGE = "STAGE-1";
    private static final String COMMAND_ID = "COMMAND-1:0";
    private static final long EXECUTION_TIME = System.currentTimeMillis();
    private static final String DERIVED_FROM = "TRACK-1:123";

    @Mock
    private Exchange exchange;

    @Mock
    private Message in;

    @Mock
    private Message out;

    @Mock
    private HamlibNativeCommand command;

    private SetHamlibNativeCommandHeaders setHeaders;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        setHeaders = new SetHamlibNativeCommandHeaders();
        inOrder = inOrder(exchange, in, out, command);
    }

    @Test
    public void testProcess() throws Exception {
        when(exchange.getIn()).thenReturn(in);
        when(exchange.getOut()).thenReturn(out);
        when(in.getBody(HamlibNativeCommand.class)).thenReturn(command);
        when(command.getStage()).thenReturn(STAGE);
        when(command.getDerivedFrom()).thenReturn(DERIVED_FROM);
        when(command.getInstanceID()).thenReturn(COMMAND_ID);
        when(command.getExecutionTime()).thenReturn(EXECUTION_TIME);

        setHeaders.process(exchange);

        inOrder.verify(exchange, times(1)).getIn();
        inOrder.verify(exchange, times(1)).getOut();
        inOrder.verify(out, times(1)).copyFrom(in);
        inOrder.verify(in, times(1)).getBody(HamlibNativeCommand.class);
        inOrder.verify(command, times(1)).getStage();
        inOrder.verify(out, times(1)).setHeader(SetHamlibNativeCommandHeaders.HEADER_STAGE, STAGE);
        inOrder.verify(command, times(1)).getDerivedFrom();
        inOrder.verify(out, times(1)).setHeader(StandardArguments.DERIVED_FROM, DERIVED_FROM);
        inOrder.verify(command, times(1)).getInstanceID();
        inOrder.verify(out, times(1)).setHeader(SetHamlibNativeCommandHeaders.HEADER_COMMAND_ID, COMMAND_ID);
        inOrder.verify(command, times(1)).getExecutionTime();
        inOrder.verify(out, times(1)).setHeader(SetHamlibNativeCommandHeaders.HEADER_EXECUTION_TIME, EXECUTION_TIME);
        inOrder.verifyNoMoreInteractions();
    }
}
