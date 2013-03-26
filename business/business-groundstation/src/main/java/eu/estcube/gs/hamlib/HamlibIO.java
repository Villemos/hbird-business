package eu.estcube.gs.hamlib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HamlibIO {
    private static final Logger LOG = LoggerFactory.getLogger(HamlibIO.class);

    public static String getDeviceDriverUrl(HamlibDriverConfiguration config) {
        return getNettyAddress(config);
    }

    private static String getNettyAddress(HamlibDriverConfiguration config) {
        StringBuilder address = new StringBuilder("netty:tcp://" + config.getAddress());
        LOG.debug("Hamlib config.getAddress {} ", config.getAddress());

        address.append("?sync=true"); // allow for responses to be forwarded
        address.append("&encoders=#encoderStringToBytes");

        /* the decoders are called in this order, from left to right */
        address
                // .append("&decoders=#decoderSplitOnNewline,#decoderBytesToString,#decoderBufferer,#decoderStringLogger,#decoderStringToTelemetryObject");
                .append("&decoders=#decoderSplitOnNewline,#decoderBytesToString,#decoderBufferer,#decoderStringLogger");
        return address.toString();
    }
}
