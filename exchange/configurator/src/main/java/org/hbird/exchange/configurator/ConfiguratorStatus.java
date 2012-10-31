package org.hbird.exchange.configurator;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.hbird.exchange.core.Named;


/**
 * Status message of a configurator component. Reports the details of the platform
 * it runs on, its runtime and the components within it.
 * 
 * @author Admin
 *
 */
public class ConfiguratorStatus extends Named {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8719570531717760268L;

	/** The following attributes of Named will be set;
	 *  - Name. The name of the configurator. 
	 *  - Description. A description of this message. 
	 *  - Timestamp. The time the message was issued. 
	 */

	public ConfiguratorStatus(String configuratorName, List<String> components) {
		super(configuratorName, configuratorName, "A status of the configurator, the host it runs on and the components it contains.");
		this.components = components;
	}

	
	{
		try {			
			hostOsArch = ManagementFactory.getOperatingSystemMXBean().getArch();
			hostOsName = ManagementFactory.getOperatingSystemMXBean().getName();
			hostOsVersion = ManagementFactory.getOperatingSystemMXBean().getVersion();
		
			startTime = ManagementFactory.getRuntimeMXBean().getStartTime();
			upTime = ManagementFactory.getRuntimeMXBean().getUptime();
			
			vmName = ManagementFactory.getRuntimeMXBean().getVmName();
			vmVendor = ManagementFactory.getRuntimeMXBean().getVmVendor();
			vmVersion = ManagementFactory.getRuntimeMXBean().getVmVersion();
			
			hostip = InetAddress.getLocalHost().getHostAddress();
			hostname = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}		
	}

	/** The time that this VM was started. */
	public long startTime = 0;
	
	/** The uptime of this VM. */
	public long upTime = 0;
	
	/** The hostname where the configurator is running. */
	public String hostname;
	
	/** The IP address of the host that the configurator is running on. */
	public String hostip;
	
	/** The OS architecture that the configurator is running on. */
	public String hostOsArch;

	/** The name of the OS that the configurator is running on. */
	public String hostOsName;

	/** The OS version that the configurator is running on. */
	public String hostOsVersion;

	/** Name of the VM in which the configurator is running. */
	public String vmName;
	
	/** Name of the vendor of the VM*/
	public String vmVendor;
	
	/** Version of the VM. */
	public String vmVersion;
	
	/** List of components running in this configurator. */
	public List<String> components = null; 
}
