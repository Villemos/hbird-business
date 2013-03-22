package eu.estcube.common;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.common.Naming.Base;

/**
 *
 */
public class NamingTest {

    /**
     * Test method for
     * {@link eu.estcube.common.Naming#createParameterAbsoluteName(eu.estcube.common.Naming.Base, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testCreateParameterAbsoluteName() {
        assertEquals("Satellite/source/parameter",
                Naming.createParameterAbsoluteName(Base.SATELLITE, "source", "parameter"));
        assertEquals("GroundStation/source/parameter",
                Naming.createParameterAbsoluteName(Base.GROUND_STATION, "source", "parameter"));
        assertEquals("WeatherStation/source/parameter",
                Naming.createParameterAbsoluteName(Base.WEATHER_STATION, "source", "parameter"));
    }

    /**
     * Test method for
     * {@link eu.estcube.common.Naming#createDataSetIdentifier(eu.estcube.common.Naming.Base, java.lang.String, long)}
     * .
     */
    @Test
    public void testCreateDataSetIdentifier() {
        long now = System.currentTimeMillis();
        assertEquals("Satellite-source-" + now, Naming.createDataSetIdentifier(Base.SATELLITE, "source", now));
        assertEquals("GroundStation-source-" + now, Naming.createDataSetIdentifier(Base.GROUND_STATION, "source", now));
        assertEquals("WeatherStation-source-" + now,
                Naming.createDataSetIdentifier(Base.WEATHER_STATION, "source", now));
    }
}
