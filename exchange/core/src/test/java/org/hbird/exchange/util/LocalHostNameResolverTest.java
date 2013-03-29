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
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 *
 */
public class LocalHostNameResolverTest {

    /**
     * Test method for {@link org.hbird.exchange.util.LocalHostNameResolver#getLocalHostName()}.
     */
    @Test
    public void testGetLocalHostName() {
        String name1 = LocalHostNameResolver.getLocalHostName();
        String name2 = LocalHostNameResolver.getLocalHostName();
        assertNotNull(name1);
        assertNotNull(name2);
        assertEquals(name1, name2);
    }

    /**
     * Test method for {@link org.hbird.exchange.util.LocalHostNameResolver#resolveLocalHostName(java.lang.String)}.
     */
    @Test
    public void testResolveLocalHostName() {
        String name1 = LocalHostNameResolver.resolveLocalHostName("default host name");
        String name2 = LocalHostNameResolver.resolveLocalHostName("default host name");
        assertNotNull(name1);
        assertNotNull(name2);
        assertEquals(name1, name2);
    }
}
