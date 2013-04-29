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
package org.hbird.business.groundstation.hamlib.protocol;

import static org.junit.Assert.assertEquals;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
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
public class HamlibErrorLoggerTest {

    @Mock
    private ChannelHandlerContext context;

    @Mock
    private Channel channel;

    private HamlibErrorLogger handler;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        handler = new HamlibErrorLogger();
        inOrder = Mockito.inOrder(context, channel);
    }

    @Test
    public void testDecode() throws Exception {
        String[] messages = {
                "set_pos:",
                "set_pos: 14.2 3.14\nRPRT 0",
                "set_pos: 14.2 3.14\nRPRT 0\n",
                "set_pos: 14.2 abc\nRPRT -1",
                "set_pos: 14.2 abc\nRPRT -1\n",
        };
        for (String input : messages) {
            assertEquals(input, handler.decode(context, channel, input));
        }
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testEscapeLineEnds() throws Exception {
        assertEquals("ab\\ncd\\nef", HamlibErrorLogger.escapeLineEnds("ab\ncd\nef"));
        assertEquals("ab\\n\rcd\\n\ref", HamlibErrorLogger.escapeLineEnds("ab\n\rcd\n\ref"));
    }
}
