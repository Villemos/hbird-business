package org.hbird.exchange.core;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ParameterTest {

    @Test
    public void testPrettyPrint() {
        Parameter parameter = new Parameter("issuer", "A name", "type-0", "parameter description", null, "unit of the parameter");
        assertNotNull(parameter.prettyPrint());
    }
}
