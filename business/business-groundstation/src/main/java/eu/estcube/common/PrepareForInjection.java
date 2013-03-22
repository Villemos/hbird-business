package eu.estcube.common;

import static eu.estcube.common.Headers.CLASS;
import static eu.estcube.common.Headers.DATA_SET_IDENTIFIER;
import static eu.estcube.common.Headers.ISSUED_BY;
import static eu.estcube.common.Headers.NAME;
import static eu.estcube.common.Headers.TIMESTAMP;
import static eu.estcube.common.Headers.TYPE;
import static eu.estcube.common.Headers.UUID;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.hbird.exchange.core.Named;
import org.springframework.stereotype.Component;

/**
 * Prepares {@link Exchange} containing {@link Named} object for injection to
 * the Camel routes. Copies values from {@link Named} object to {@link Message}
 * headers.
 * Values are mapped as:
 * <ul>
 * <li>{@link Named#getUuid()} to header {@link Headers#UUID}.</li>
 * <li>{@link Named#getName()} to header {@link Headers#NAME}.</li>
 * <li>{@link Named#getIssuedBy()} to header {@link Headers#ISSUED_BY}.</li>
 * <li>{@link Class#getSimpleName()} to header {@link Headers#CLASS}.</li>
 * <li>{@link Named#getType()} to header {@link Headers#TYPE}.</li>
 * <li>{@link Named#getDatasetidentifier()} to header
 * {@link Headers#DATA_SET_IDENTIFIER}.</li>
 * <li>{@link Named#getTimestamp()} to header {@link Headers#TIMESTAMP}.</li>
 * </ul>
 */
@Component
public class PrepareForInjection implements Processor {

    /** @{inheritDoc . */
    @Override
    public void process(Exchange exchange) throws Exception {
        Message in = exchange.getIn();
        Message out = exchange.getOut();
        out.copyFrom(in);
        Named named = in.getBody(Named.class);
        out.setHeader(UUID, named.getUuid());
        out.setHeader(NAME, named.getName());
        out.setHeader(ISSUED_BY, named.getIssuedBy());
        out.setHeader(CLASS, named.getClass().getSimpleName());
        out.setHeader(TYPE, named.getType());
        out.setHeader(DATA_SET_IDENTIFIER, named.getDatasetidentifier());
        out.setHeader(TIMESTAMP, named.getTimestamp());
    }
}
