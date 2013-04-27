package org.hbird.business.groundstation.hamlib.rotator.protocol;

import java.util.Collections;
import java.util.List;

import org.hbird.business.groundstation.base.DriverContext;
import org.hbird.business.groundstation.configuration.RotatorDriverConfiguration;
import org.hbird.business.groundstation.device.response.ResponseHandler;
import org.hbird.business.groundstation.hamlib.protocol.HamlibProtocolHelper;
import org.hbird.exchange.interfaces.INamed;

public class Reset implements ResponseHandler<RotatorDriverConfiguration, String, String> {

    public static final String KEY = "reset";

    // TODO - 17.04.2013, kimmell - test this
    // From documentation it's not clear if there is other possible options for the argument
    public static final String ALL = "+R 1\n";

    /**
     * @see org.hbird.business.groundstation.device.response.ResponseHandler#getKey()
     */
    @Override
    public String getKey() {
        return KEY;
    }

    /**
     * @see org.hbird.business.groundstation.device.response.ResponseHandler#handle(org.hbird.business.groundstation.base.DriverContext,
     *      java.lang.Object)
     */
    @Override
    public List<INamed> handle(DriverContext<RotatorDriverConfiguration, String, String> driverContext, String response) {
        if (HamlibProtocolHelper.isErrorResponse(response)) {
            // TODO - 27.04.2013, kimmell - handle error response
            return Collections.emptyList();
        }
        else {
            // TODO - 27.04.2013, kimmell - send some sort of notification here?
            return Collections.emptyList();
        }
    }
}
