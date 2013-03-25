/** 
 *
 */
package eu.estcube.common.helper;

import org.apache.camel.Body;
import org.apache.camel.Handler;
import org.apache.camel.Header;
import org.hbird.business.core.naming.Base;
import org.hbird.business.core.naming.DefaultNaming;
import org.hbird.business.core.naming.Naming;
import org.hbird.exchange.core.Label;

import eu.estcube.common.ByteUtil;
import eu.estcube.common.Headers;
import eu.estcube.domain.transport.tnc.TncFrame;

/**
 *
 */
public class TncFrameToLabel {

    public static final String RELATIVE_PARAMETER_NAME = "TNC Frame";

    public static final String DESCRIPTION = "TNC Frame Dump for debug";

    public static final String HEADER_TIMESTAMP = "jmstimestamp";

    private final String finalParameterName;

    public TncFrameToLabel(String groundStationName) {
        Naming naming = new DefaultNaming();
        finalParameterName = naming.createAbsoluteName(Base.GROUND_STATION, groundStationName, RELATIVE_PARAMETER_NAME);
    }

    @Handler
    public Label process(@Header(HEADER_TIMESTAMP) Long timestamp,
            @Header(Headers.ISSUED_BY) String issuedBy, @Body TncFrame frame) {
        String value = ByteUtil.toHexString(frame.getData());
        Label label = new Label(issuedBy, finalParameterName, DESCRIPTION, value);
        label.setTimestamp(timestamp);
        return label;
    }
}
