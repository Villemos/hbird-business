package eu.estcube.common;

import org.apache.camel.Message;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.Named;

/**
 * Common header keys for {@link Message}s.
 */
public class Headers {

    /** Header key for {@link Named#getUuid()}. */
    public static final String UUID = "UUID";

    /** Header key for {@link Named#getName()}. */
    public static final String NAME = "name";

    /** Header key for {@link Named#getIssuedBy()}. */
    public static final String ISSUED_BY = "issuedBy";

    /** Header key for {@link Class#getSimpleName()}. */
    public static final String CLASS = "class";

    /** Header key for {@link Named#getType()}. */
    public static final String TYPE = "type";

    /** Header key for {@link Named#getDatasetidentifier()}. */
    public static final String DATA_SET_IDENTIFIER = "datasetidentifier";

    /** Header key for {@link Named#timestamp}. */
    public static final String TIMESTAMP = "timestamp";

    /** Header key for {@link Command#destination} */
    public static final String DESTINATION = "destination";

    /** Header key for contact id. */
    public static final String CONTACT_ID = "contact";
}
