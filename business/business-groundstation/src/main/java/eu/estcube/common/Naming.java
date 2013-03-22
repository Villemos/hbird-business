package eu.estcube.common;

/**
 * Helper methods for the ESTCube naming conventions.
 */
public class Naming {

    public static final String PARAMETER_NAME_SEPARATOR = "/";

    public static final String DATA_SET_IDENTIFIER_SEPARATOR = "-";

    public enum Base {
        SATELLITE("Satellite"),
        GROUND_STATION("GroundStation"),
        WEATHER_STATION("WeatherStation"), ;

        private final String name;

        private Base(String name) {
            this.name = name;
        }

        /** @{inheritDoc . */
        @Override
        public String toString() {
            return name;
        }
    }

    public static String createParameterAbsoluteName(Base base, String source, String relativeParameterName) {
        StringBuilder sb = new StringBuilder();
        sb.append(base)
                .append(PARAMETER_NAME_SEPARATOR)
                .append(source)
                .append(PARAMETER_NAME_SEPARATOR)
                .append(relativeParameterName);
        return sb.toString();
    }

    public static String createDataSetIdentifier(Base base, String source, long timestamp) {
        StringBuilder sb = new StringBuilder();
        sb.append(base)
                .append(DATA_SET_IDENTIFIER_SEPARATOR)
                .append(source)
                .append(DATA_SET_IDENTIFIER_SEPARATOR)
                .append(timestamp);
        return sb.toString();
    }
}
