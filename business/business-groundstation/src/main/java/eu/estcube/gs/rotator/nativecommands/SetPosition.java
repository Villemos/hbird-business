package eu.estcube.gs.rotator.nativecommands;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class SetPosition {

    /**
     * Using this format to convert double values to {@link String}.
     * Format takes care of rounding and output length.
     * Also uses decimal notation instead of scientific notation.
     * US specific symbols are taking care of decimal and grouping symbols which may otherwise corrupt the output.
     * In final {@link String} - output has max 6 decimal places. Dot is used as decimal separator and no grouping
     * symbols are used. Unnecessary zeros are avoided.
     */
    public static final DecimalFormat DOUBLE_FORMATTER = new DecimalFormat("0.######", DecimalFormatSymbols.getInstance(Locale.US));

    public static String createCommand(Double azimuth, Double elevation) {
        StringBuilder messageString = new StringBuilder();
        messageString.append("+P ");
        messageString.append(DOUBLE_FORMATTER.format(azimuth));
        messageString.append(" ");
        messageString.append(DOUBLE_FORMATTER.format(elevation));
        messageString.append("\n");
        return messageString.toString();
    }
}
