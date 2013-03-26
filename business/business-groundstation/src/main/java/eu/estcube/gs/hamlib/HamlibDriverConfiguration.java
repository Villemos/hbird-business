package eu.estcube.gs.hamlib;

import org.hbird.exchange.groundstation.GroundStationConfigurationBase;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 */
public class HamlibDriverConfiguration extends GroundStationConfigurationBase {

    public HamlibDriverConfiguration() {
    };

    public HamlibDriverConfiguration(String serviceId, String serviceVersion, int heartbeatInterval, String groundstationId, String deviceName,
            String deviceType, int devicePort, String deviceHost, Long timerFireInterval, int pollDelayInterval, int pollRoundingScale) {
        super(serviceId, serviceVersion, heartbeatInterval, groundstationId);
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.devicePort = devicePort;
        this.deviceHost = deviceHost;
        this.timerFireInterval = timerFireInterval;
        this.pollDelayInterval = pollDelayInterval;
        this.pollRoundingScale = pollRoundingScale;
    }

    @Value("${device.name}")
    private String deviceName;

    @Value("${device.type}")
    private String deviceType;

    @Value("${device.port}")
    private int devicePort;

    @Value("${device.host}")
    private String deviceHost;

    @Value("${timer.fire.interval}")
    private Long timerFireInterval;

    @Value("${poll.delay.interval}")
    private int pollDelayInterval;

    @Value("${poll.round.scale}")
    private int pollRoundingScale;

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public int getDevicePort() {
        return devicePort;
    }

    public String getDeviceHost() {
        return deviceHost;
    }

    public Long getTimerFireInterval() {
        return timerFireInterval;
    }

    public int getPollDelayInterval() {
        return pollDelayInterval;
    }

    public int getPollRoundingScale() {
        return pollRoundingScale;
    }

    public String getAddress() {
        return deviceHost + ":" + devicePort;
    }

    public String getComponentId() {
        return deviceType + "_" + getGroundstationId() + "_" + deviceName;
    }
}
