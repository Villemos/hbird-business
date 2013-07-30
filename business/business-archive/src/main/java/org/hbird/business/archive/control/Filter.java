package org.hbird.business.archive.control;

import java.io.Serializable;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;

public abstract class Filter implements Predicate, Serializable {
    private static final long serialVersionUID = 8468620253205649948L;

    /**
     * 
     * @param obj The message
     * @return If false, the message is not stored in the archive
     */
    public abstract boolean passes(Object obj);

    @Override
    public boolean matches(Exchange exchange) {
        Object message = exchange.getIn().getBody();

        return passes(message);
    }
}
