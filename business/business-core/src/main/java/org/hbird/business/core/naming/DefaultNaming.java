package org.hbird.business.core.naming;

import org.springframework.stereotype.Component;

@Component
public class DefaultNaming implements INaming {

    public static final String ID_SEPARATOR = "/";
    public static final String ID_SEPARATOR_ESCAPED = "&#47;";

    static String escape(String str) {
        return str.replaceAll(ID_SEPARATOR, ID_SEPARATOR_ESCAPED);
    }

    /**
     * @see org.hbird.business.core.naming.INaming#buildId(java.lang.String, java.lang.String)
     */
    @Override
    public String buildId(String base, String name) {
        return new StringBuilder()
                .append(base == null ? "" : base)
                .append(ID_SEPARATOR)
                .append(escape(name))
                .toString();
    }
}
