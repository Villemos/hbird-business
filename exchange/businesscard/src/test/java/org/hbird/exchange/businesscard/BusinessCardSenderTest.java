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
package org.hbird.exchange.businesscard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import org.hbird.exchange.core.BusinessCard;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class BusinessCardSenderTest {

    private static final String ISSUER = "issuer";

    @Mock
    private BusinessCard card;

    private BusinessCardSender businessCardSender;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        businessCardSender = new BusinessCardSender(card);
        inOrder = inOrder(card);
    }

    /**
     * Test method for {@link org.hbird.exchange.businesscard.BusinessCardSender#send()}.
     */
    @Test
    public void testSend() {
        when(card.getIssuedBy()).thenReturn(ISSUER);
        BusinessCard bc = businessCardSender.send();
        assertEquals(card, bc);
        inOrder.verify(card, times(1)).getIssuedBy();
        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        inOrder.verify(card, times(1)).setTimestamp(captor.capture());
        inOrder.verifyNoMoreInteractions();
        Long newTimestamp = captor.getValue();
        assertTrue(newTimestamp <= System.currentTimeMillis() && newTimestamp > System.currentTimeMillis() - 3 * 1000L);
    }
}
