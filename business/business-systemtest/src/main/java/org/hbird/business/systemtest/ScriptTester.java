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
import org.hbird.business.scripting.ScriptComponent;
import org.hbird.exchange.core.EntityInstance;
import org.hbird.exchange.core.Label;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.Part;
import org.hbird.exchange.core.State;

public class ScriptTester extends SystemTest {

    private static org.apache.log4j.Logger LOG = Logger.getLogger(ScriptTester.class);

    @Handler
    public void process() throws InterruptedException {

        LOG.info("------------------------------------------------------------------------------------------------------------");
        LOG.info("Starting");

        /** Create script component from scratch. */
        String script = "var value=in1.asDouble()*10 + in2.asDouble(); output.setValue(value);\n";

        String scriptName = "SYN1";
        //
        Part scripts = parts.get("Synthetic Parameters");

        Map<String, String> binding = new HashMap<String, String>();
        binding.put("PARA3", "in1");
        binding.put("PARA4", "in2");

        Parameter parameter = new Parameter("SYN1", "SYN1");
        parameter.setDescription("A test script parameter.");
        parameter.setUnit("Volt");

        ScriptComponent com = createScriptComponent("SCRIPT1", script, scriptName, binding, parameter);
        partmanagerApi.start(com);
        Thread.sleep(2000);

        /** Send one of the two parameters. Should NOT trigger the script. */
        publishApi.publishParameter("PARA3", "PARA3", "A parameter", 2d, "Volt");

        Thread.sleep(2000);

        azzert(parameterListener.lastReceived.getName().equals("SYN_PARA1") == false);

        /** Send the other parameter. Should trigger the script. */
        publishApi.publishParameter("PARA4", "PARA4", "A parameter", 5d, "Volt");

        Thread.sleep(2000);

        azzert(parameterListener.lastReceived.getName().equals("SYN1") == true);
        azzert(parameterListener.lastReceived instanceof Parameter);
        Parameter out = (Parameter) parameterListener.lastReceived;
        azzert(out.getDescription().equals("A test script parameter."));
        azzert(out.getUnit().equals("Volt"));
        azzert((Double) out.getValue() == 25d);

        /** Test the creation of a state parameter based on a script. */
        script = "if (in1.asInt() == 4) {output.setValue(true)} else {output.setValue(false)}\n";

        binding = new HashMap<String, String>();
        binding.put("PARA5", "in1");

        State state = new State("SYN_STATE_1", "SYN_STATE_1");
        state.setApplicableTo("PARA2");

        com = createScriptComponent("SCRIPT2", script, scriptName, binding, state);
        partmanagerApi.start(com);

        Thread.sleep(2000);

        publishApi.publishParameter("PARA5", "PARA5", "A parameter", 5d, "Volt");

        Thread.sleep(2000);

        azzert(stateListener.lastReceived.getName().equals("SYN_STATE_1") == true);
        azzert(stateListener.lastReceived instanceof State);
        State out2 = (State) stateListener.lastReceived;
        azzert(out2.getValue() == false);

        publishApi.publishParameter("PARA5", "PARA5", "A parameter", 4d, "Volt");

        Thread.sleep(2000);

        azzert(stateListener.lastReceived.getName().equals("SYN_STATE_1") == true);
        azzert(stateListener.lastReceived instanceof State);
        State out3 = (State) stateListener.lastReceived;
        azzert(out3.getValue() == true);

        /** Test the triggering of a script from the library */
        new HashMap<String, String>();
        binding.put("PARA6", "in1");

        script = null;
        scriptName = "Fahrenheit2CelsiusConvertion";

        parameter = new Parameter("SYN2", "SYN2");
        parameter.setDescription("Temperature in CELCIUS.");
        parameter.setValue(new Double(0));
        parameter.setUnit("Celsius");

        com = createScriptComponent("SCRIPT3", script, scriptName, binding, parameter);
        partmanagerApi.start(com);

        Thread.sleep(2000);

        publishApi.publishParameter("PARA6", "PARA6", "The temperature in FAHRENHEIT.", 200d, "Fahrenheit");

        Thread.sleep(2000);

        azzert(parameterListener.lastReceived.getName().equals("SYN2") == true);
        azzert(parameterListener.lastReceived instanceof Parameter);
        Parameter out4 = (Parameter) parameterListener.lastReceived;
        azzert(out4.getValue().doubleValue() == 93.33333333333333);

        /** Test script that creates a label */
        binding = new HashMap<String, String>();
        binding.put("PARA8", "in1");
        binding.put("PARA7", "threshold");

        script = null;
        scriptName = "OnOffSpline";

        Label label = new Label("SYN3", "SYN3");
        label.setDescription("Whether the battery is ON or OFF");
        label.setValue("ON");

        com = createScriptComponent("SCRIPT4", script, scriptName, binding, label);
        partmanagerApi.start(com);

        Thread.sleep(2000);

        publishApi.publishParameter("PARA7", "PARA7", "The ON / OFF threshold.", 300d, "Volt");
        publishApi.publishParameter("PARA8", "PARA8", "Dont know.", 200d, "Volt");

        Thread.sleep(2000);

        azzert(labelListener.lastReceived.getName().equals("SYN3") == true);
        azzert(labelListener.lastReceived instanceof Label);
        Label out5 = (Label) labelListener.lastReceived;
        azzert(out5.getValue().equals("OFF"));

        publishApi.publishParameter("PARA8", "PARA8", "Dont know.", 300d, "Volt");

        Thread.sleep(2000);

        azzert(labelListener.lastReceived.getName().equals("SYN3") == true);
        out5 = (Label) labelListener.lastReceived;
        azzert(out5.getValue().equals("ON"));

        publishApi.publishParameter("PARA8", "PARA8", "Dont know.", 400d, "Volt");

        Thread.sleep(2000);

        azzert(labelListener.lastReceived.getName().equals("SYN3") == true);
        out5 = (Label) labelListener.lastReceived;
        azzert(out5.getValue().equals("ON"));

        LOG.info("Finished");
    }

    public ScriptComponent createScriptComponent(String componentName, String script, String scriptName, Map<String, String> binding, EntityInstance obj) {
        ScriptComponent component = new ScriptComponent(componentName, componentName);
        component.setScript(script);
        component.setScriptName(scriptName);
        component.setBinding(binding);
        component.setReturnType(obj);
        return component;
    }
}
