package org.hbird.business.groundstation.hamlib.rotator.protocol;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.camel.TypeConverter;
import org.apache.commons.lang3.StringUtils;
import org.hbird.business.groundstation.base.DriverContext;
import org.hbird.business.groundstation.configuration.RotatorDriverConfiguration;
import org.hbird.business.groundstation.device.response.ResponseHandler;
import org.hbird.business.groundstation.hamlib.protocol.HamlibProtocolHelper;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.interfaces.IEntityInstance;

public class SetPosition implements ResponseHandler<RotatorDriverConfiguration, String, String> {

    public static final String KEY = "set_pos";

    /**
     * Using this format to convert double values to {@link String}.
     * Format takes care of rounding and output length.
     * Also uses decimal notation instead of scientific notation.
     * US specific symbols are taking care of decimal and grouping symbols which may otherwise corrupt the output.
     * In final {@link String} - output has max 6 decimal places. Dot is used as decimal separator and no grouping
     * symbols are used. Unnecessary zeros are avoided.
     */
    public static final DecimalFormat DOUBLE_FORMATTER = new DecimalFormat("0.######", DecimalFormatSymbols.getInstance(Locale.US));

    /**
     * @see org.hbird.business.groundstation.device.response.ResponseHandler#getKey()
     */
    @Override
    public String getKey() {
        return KEY;
    }

    /**
     * @see org.hbird.business.groundstation.device.response.ResponseHandler#handle(org.hbird.business.groundstation.DriverContext,
     *      java.lang.Object)
     */
    @Override
    public List<IEntityInstance> handle(DriverContext<RotatorDriverConfiguration, String, String> driverContext, String response) {
        if (HamlibProtocolHelper.isErrorResponse(response)) {
            // TODO - 27.04.2013, kimmell - handle error response
            return Collections.emptyList();
        }
        else {
            List<IEntityInstance> result = new ArrayList<IEntityInstance>(2);
            TypeConverter converter = driverContext.getTypeConverter();
            String id = driverContext.getPart().getID();
            Map<String, String> map = HamlibProtocolHelper.toMap(response);
            String args = map.get(KEY);
            String azimuthValue = StringUtils.substringBefore(args, " ").trim();
            String elevationValue = StringUtils.substringAfter(args, " ").trim();

            Parameter azimuth = new Parameter(id + "/" + "Target Azimuth", "Target Azimuth");
            azimuth.setIssuedBy(id);
            azimuth.setDescription("Target Azimuth of the antenna rotator");
            azimuth.setValue(converter.convertTo(Double.class, azimuthValue));
            azimuth.setUnit("Degree");

            Parameter elevation = new Parameter(id + "/" + "Target Elevation", "Target Elevation");
            elevation.setIssuedBy(id);
            elevation.setDescription("Target Elevation of the antenna rotator");
            elevation.setValue(converter.convertTo(Double.class, elevationValue));
            elevation.setUnit("Degree");

            result.add(azimuth);
            result.add(elevation);
            return result;
        }
    }

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
