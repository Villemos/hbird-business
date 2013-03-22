package eu.estcube.gs.rotator.nativecommands;

public class Reset {

    public static StringBuilder createMessageString(int toReset) {
        StringBuilder messageString = new StringBuilder();
        messageString.append("+R ");
        messageString.append(toReset);
        messageString.append("\n");
        return messageString;
    }
}
