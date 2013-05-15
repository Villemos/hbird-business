package org.hbird.exchange.core;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ParameterTest {

    @Test
    public void testToString() {
        Parameter parameter = new Parameter("ID", "A name");
        String s = parameter.toString();
        assertNotNull(s);
    }
}
