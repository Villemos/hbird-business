package org.hbird.business.api.impl;

import org.hbird.business.api.IdBuilder;
import org.springframework.stereotype.Component;

@Component
public class DefaultIdBuilder implements IdBuilder {

    public static final String ID_SEPARATOR = "/";
    public static final String ID_SEPARATOR_ESCAPED = "&#47;";

    static String escape(String str) {
        return str.replaceAll(ID_SEPARATOR, ID_SEPARATOR_ESCAPED);
    }

    /**
     * @see org.hbird.business.api.IdBuilder#buildId(java.lang.String, java.lang.String)
     */
    @Override
    public String buildID(String base, String name) {
        return new StringBuilder()
                .append(base == null ? "" : base)
                .append(ID_SEPARATOR)
                .append(escape(name))
                .toString();
    }
}
