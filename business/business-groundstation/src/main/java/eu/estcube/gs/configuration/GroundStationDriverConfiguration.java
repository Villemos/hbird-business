package eu.estcube.gs.configuration;

import org.hbird.exchange.groundstation.GroundStationConfigurationBase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class GroundStationDriverConfiguration extends GroundStationConfigurationBase {

    private static final long serialVersionUID = 2950840425830111029L;

    /** Default value for the pre contact delta. */
    public static final long DEFAULT_PRE_CONTACT_DELTA = 1000L * 60 * 3;

    /** Default value for the post contact delta. */
    public static final long DEFAULT_POST_CONTACT_DELTA = 1000L * 60 * 3;

    /** Default device port. */
    public static final int DEFAULT_DEVICE_PORT = -1;

    /** Default device poll interval. */
    public static final long DEFAULT_DEVICE_POLL_INTERVAL = 1000L * 3;

    /** Default device host. */
    public static final String DEFAULT_DEVICE_HOST = "localhost";

    /** Default value for command interval. */
    public static final long DEFAULT_COMMAND_INTERVAL = 500;

    /** Default value for delay in command group. */
    public static final long DEFAULT_DELAY_IN_COMMAND_GROUP = 20;

    /** Name of the device. */
    @Value("${device.name}")
    protected String deviceName;

    /** Type of the device. */
    @Value("${device.type}")
    protected String deviceType;

    @Value("${device.port:-1}")
    protected int devicePort = DEFAULT_DEVICE_PORT;

    @Value("${device.host:localhost}")
    protected String deviceHost = DEFAULT_DEVICE_HOST;

    /** Used to poll status info from the device. */
    @Value("${device.poll.interval:3000}")
    protected long devicePollInterval = DEFAULT_DEVICE_POLL_INTERVAL;

    /** Milliseconds before the contact when setup command has to be issued. */
    @Value("${contact.pre.delta:180000}")
    protected long preContactDelta = DEFAULT_PRE_CONTACT_DELTA;

    /** Milliseconds after the contact when stop command has to be issued. */
    @Value("${contact.post.delta:180000}")
    protected long postContactDelta = DEFAULT_POST_CONTACT_DELTA;

    @Value("${radio.command.interval:500}")
    protected long commandInterval = DEFAULT_COMMAND_INTERVAL;

    @Value("${radio.command.group.delay:20}")
    protected long delayInCommandGroup = DEFAULT_DELAY_IN_COMMAND_GROUP;

    public GroundStationDriverConfiguration() {
        super();
    }

    public GroundStationDriverConfiguration(String serviceId, String serviceVersion, long heartbeatInterval, String groundstationId, String deviceName,
            String deviceType, int devicePort, String deviceHost, long devicePollInterval, long preContactDelta, long postContactDelta) {
        super(serviceId, serviceVersion, heartbeatInterval, groundstationId);
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.devicePort = devicePort;
        this.deviceHost = deviceHost;
        this.devicePollInterval = devicePollInterval;
        this.preContactDelta = preContactDelta;
        this.postContactDelta = postContactDelta;
    }

    /**
     * @return the deviceName
     */
    public String getDeviceName() {
        return deviceName;
    }

    /**
     * @param deviceName the deviceName to set
     */
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    /**
     * @return the deviceType
     */
    public String getDeviceType() {
        return deviceType;
    }

    /**
     * @param deviceType the deviceType to set
     */
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    /**
     * @return the devicePort
     */
    public int getDevicePort() {
        return devicePort;
    }

    /**
     * @param devicePort the devicePort to set
     */
    public void setDevicePort(int devicePort) {
        this.devicePort = devicePort;
    }

    /**
     * @return the deviceHost
     */
    public String getDeviceHost() {
        return deviceHost;
    }

    /**
     * @param deviceHost the deviceHost to set
     */
    public void setDeviceHost(String deviceHost) {
        this.deviceHost = deviceHost;
    }

    /**
     * @return the pollDelayInterval
     */
    public long getDevicePollInterval() {
        return devicePollInterval;
    }

    /**
     */
    public void setDevicePollInterval(long devicePollInterval) {
        this.devicePollInterval = devicePollInterval;
    }

    /**
     * @return the preContactDelta
     */
    public long getPreContactDelta() {
        return preContactDelta;
    }

    /**
     * @param preContactDelta the preContactDelta to set
     */
    public void setPreContactDelta(long preContactDelta) {
        this.preContactDelta = preContactDelta;
    }

    /**
     * @return the postContactDelta
     */
    public long getPostContactDelta() {
        return postContactDelta;
    }

    /**
     * @param postContactDelta the postContactDelta to set
     */
    public void setPostContactDelta(long postContactDelta) {
        this.postContactDelta = postContactDelta;
    }

    /**
     * @return the commandInterval
     */
    public long getCommandInterval() {
        return commandInterval;
    }

    /**
     * @param commandInterval the commandInterval to set
     */
    public void setCommandInterval(long commandInterval) {
        this.commandInterval = commandInterval;
    }

    /**
     * @return the delayInCommandGroup
     */
    public long getDelayInCommandGroup() {
        return delayInCommandGroup;
    }

    /**
     * @param delayInCommandGroup the delayInCommandGroup to set
     */
    public void setDelayInCommandGroup(long delayInCommandGroup) {
        this.delayInCommandGroup = delayInCommandGroup;
    }

    public String getAddress() {
        return deviceHost + ":" + devicePort;
    }
}
