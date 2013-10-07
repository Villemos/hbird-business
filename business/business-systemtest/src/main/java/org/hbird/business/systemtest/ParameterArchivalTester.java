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
package org.hbird.business.systemtest;

import java.util.Date;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.Handler;
import org.apache.log4j.Logger;
import org.hbird.exchange.core.Parameter;

public class ParameterArchivalTester extends SystemTest {

    private static org.apache.log4j.Logger LOG = Logger.getLogger(ParameterArchivalTester.class);

    private Parameter createParameter(String ID, String name, String description, Number value, String unit) {
        Parameter param = new Parameter(ID, name);
        param.setDescription(description);
        param.setValue(value);
        param.setUnit(unit);

        return param;
    }

    @Handler
    public void process(CamelContext context) throws Exception {

        LOG.info("------------------------------------------------------------------------------------------------------------");
        LOG.info("Starting");

        startMonitoringArchive();

        Thread.sleep(2000);

        String para1Name = "PARA1";
        String para2Name = "PARA2";
        String para3Name = "PARA3";

        /** Publish parameters. */
        LOG.info("Publishing parameters.");
        publishApi.publish(createParameter(para1Name, para1Name, "A test description,", 2d, "Volt"));
        Thread.sleep(1);
        publishApi.publish(createParameter(para1Name, para1Name, "A test description,", 2.1d, "Volt"));
        Thread.sleep(1);
        publishApi.publish(createParameter(para1Name, para1Name, "A test description,", 2.2d, "Volt"));
        Thread.sleep(1);
        publishApi.publish(createParameter(para1Name, para1Name, "A test description,", 2.3d, "Volt"));
        // Thread.sleep(1);
        publishApi.publish(createParameter(para1Name, para1Name, "A test description,", 2.4d, "Volt"));
        Thread.sleep(1);
        publishApi.publish(createParameter(para1Name, para1Name, "A test description,", 2.5d, "Volt"));
        Thread.sleep(1);
        publishApi.publish(createParameter(para1Name, para1Name, "A test description,", 2.6d, "Volt"));
        Thread.sleep(1);
        publishApi.publish(createParameter(para2Name, para2Name, "A test description,", 2l, "Meter"));

        /** Make sure we have different timestamps. */
        Thread.sleep(1);
        Date start = new Date();
        Thread.sleep(1);

        publishApi.publish(createParameter(para2Name, para2Name, "A test description,", 3l, "Meter"));
        Thread.sleep(1);
        publishApi.publish(createParameter(para2Name, para2Name, "A test description,", 4l, "Meter"));
        Thread.sleep(1);
        publishApi.publish(createParameter(para3Name, para3Name, "A test description,", 10f, "Seconds"));
        Thread.sleep(1);
        publishApi.publish(createParameter(para3Name, para3Name, "A test description,", 15f, "Seconds"));
        Thread.sleep(1);
        publishApi.publish(createParameter(para3Name, para3Name, "A test description,", 20f, "Seconds"));

        Thread.sleep(1);
        Date end = new Date();
        Thread.sleep(1);

        publishApi.publish(createParameter(para2Name, para2Name, "A test description,", 5l, "Meter"));
        Thread.sleep(1);
        publishApi.publish(createParameter(para3Name, para3Name, "A test description,", 35f, "Seconds"));

        Thread.sleep(2000);

        /** Check whether they were published on ActiveMQ to the listener. */
        azzert(parameterListener.elements.size() == 15, "Expect to receive 15 parameters. Received " + parameterListener.elements.size());

        /** Test retrieval. */

        // Test retrieval of only the last value of a parameter.
        try {
            LOG.info("Retrieveing last value of PARA1");

            Parameter respond = accessApi.getById(para1Name, Parameter.class);
            azzert(respond != null, "Received a response.");

            azzert(respond.asDouble() == 2.6d, "Last value should be 2.6. Received " + respond.asDouble());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // Test retrieval of a lower bound time range for one parameter.
        try {
            List<Parameter> respond = accessApi.getById(para2Name, start.getTime(), (new Date()).getTime(), Parameter.class);
            azzert(respond != null, "Received a response.");
            azzert(respond.size() == 3, "Expect 3 entries. Received " + respond.size());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // Test retrieval of a lower bound time range for one parameter.
        try {
            List<Parameter> respond = accessApi.getById(para2Name, start.getTime(), end.getTime(), Parameter.class);
            azzert(respond != null, "Received a response.");
            azzert(respond.size() == 2, "Expect 2 entries. Received " + respond.size());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // Test retrieval of a lower bound time range for one parameter.
        /*
         * try {
         * List<Parameter> respond = accessApi.getParameters(Arrays.asList(para1Name, para2Name, para3Name),
         * start.getTime(), end.getTime());
         * azzert(respond != null, "Received a response.");
         * azzert(respond.size() == 5, "Expect 5 entries. Received " + respond.size());
         * }
         * catch (Exception e) {
         * e.printStackTrace();
         * }
         */

        LOG.info("Finished");
    }
}
