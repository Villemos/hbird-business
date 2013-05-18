package org.hbird.exchange.dataaccess;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hbird.exchange.navigation.Satellite;

public class SatelliteRequest extends DataRequest {

    public static final String DESCRIPTION = "A request for the definition of satellites.";

    private static final long serialVersionUID = 8390710311460399865L;

    /**
	 * 
	 */
    public SatelliteRequest(String issuedBy) {
        super(issuedBy);
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

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
        builder.append("ID", getInstanceID());
        builder.append("name", name);
        builder.append("issuedBy", issuedBy);
        builder.append("destination", destination);
        builder.append("satelliteNames", getNames());
        return builder.build();
    }

}
