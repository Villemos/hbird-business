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

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.core.ApplicableTo;
import org.hbird.exchange.core.CommandBase;
import org.hbird.exchange.interfaces.IDerivedFrom;
import org.hbird.exchange.interfaces.IEntity;
import org.hbird.exchange.interfaces.IEntityInstance;
import org.hbird.exchange.interfaces.IGroundStationSpecific;
import org.hbird.exchange.interfaces.ISatelliteSpecific;
import org.hbird.exchange.interfaces.IScheduled;
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
public class AddHeadersTest {

    private static final String NAME = "NAME";
    private static final String ENTITY_ID = "ENTITY ID";
    private static final String ENTITY_INSTANCE_ID = "ENTITY INSTANCE ID";
    private static final long NOW = System.currentTimeMillis();
    private static final String ISSUED_BY = "ISSUER";
    private static final String APPLICABLE_TO = "APPLICABLE TO";
    private static final String DERIVED_FROM = "DERIVED FROM";
    private static final String GS_ID = "GS ID";
    private static final String SAT_ID = "SAT ID";
    private static final String DESTINATION = "DESTINATION";
    private static final long VERSION = (long) (Math.random() * 10000000L);

    @Mock
    private Exchange exchange;

    @Mock
    private Message in;

    @Mock
    private Message out;

    @Mock
    private IEntity entity;

    @Mock
    private IEntityInstance entityInstance;

    @Mock
    private ApplicableTo applicableTo;

    @Mock
    private CommandBase command;

    @Mock
    private IDerivedFrom derivedFrom;

    @Mock
    private IGroundStationSpecific gsSpcific;

    @Mock
    private ISatelliteSpecific satSpcific;

