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
package eu.estcube.gs.rotator.nativecommands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 *
 */
public class SetPositionTest {

    /**
     * Test method for
     * {@link eu.estcube.gs.rotator.nativecommands.SetPosition#createCommand(java.lang.Double, java.lang.Double)}.
     */
    @Test
    public void testCreateCommand() {
        assertEquals("+P 0 0\n", SetPosition.createCommand(0.0D, 0.0D));
        assertEquals("+P 0.000001 0.000001\n", SetPosition.createCommand(0.000001D, 0.000001D));
        assertEquals("+P 0.000001 0.000001\n", SetPosition.createCommand(0.0000011D, 0.0000011D));
        assertEquals("+P 0.000002 0.000002\n", SetPosition.createCommand(0.0000019D, 0.0000019D));
        assertEquals("+P 100000.000002 100000.000002\n", SetPosition.createCommand(100000.0000019D, 100000.0000019D));
        assertEquals("+P 1.1 1.1\n", SetPosition.createCommand(1.1D, 1.1D));
        assertEquals("+P 1.01 1.01\n", SetPosition.createCommand(1.01D, 1.01D));
        assertEquals("+P 1.001 1.001\n", SetPosition.createCommand(1.001D, 1.001D));
        assertEquals("+P 1.0001 1.0001\n", SetPosition.createCommand(1.0001D, 1.0001D));
        assertEquals("+P 1.00001 1.00001\n", SetPosition.createCommand(1.00001D, 1.00001D));
        assertEquals("+P 1.000001 1.000001\n", SetPosition.createCommand(1.000001D, 1.000001D));
        assertEquals("+P 1 1\n", SetPosition.createCommand(1.0000001D, 1.0000001D));
        assertEquals("+P 10 10\n", SetPosition.createCommand(10D, 10D));
        assertEquals("+P 100 100\n", SetPosition.createCommand(100D, 100D));
        assertEquals("+P 1000 1000\n", SetPosition.createCommand(1000D, 1000D));
        assertEquals("+P 10000 10000\n", SetPosition.createCommand(10000D, 10000D));
        assertEquals("+P 100000 100000\n", SetPosition.createCommand(100000D, 100000D));
        assertEquals("+P 1000000 1000000\n", SetPosition.createCommand(1000000D, 1000000D));
        assertEquals("+P 10000000 10000000\n", SetPosition.createCommand(10000000D, 10000000D));

        assertEquals("+P 1 2\n", SetPosition.createCommand(1D, 2D));
        assertEquals("+P 1 2\n", SetPosition.createCommand(1.0D, 2.0D));
        assertEquals("+P 1 2\n", SetPosition.createCommand(01.00D, 02.00D));

        assertEquals("+P -0 -0\n", SetPosition.createCommand(-0.0D, -0.0D));
        assertEquals("+P -0.1 -0.1\n", SetPosition.createCommand(-0.1D, -0.1D));
        assertEquals("+P -0.000001 -0.000001\n", SetPosition.createCommand(-0.000001D, -0.000001D));
        assertEquals("+P -0.000001 -0.000001\n", SetPosition.createCommand(-0.0000011D, -0.0000011D));
        assertEquals("+P -0.000002 -0.000002\n", SetPosition.createCommand(-0.0000019D, -0.0000019D));
        assertEquals("+P -100000.000002 -100000.000002\n", SetPosition.createCommand(-100000.0000019D, -100000.0000019D));

    }
}
