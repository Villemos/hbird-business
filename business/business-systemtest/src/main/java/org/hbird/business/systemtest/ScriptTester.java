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

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Handler;
import org.apache.log4j.Logger;
import org.hbird.exchange.core.Label;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.State;

public class ScriptTester extends SystemTest {

    private static org.apache.log4j.Logger LOG = Logger.getLogger(ScriptTester.class);

    @Handler
    public void process() throws InterruptedException {

        LOG.info("------------------------------------------------------------------------------------------------------------");
        LOG.info("Starting");

        /** Create script component from scratch. */
        String script = "var value=in1.asDouble()*10 + in2.asDouble(); output.setValue(value);\n";

        Map<String, String> binding = new HashMap<String, String>();
        binding.put("PARA3", "in1");
        binding.put("PARA4", "in2");
        // FIXME - 27.02.2013, kimmell - compilaton errors
        // injection.sendBody(new StartScriptComponent("ScriptComponent_SYN1", "SYN1", script, new
        // Parameter("ScriptEngine", "SYN1", "Power", "A test script parameter.", new Double(0), "Volt"), binding));

        Thread.sleep(2000);

        /** Send one of the two parameters. Should NOT trigger the script. */
        injection.sendBody(new Parameter("SystemTest", "PARA3", "", "A parameter", 2d, "Volt"));

        Thread.sleep(2000);

        azzert(parameterListener.lastReceived.getName().equals("SYN_PARA1") == false);

        /** Send the other parameter. Should trigger the script. */
        injection.sendBody(new Parameter("SystemTest", "PARA4", "", "A parameter", 5d, "Volt"));

        Thread.sleep(2000);

        azzert(parameterListener.lastReceived.getName().equals("SYN1") == true);
        azzert(parameterListener.lastReceived instanceof Parameter);
        Parameter out = (Parameter) parameterListener.lastReceived;
        azzert(out.getDescription().equals("A test script parameter."));
        azzert(out.getUnit().equals("Volt"));
        azzert((Double) out.getValue() == 25d);

        /** Test the creation of a state parameter based on a script. */
        script = "if (in1.asInt() == 4) {output.setValid()} else {output.setInvalid()}\n";

        binding = new HashMap<String, String>();
        binding.put("PARA5", "in1");

        // FIXME - 27.02.2013, kimmell - compilaton errors
        // injection.sendBody(new StartScriptComponent("ScriptComponent_SYN_STATE1", "SYN_STATE1", script, new
        // State("ScriptEngine", "SYN_STATE1", "", "PARA2", true), binding));

        Thread.sleep(2000);

        injection.sendBody(new Parameter("SystemTest", "PARA5", "", "A parameter", 5d, "Volt"));

        Thread.sleep(2000);

        azzert(stateListener.lastReceived.getName().equals("SYN_STATE1") == true);
        azzert(stateListener.lastReceived instanceof State);
        State out2 = (State) stateListener.lastReceived;
        azzert(out2.getValue() == false);

        injection.sendBody(new Parameter("SystemTest", "PARA5", "", "A parameter", 4d, "Volt"));

        Thread.sleep(2000);

        azzert(stateListener.lastReceived.getName().equals("SYN_STATE1") == true);
        azzert(stateListener.lastReceived instanceof State);
        State out3 = (State) stateListener.lastReceived;
        azzert(out3.getValue() == true);

        /** Test the triggering of a script from the library */
        new HashMap<String, String>();
        binding.put("PARA6", "in1");

        // FIXME - 27.02.2013, kimmell - compilaton errors
        // injection.sendBody(new StartScriptComponent("ScriptComponent3", "Fahrenheit2CelsiusConvertion", binding, new
        // Parameter("ScriptComponent3", "SYN2", "Temperature", "Temperature in CELCIUS.", new Double(0), "Celsius")));

        Thread.sleep(2000);

        injection.sendBody(new Parameter("SystemTest", "PARA6", "Temperature", "The temperature in FAHRENHEIT.", 200d, "Fahrenheit"));

        Thread.sleep(2000);

        azzert(parameterListener.lastReceived.getName().equals("SYN2") == true);
        azzert(parameterListener.lastReceived instanceof Parameter);
        Parameter out4 = (Parameter) parameterListener.lastReceived;
        azzert(out4.getValue().doubleValue() == 93.33333333333333);

        /** Test script that creates a label */
        binding = new HashMap<String, String>();
        binding.put("PARA8", "in1");
        binding.put("PARA7", "threshold");

        // FIXME - 27.02.2013, kimmell - compilaton errors
        // injection.sendBody(new StartScriptComponent("ScriptComponent4", "OnOffSpline", binding, new
        // Label("ScriptEngine", "SYN3", "Battery Status", "Whether the battery is ON or OFF", "ON")));

        Thread.sleep(2000);

        injection.sendBody(new Parameter("SystemTest", "PARA7", "Threshold", "The ON / OFF threshold.", 300d, "Volt"));
        injection.sendBody(new Parameter("SystemTest", "PARA8", "Measurement", "Dont know.", 200d, "Volt"));

        Thread.sleep(2000);

        azzert(labelListener.lastReceived.getName().equals("SYN3") == true);
        azzert(labelListener.lastReceived instanceof Label);
        Label out5 = (Label) labelListener.lastReceived;
        azzert(out5.getValue().equals("OFF"));

        injection.sendBody(new Parameter("test", "PARA8", "Measurement", "Dont know.", 300d, "Volt"));

        Thread.sleep(2000);

        azzert(labelListener.lastReceived.getName().equals("SYN3") == true);
        out5 = (Label) labelListener.lastReceived;
        azzert(out5.getValue().equals("ON"));

        injection.sendBody(new Parameter("test", "PARA8", "Measurement", "Dont know.", 400d, "Volt"));

        Thread.sleep(2000);

        azzert(labelListener.lastReceived.getName().equals("SYN3") == true);
        out5 = (Label) labelListener.lastReceived;
        azzert(out5.getValue().equals("ON"));

        LOG.info("Finished");
    }
}
