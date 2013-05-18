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
package org.hbird.business.core;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.hbird.exchange.commandrelease.CommandRequest;
import org.hbird.exchange.configurator.StandardEndpoints;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.Entity;
import org.hbird.exchange.core.EntityInstance;
import org.hbird.exchange.core.Event;
import org.hbird.exchange.core.Label;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.tasking.Task;
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
public class EntityRouterTest {

    @Mock
    private Exchange exchange;

    @Mock
    private Message in;

    @Mock
    private Message out;

    @Mock
    private Object body;

    private EntityRouter router;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        router = new EntityRouter();
        inOrder = inOrder(exchange, in, out, body);
        when(exchange.getIn()).thenReturn(in);
        when(exchange.getOut()).thenReturn(out);
        when(out.getBody()).thenReturn(body);
    }

    @SuppressWarnings("serial")
    @Test
    public void testRoute() throws Exception {
        assertEquals(StandardEndpoints.COMMANDS, router.route(new Command("", "")));
        assertEquals(StandardEndpoints.REQUESTS, router.route(new CommandRequest("", "")));
        assertEquals(StandardEndpoints.TASKS, router.route(new Task("", "") {
            @Override
            public List<EntityInstance> execute() {
                return null;
            }
        }));
        assertEquals(StandardEndpoints.EVENTS, router.route(new Event("", "")));
        assertEquals(StandardEndpoints.MONITORING, router.route(new Object()));
        assertEquals(StandardEndpoints.MONITORING, router.route(new Parameter("", "")));
        assertEquals(StandardEndpoints.MONITORING, router.route(new Label("", "")));
        assertEquals(StandardEndpoints.MONITORING, router.route(new Entity("", "")));
        assertEquals(StandardEndpoints.MONITORING, router.route(new EntityInstance("", "") {
        }));
    }

    @Test
    public void testProcess() throws Exception {
        router.process(exchange);
        inOrder.verify(exchange, times(1)).getIn();
        inOrder.verify(exchange, times(1)).getOut();
        inOrder.verify(out, times(1)).copyFrom(in);
        inOrder.verify(out, times(1)).getBody();
        inOrder.verify(out, times(1)).setHeader(EntityRouter.ROUTING_HEADER, StandardEndpoints.MONITORING);
        inOrder.verifyNoMoreInteractions();
    }
}
