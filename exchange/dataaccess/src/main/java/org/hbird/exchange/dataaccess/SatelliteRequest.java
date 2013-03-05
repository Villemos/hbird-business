package org.hbird.exchange.dataaccess;

import java.util.ArrayList;
import java.util.List;

import org.hbird.exchange.constants.StandardComponents;
import org.hbird.exchange.navigation.Satellite;

public class SatelliteRequest extends DataRequest {

    public static final String DESCRIPTION = "A request for the definition of satellites.";

    private static final long serialVersionUID = 8390710311460399865L;

    /**
	 * 
	 */
    public SatelliteRequest(String issuedBy) {
        super(issuedBy, StandardComponents.ARCHIVE, SatelliteRequest.class.getSimpleName(), DESCRIPTION);
        setClass(Satellite.class.getSimpleName());
        setIsInitialization(true);
    }

    public SatelliteRequest(String issuedBy, List<String> satellites) {
        this(issuedBy);
        addNames(satellites);
    }

    public SatelliteRequest(String issuedBy, String satellite) {
        this(issuedBy);
        List<String> satellites = new ArrayList<String>();
        satellites.add(satellite);
        addNames(satellites);
    }
}
