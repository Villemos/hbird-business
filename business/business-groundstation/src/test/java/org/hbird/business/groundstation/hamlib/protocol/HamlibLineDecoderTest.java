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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class HamlibLineDecoderTest {

    private HamlibLineDecoder decoder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        decoder = new HamlibLineDecoder();
    }

    @Test
    public void testNewChannelHandler() throws Exception {
        ChannelHandler h1 = decoder.newChannelHandler();
        ChannelHandler h2 = decoder.newChannelHandler();
        assertNotNull(h1);
        assertEquals(DelimiterBasedFrameDecoder.class, h1.getClass());
        assertNotNull(h2);
        assertEquals(DelimiterBasedFrameDecoder.class, h2.getClass());
        assertNotSame(h1, h2);
    }
}
