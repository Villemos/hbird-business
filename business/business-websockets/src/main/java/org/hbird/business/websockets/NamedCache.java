package org.hbird.business.websockets;

import org.hbird.exchange.core.Named;
import org.springframework.stereotype.Component;

@Component
public class NamedCache extends AbstractCache<Named> {

    public static final String SEPARATOR = "/";

    /** @{inheritDoc}. */
    @Override
    protected boolean shouldReplace(Named oldValue, Named newValue) {
        return oldValue.getTimestamp() <= newValue.getTimestamp();
    }

    /** @{inheritDoc}. */
    @Override
    protected String getId(Named t) {
        StringBuilder sb = new StringBuilder();
        sb.append(t.getIssuedBy())
          .append(SEPARATOR)
          .append(t.getName());
        return sb.toString();
    }
}
