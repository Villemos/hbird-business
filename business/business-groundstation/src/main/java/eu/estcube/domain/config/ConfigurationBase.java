package eu.estcube.domain.config;

import org.springframework.beans.factory.annotation.Value;

/**
 * Base class for configuration objects.
 * 
 * Component assemblies are responsible for loading property files to fill
 * values in configuration objects.
 * 
 * Example snippet from Spring/Camel context file: <code>
 * <pre>
 *    &lt;bean id="propertyPlaceholder" class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer"&gt;
 *       &lt;property name="systemPropertiesModeName" value="SYSTEM_PROPERTIES_MODE_OVERRIDE"/&gt;
 *       &lt;property name="ignoreResourceNotFound" value="true"/&gt;
 *       &lt;property name="locations"&gt;
 *           &lt;list&gt;
 *               &lt;value&gt;classpath:service.properties&lt;/value&gt;
 *               &lt;value&gt;file:service.properties&lt;/value&gt;
 *           &lt;/list&gt;
 *       &lt;/property&gt;
 *   &lt;/bean&gt;
 * </pre>
 * </code>
 * 
 * In this snippet properties are loaded from <tt>service.properties</tt> file
 * from classpath and if found from file <tt>service.properties</tt>.
 * Values from the file override values in the classpath. No properties mixing
 * is taking place - all values has to be present in both files to work
 * properly.
 */
public class ConfigurationBase {

    public ConfigurationBase() {
    };

    public ConfigurationBase(String serviceId, String serviceVersion, int heartBeatInterval) {
        super();
        this.serviceId = serviceId;
        this.serviceVersion = serviceVersion;
        this.heartBeatInterval = heartBeatInterval;
    }

    @Value("${service.id}")
    protected String serviceId;

    @Value("${service.version}")
    protected String serviceVersion;

    @Value("${heart.beat.interval}")
    protected int heartBeatInterval;

    public String getServiceVersion() {
        return serviceVersion;
    }

    public String getServiceId() {
        return serviceId;
    }

    public int getHeartBeatInterval() {
        return heartBeatInterval;
    }
}
