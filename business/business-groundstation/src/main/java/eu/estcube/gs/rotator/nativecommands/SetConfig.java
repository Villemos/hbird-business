package eu.estcube.gs.rotator.nativecommands;

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
