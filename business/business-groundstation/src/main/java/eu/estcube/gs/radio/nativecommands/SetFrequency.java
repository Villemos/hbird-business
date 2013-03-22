package eu.estcube.gs.radio.nativecommands;

public class SetFrequency {

    public static StringBuilder createMessageString(Double frequency) {
        StringBuilder messageString = new StringBuilder();
        messageString.append("+F ");
        messageString.append(frequency);
        messageString.append("\n");
        return messageString;
    }
}
