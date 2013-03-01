/**
 * Licensed to the Hummingbird Foundation (HF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The HF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hbird.business.queuemanagement.api;

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
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.OpenDataException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.activemq.broker.jmx.BrokerViewMBean;
import org.apache.activemq.broker.jmx.CompositeDataConstants;
import org.apache.activemq.broker.jmx.QueueViewMBean;
import org.apache.activemq.broker.jmx.TopicViewMBean;
import org.hbird.business.api.HbirdApi;
import org.hbird.business.api.IQueueManagement;

/**
 * The queue manager component support the monitoring and control of a activemq queue.
 * 
 * @author Gert Villemos
 * 
 */
public class QueueManagerApi extends HbirdApi implements IQueueManagement {

    private static final String KEY_JMS_MESSAGE_ID = "JMSMessageID";

    private static final String KEY_DESTINATION = "Destination";

    /** The connection to the server. */
    protected MBeanServerConnection conn = null;

    public QueueManagerApi(String issuedBy) {
        super(issuedBy);
    }

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
        mbean = MBeanServerInvocationHandler.newProxyInstance(conn, activeMQ, BrokerViewMBean.class, true);

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
            QueueViewMBean qbean = MBeanServerInvocationHandler.newProxyInstance(conn, name, QueueViewMBean.class, true);

            if (qbean.getName().matches(qname)) {
                return qbean;
            }
        }

        return null;
    }

    /**
     * Helper method to get the XBean of the queue.
     * 
     * @param mbean The XBean of the broker.
     * @param qname The name of the queue
     * @return The XBean of the queue
     */
    protected TopicViewMBean getTopicViewBean(BrokerViewMBean mbean, String qname) {
        /** Find the queue we are monitoring and controlling. */
        for (ObjectName name : mbean.getTopics()) {
            TopicViewMBean qbean = (TopicViewMBean) MBeanServerInvocationHandler.newProxyInstance(conn, name, QueueViewMBean.class, true);

            if (qbean.getName().matches(qname)) {
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
    @Override
    public List<String> listQueues() throws MalformedObjectNameException, MalformedURLException, NullPointerException, IOException {

        List<String> queues = new ArrayList<String>();
        for (ObjectName name : getBrokerBean().getQueues()) {
            queues.add(name.getKeyProperty(KEY_DESTINATION));
        }

        return queues;
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
    @Override
    public List<String> listTopics() throws MalformedObjectNameException, MalformedURLException, NullPointerException, IOException {

        List<String> topics = new ArrayList<String>();
        for (ObjectName name : getBrokerBean().getTopics()) {
            topics.add(name.getKeyProperty(KEY_DESTINATION));
        }

        return topics;
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
    @Override
    public Map<String, String> viewQueue(String queueName) throws InvalidSelectorException, OpenDataException, MalformedObjectNameException,
            MalformedURLException, NullPointerException, IOException {
        return listQueueElements(queueName);
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
    protected Map<String, String> listQueueElements(String qname) throws OpenDataException, MalformedObjectNameException, MalformedURLException,
            NullPointerException, IOException {

        /** Go through all messages in the queue. */
        Map<String, String> entries = new HashMap<String, String>();
        QueueViewMBean bean = getQueueViewBean(getBrokerBean(), qname);
        if (bean != null) {
            for (CompositeData cdata : bean.browse()) {
                entries.put((String) cdata.get(KEY_JMS_MESSAGE_ID), (String) cdata.get(CompositeDataConstants.PROPERTIES));
            }
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
    @Override
    public Map<String, String> removeQueueElements(String queueName, String pattern) throws Exception {

        QueueViewMBean qbean = getQueueViewBean(getBrokerBean(), queueName);

        List<String> toDelete = new ArrayList<String>();

        /** See if a pattern has been set. If yes, then identify the messages that matches and add them to the list. */
        if (pattern != null) {
            for (CompositeData cdata : qbean.browse()) {
                String properties = (String) cdata.get(CompositeDataConstants.PROPERTIES);
                if (properties.matches(pattern)) {
                    toDelete.add((String) cdata.get(KEY_JMS_MESSAGE_ID));
                }
            }
        }

        /** Delete the entries. */
        for (String element : toDelete) {
            qbean.removeMessage(element);
        }

        /** Return the current view of the queue. */
        return listQueueElements(queueName);
    }

    /**
     * Method to delete all entries in a queue.
     * 
     * @param command The clear command. Contains the name of the queue to be purged.
     * @return True
     * @throws Exception
     */
    @Override
    public Boolean clearQueue(String queueName) throws Exception {
        QueueViewMBean bean = getQueueViewBean(getBrokerBean(), queueName);
        if (bean != null) {
            bean.purge();
        }

        return true;
    }

    /**
     * Method to delete all entries in a queue.
     * 
     * @param command The clear command. Contains the name of the queue to be purged.
     * @return True
     * @throws Exception
     */
    @Override
    public Boolean clearTopic(String topicName) throws Exception {
        /** We cannot purge topics. Instead we remove it. */
        getBrokerBean().removeTopic(topicName);
        return true;
    }

    @Override
    public boolean clearAll() {
        try {
            BrokerViewMBean mbean = getBrokerBean();

            for (String queue : listQueues()) {
                clearQueue(queue);
            }
            for (String topic : listTopics()) {
                clearTopic(topic);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
}
