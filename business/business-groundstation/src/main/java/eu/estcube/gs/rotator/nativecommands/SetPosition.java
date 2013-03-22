package eu.estcube.gs.rotator.nativecommands;

public class SetPosition {

    public static StringBuilder createMessageString(Double azimuth, Double elevation) {
        StringBuilder messageString = new StringBuilder();
        messageString.append("+P ");
        messageString.append(azimuth);
        messageString.append(" ");
        messageString.append(elevation);
        messageString.append("\n");
        return messageString;
    }
}
