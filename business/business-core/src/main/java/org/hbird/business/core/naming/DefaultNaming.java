package org.hbird.business.core.naming;

import org.springframework.stereotype.Component;

@Component
public class DefaultNaming implements Naming {

    public static final String NAME_SEPARATOR = "/";
    public static final String NAME_SEPARATOR_ESCAPED = "&#47;";

    public String createAbsoluteName(Base base, String source, String relativeName) {
        return createAbsoluteName(base.toString(), source, relativeName);
    }

    public String createAbsoluteName(String... parts) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            sb.append(parts[i]);
            if (i < parts.length - 1) {
                sb.append(NAME_SEPARATOR);
            }
        }
        return sb.toString();
    }

    static String escape(String str) {
        return str.replaceAll(NAME_SEPARATOR, NAME_SEPARATOR_ESCAPED);
    }
}
