package eu.estcube.gs.radio.nativecommands;

import org.hbird.business.navigation.orekit.NavigationUtilities;

/**
 * Creates Hamlib native command to set the radio frequency
 * 
 * Command format is
 * 
 * <pre>
 * <code>F ${frequency}\n</code>
 * </pre>
 * 
 * Where <tt>${frequnecy}</tt> is radio frequency in Hz.
 * 
 */
public class SetFrequency {

    public static String createCommand(long frequency, double doppler) {
        double dopplerShift = NavigationUtilities.calculateDopplerShift(doppler, frequency);
        long actualFrequency = (long) (frequency + dopplerShift);
        return createCommand(actualFrequency);
    }

    public static String createCommand(long frquency) {
        StringBuilder sb = new StringBuilder();
        sb.append("+F ").append(frquency).append("\n");
        return sb.toString();
    }
}
