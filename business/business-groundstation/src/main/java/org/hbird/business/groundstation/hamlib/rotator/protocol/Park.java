package org.hbird.business.groundstation.hamlib.rotator.protocol;

import java.util.Collections;
import java.util.List;

import org.hbird.business.groundstation.base.DriverContext;
import org.hbird.business.groundstation.configuration.RotatorDriverConfiguration;
import org.hbird.business.groundstation.device.response.ResponseHandler;
import org.hbird.business.groundstation.hamlib.protocol.HamlibProtocolHelper;
import org.hbird.exchange.interfaces.INamed;

public class Park implements ResponseHandler<RotatorDriverConfiguration, String, String> {

    public static final String COMMAND = "+K\n";
    public static final String KEY = "park";

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
