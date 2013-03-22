package eu.estcube.domain.config;

import org.springframework.beans.factory.annotation.Value;

/**
 *
 */
public class GroundStationConfigurationBase extends ConfigurationBase {

    public GroundStationConfigurationBase() {
    };

    public GroundStationConfigurationBase(String serviceId, String serviceVersion, int heartbeatInterval,
            String groundstationId) {
        super(serviceId, serviceVersion, heartbeatInterval);
        this.groundstationId = groundstationId;
    }

    @Value("${gs.id}")
    protected String groundstationId;

    public String getGroundstationId() {
        return groundstationId;
    }
}
