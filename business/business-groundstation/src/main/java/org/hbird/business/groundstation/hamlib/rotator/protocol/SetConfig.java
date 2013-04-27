package org.hbird.business.groundstation.hamlib.rotator.protocol;

/**
 * Not used. Implement if needed.
 */
@Deprecated
public class SetConfig {

    public static StringBuilder createMessageString(String token, Number value) {
        StringBuilder messageString = new StringBuilder();
        messageString.append("+C ");
        messageString.append(token);
        messageString.append(" ");
        messageString.append(value);
        messageString.append("\n");
        return messageString;
    }
}