    private AddHeaders addHeaders;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        addHeaders = new AddHeaders();
        inOrder = inOrder(exchange, in, out, entity, entityInstance, applicableTo, command, derivedFrom, gsSpcific, satSpcific);
        when(exchange.getIn()).thenReturn(in);
        when(exchange.getOut()).thenReturn(out);
    }

    @Test
    public void testProcessNull() throws Exception {
        when(in.getBody()).thenReturn(null);
        addHeaders.process(exchange);
        inOrder.verify(exchange, times(1)).getIn();
        inOrder.verify(in, times(1)).getBody();
        inOrder.verify(exchange, times(1)).setProperty(Exchange.ROUTE_STOP, Boolean.TRUE);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testProcessEntity() throws Exception {
        when(in.getBody()).thenReturn(entity);
        when(entity.getID()).thenReturn(ENTITY_ID);
        when(entity.getName()).thenReturn(NAME);
        when(entity.getIssuedBy()).thenReturn(ISSUED_BY);
        addHeaders.process(exchange);
        inOrder.verify(exchange, times(1)).getIn();
        inOrder.verify(in, times(1)).getBody();
        inOrder.verify(exchange, times(1)).getOut();
        inOrder.verify(out, times(1)).copyFrom(in);
        inOrder.verify(out, times(1)).setHeader(StandardArguments.CLASS, entity.getClass().getSimpleName());
        inOrder.verify(out, times(1)).setHeader(StandardArguments.ENTITY_ID, ENTITY_ID);
        inOrder.verify(out, times(1)).setHeader(StandardArguments.NAME, NAME);
        inOrder.verify(out, times(1)).setHeader(StandardArguments.ISSUED_BY, ISSUED_BY);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testProcessEntityInstance() throws Exception {
        when(in.getBody()).thenReturn(entityInstance);
        when(entityInstance.getID()).thenReturn(ENTITY_ID);
        when(entityInstance.getName()).thenReturn(NAME);
        when(entityInstance.getIssuedBy()).thenReturn(ISSUED_BY);
        when(entityInstance.getInstanceID()).thenReturn(ENTITY_INSTANCE_ID);
        when(entityInstance.getVersion()).thenReturn(VERSION);
        when(entityInstance.getTimestamp()).thenReturn(NOW);
        addHeaders.process(exchange);
        inOrder.verify(exchange, times(1)).getIn();
        inOrder.verify(in, times(1)).getBody();
        inOrder.verify(exchange, times(1)).getOut();
        inOrder.verify(out, times(1)).copyFrom(in);
        inOrder.verify(out, times(1)).setHeader(StandardArguments.CLASS, entityInstance.getClass().getSimpleName());
        inOrder.verify(out, times(1)).setHeader(StandardArguments.ENTITY_ID, ENTITY_ID);
        inOrder.verify(out, times(1)).setHeader(StandardArguments.NAME, NAME);
        inOrder.verify(out, times(1)).setHeader(StandardArguments.ISSUED_BY, ISSUED_BY);
        inOrder.verify(out, times(1)).setHeader(StandardArguments.ENTITY_INSTANCE_ID, ENTITY_INSTANCE_ID);
        inOrder.verify(out, times(1)).setHeader(StandardArguments.VERSION, VERSION);
        inOrder.verify(out, times(1)).setHeader(StandardArguments.TIMESTAMP, NOW);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testProcessApplicableTo() throws Exception {
        when(in.getBody()).thenReturn(applicableTo);
        when(applicableTo.getID()).thenReturn(ENTITY_ID);
        when(applicableTo.getName()).thenReturn(NAME);
        when(applicableTo.getIssuedBy()).thenReturn(ISSUED_BY);
        when(applicableTo.getInstanceID()).thenReturn(ENTITY_INSTANCE_ID);
        when(applicableTo.getVersion()).thenReturn(VERSION);
        when(applicableTo.getTimestamp()).thenReturn(NOW);
        when(applicableTo.getApplicableTo()).thenReturn(APPLICABLE_TO);
        addHeaders.process(exchange);
        inOrder.verify(exchange, times(1)).getIn();
        inOrder.verify(in, times(1)).getBody();
        inOrder.verify(exchange, times(1)).getOut();
        inOrder.verify(out, times(1)).copyFrom(in);
        inOrder.verify(out, times(1)).setHeader(StandardArguments.CLASS, applicableTo.getClass().getSimpleName());
        inOrder.verify(out, times(1)).setHeader(StandardArguments.ENTITY_ID, ENTITY_ID);
        inOrder.verify(out, times(1)).setHeader(StandardArguments.NAME, NAME);
        inOrder.verify(out, times(1)).setHeader(StandardArguments.ISSUED_BY, ISSUED_BY);
        inOrder.verify(out, times(1)).setHeader(StandardArguments.ENTITY_INSTANCE_ID, ENTITY_INSTANCE_ID);
        inOrder.verify(out, times(1)).setHeader(StandardArguments.VERSION, VERSION);
        inOrder.verify(out, times(1)).setHeader(StandardArguments.TIMESTAMP, NOW);
        inOrder.verify(out, times(1)).setHeader(StandardArguments.APPLICABLE_TO, APPLICABLE_TO);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testProcessCommand() throws Exception {
        when(in.getBody()).thenReturn(command);
        when(command.getID()).thenReturn(ENTITY_ID);
        when(command.getName()).thenReturn(NAME);
        when(command.getIssuedBy()).thenReturn(ISSUED_BY);
        when(command.getInstanceID()).thenReturn(ENTITY_INSTANCE_ID);
        when(command.getVersion()).thenReturn(VERSION);
        when(command.getTimestamp()).thenReturn(NOW);
        when(command.getDestination()).thenReturn(DESTINATION);
        when(command.getTransferTime()).thenReturn(NOW - 10000);
        addHeaders.process(exchange);
        inOrder.verify(exchange, times(1)).getIn();
        inOrder.verify(in, times(1)).getBody();
        inOrder.verify(exchange, times(1)).getOut();
        inOrder.verify(out, times(1)).copyFrom(in);
        inOrder.verify(out, times(1)).setHeader(StandardArguments.CLASS, command.getClass().getSimpleName());
        inOrder.verify(out, times(1)).setHeader(StandardArguments.ENTITY_ID, ENTITY_ID);
        inOrder.verify(out, times(1)).setHeader(StandardArguments.NAME, NAME);
        inOrder.verify(out, times(1)).setHeader(StandardArguments.ISSUED_BY, ISSUED_BY);
        inOrder.verify(out, times(1)).setHeader(StandardArguments.ENTITY_INSTANCE_ID, ENTITY_INSTANCE_ID);
        inOrder.verify(out, times(1)).setHeader(StandardArguments.VERSION, VERSION);
        inOrder.verify(out, times(1)).setHeader(StandardArguments.TIMESTAMP, NOW);
        inOrder.verify(out, times(1)).setHeader(StandardArguments.DESTINATION, DESTINATION);
        inOrder.verify(out, times(1)).setHeader(AddHeaders.AMQ_SCHEDULED_DELAY, IScheduled.IMMEDIATE);
        inOrder.verify(out, times(1)).setHeader(AddHeaders.DELIVERYTIME, NOW - 10000);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testProcessDerivedFrom() throws Exception {
        when(in.getBody()).thenReturn(derivedFrom);
        when(derivedFrom.getDerivedFromId()).thenReturn(DERIVED_FROM);
        addHeaders.process(exchange);
        inOrder.verify(exchange, times(1)).getIn();
        inOrder.verify(in, times(1)).getBody();
        inOrder.verify(exchange, times(1)).getOut();
        inOrder.verify(out, times(1)).copyFrom(in);
        inOrder.verify(out, times(1)).setHeader(StandardArguments.CLASS, derivedFrom.getClass().getSimpleName());
        inOrder.verify(out, times(1)).setHeader(StandardArguments.DERIVED_FROM, DERIVED_FROM);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testProcessGsSpecific() throws Exception {
        when(in.getBody()).thenReturn(gsSpcific);
        when(gsSpcific.getGroundStationID()).thenReturn(GS_ID);
        addHeaders.process(exchange);
        inOrder.verify(exchange, times(1)).getIn();
        inOrder.verify(in, times(1)).getBody();
        inOrder.verify(exchange, times(1)).getOut();
        inOrder.verify(out, times(1)).copyFrom(in);
        inOrder.verify(out, times(1)).setHeader(StandardArguments.CLASS, gsSpcific.getClass().getSimpleName());
        inOrder.verify(out, times(1)).setHeader(StandardArguments.GROUND_STATION_ID, GS_ID);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testProcessSatSpecific() throws Exception {
        when(in.getBody()).thenReturn(satSpcific);
        when(satSpcific.getSatelliteID()).thenReturn(SAT_ID);
        addHeaders.process(exchange);
        inOrder.verify(exchange, times(1)).getIn();
        inOrder.verify(in, times(1)).getBody();
        inOrder.verify(exchange, times(1)).getOut();
        inOrder.verify(out, times(1)).copyFrom(in);
        inOrder.verify(out, times(1)).setHeader(StandardArguments.CLASS, satSpcific.getClass().getSimpleName());
        inOrder.verify(out, times(1)).setHeader(StandardArguments.SATELLITE_ID, SAT_ID);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testCalculateDely() {
        assertEquals(1, addHeaders.calculateDelay(1, 0));
        assertEquals(IScheduled.IMMEDIATE, addHeaders.calculateDelay(1, 1));
        assertEquals(IScheduled.IMMEDIATE, addHeaders.calculateDelay(1, 2));
        assertEquals(9, addHeaders.calculateDelay(10, 1));
    }
}
