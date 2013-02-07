package org.hbird.queuemanagement;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueueHelper {

	/** 		org.apache.activemq:BrokerName=localhost,Destination=hbird.tasks,Type=Queue */
	protected static Pattern canonicalNamePattern = Pattern.compile("(.*?):BrokerName=(.*?),Destination=(.*?),Type=(.*?)");
	
	public static String getQueueName(String canonicalName) {
		Matcher matcher = QueueHelper.canonicalNamePattern.matcher(canonicalName);
		if (matcher.find()) {
			return matcher.group(3);
		}
		
		return null;
	}
	
	/** {scheduledJobId=ID:Admin-VAIO-53276-1355954904327-27:1:1:1:1, timestamp=1355954911269, deliverytime=1355954921269, name=CommandContainerREQ_1, breadcrumbId=ID-Admin-VAIO-53275-1355954903135-1-46, issuedBy=SystemTest, type=CommandContainer} */
	public static Map<String, String> getProperties(String properties) {
		Map<String, String> entries = new HashMap<String, String>();

		for (String element : properties.substring(1, properties.length() - 1).split(", ")) {
			String[] parts = element.split("=");
			entries.put(parts[0], parts[1]);
		}
		
		return entries;
	}
}
