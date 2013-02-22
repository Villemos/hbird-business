package org.hbird.exchange.core;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ParameterTest {

    @Test
    public void testPrettyPrint() {
        Parameter parameter = new Parameter("issuer", "name", "type", "description", null, "unit");
        assertNotNull(parameter.prettyPrint());
    }
}
