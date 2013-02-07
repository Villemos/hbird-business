package org.hbird.business.queuemanagement;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.InvalidSelectorException;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.activemq.broker.jmx.BrokerViewMBean;
import org.apache.activemq.broker.jmx.CompositeDataConstants;
import org.apache.activemq.broker.jmx.QueueViewMBean;
import org.apache.camel.Body;
import org.hbird.queuemanagement.ClearQueue;
import org.hbird.queuemanagement.RemoveQueueElements;
import org.hbird.queuemanagement.ViewQueue;

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.OpenDataException;

/**
 * The queue manager component support the monitoring and control of a activemq queue.
 * 
 * @author Gert Villemos
 *
 */
public class QueueManager {

	/** The connection to the server. */
	protected MBeanServerConnection conn = null;


	/**
	 * Method to get the XBean of the Activemq Broker
	 * 
	 * @return The XBean of the broker
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws MalformedObjectNameException
	 * @throws NullPointerException
	 */
	protected BrokerViewMBean getBrokerBean() throws MalformedURLException, IOException, MalformedObjectNameException, NullPointerException {
		BrokerViewMBean mbean = null;

		/** The ActiveMq BROKER has to be configured to set the 'createConnector' == true. In the activemq.xml file... */
		if (conn == null) {
			JMXConnector jmxc = JMXConnectorFactory.connect(new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:1099/jmxrmi"));
			conn = jmxc.getMBeanServerConnection();
		}

		ObjectName activeMQ = new ObjectName("org.apache.activemq:BrokerName=localhost,Type=Broker");
		mbean = (BrokerViewMBean) MBeanServerInvocationHandler.newProxyInstance(conn, activeMQ, BrokerViewMBean.class, true);

		return mbean;
	}

	/**
	 * Helper method to get the XBean of the queue.
	 * 
	 * @param mbean The XBean of the broker.
	 * @param qname The name of the queue
	 * @return The XBean of the queue
	 */
	protected QueueViewMBean getQueueViewBean(BrokerViewMBean mbean, String qname) {
		/** Find the queue we are monitoring and controlling. */		
		for (ObjectName name : mbean.getQueues()) {
			QueueViewMBean qbean = (QueueViewMBean) MBeanServerInvocationHandler.newProxyInstance(conn, name, QueueViewMBean.class, true);

			if (qbean.getName().equals(qname)) {
				return qbean;
			}
		} 

		return null;
	}


	/**
	 * Method to list all queues in the system.
	 * 
	 * @return List of all queues. The list is a pretty printed queue.
	 * @throws MalformedObjectNameException
	 * @throws MalformedURLException
	 * @throws NullPointerException
	 * @throws IOException
	 */
	public List<String> listQueues() throws MalformedObjectNameException, MalformedURLException, NullPointerException, IOException {

		List<String> queues = new ArrayList<String>();
		for (ObjectName name : getBrokerBean().getQueues()) {
			queues.add(name.getCanonicalName());
		}

		return queues;
	}


	/**
	 * Method to get all elements of a specific queue
	 * 
	 * @param command Command requesting the listing. Holds the name of the queue.
	 * @return
	 * @throws InvalidSelectorException
	 * @throws OpenDataException
	 * @throws MalformedObjectNameException
	 * @throws MalformedURLException
	 * @throws NullPointerException
	 * @throws IOException
	 */
	public Map<String, String> viewQueue(@Body ViewQueue command) throws InvalidSelectorException, OpenDataException, MalformedObjectNameException, MalformedURLException, NullPointerException, IOException {
		return listQueueElements(command.getQueueName());
	}


	/**
	 * Helper method to list all entries in a specific queue.
	 * 
	 * @param qname The name of the queue.
	 * @return A Map keyed on the JMS ID and value being an pretty printed String of element properties.
	 * @throws OpenDataException
	 * @throws MalformedObjectNameException
	 * @throws MalformedURLException
	 * @throws NullPointerException
	 * @throws IOException
	 */
	protected Map<String, String> listQueueElements(String qname) throws OpenDataException, MalformedObjectNameException, MalformedURLException, NullPointerException, IOException {

		/** Go through all messages in the queue. */
		Map<String, String> entries = new HashMap<String, String>();
		for (CompositeData cdata : getQueueViewBean(getBrokerBean(), qname).browse()) {
			entries.put((String) cdata.get("JMSMessageID"), (String) cdata.get(CompositeDataConstants.PROPERTIES));
		} 

		return entries;
	}

	/**
	 * Method to remove a specific set of entries from a queue.
	 * 
	 * @param command The command to remove elements. Contains the JMS ID of the elements to delete and the queue name.
	 * @return A Map defining the current set of elements.
	 * @throws Exception
	 */
	public Map<String, String> removeQueueElements(@Body RemoveQueueElements command) throws Exception {

		QueueViewMBean qbean = getQueueViewBean(getBrokerBean(), command.getQueueName());

		List<String> toDelete = command.getElements();

		/** See if a pattern has been set. If yes, then identify the messages that matches and add them to the list. */
		String pattern = command.getPattern();
		if (pattern != null) {
			for (CompositeData cdata : qbean.browse()) {
				String properties = (String) cdata.get(CompositeDataConstants.PROPERTIES);
				if (properties.matches(pattern)) {
					toDelete.add((String) cdata.get("JMSMessageID"));
				}
			} 
		}

		/** Delete the entries. */
		for (String element : toDelete) {
			qbean.removeMessage(element);
		}

		/** Return the current view of the queue. */
		return listQueueElements(command.getQueueName());
	}


	/**
	 * Method to delete all entries in a queue.
	 * 
	 * @param command The clear command. Contains the name of the queue to be purged.
	 * @return True
	 * @throws Exception
	 */
	public Boolean clearQueue(@Body ClearQueue command) throws Exception {
		getQueueViewBean(getBrokerBean(), command.getQueueName()).purge();
		return true;
	}
}
