package org.hbird.business.systemtest;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Handler;
import org.hbird.exchange.configurator.StartScriptComponent;
import org.hbird.exchange.core.Label;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.State;
import org.hbird.exchange.scripting.ScriptExecutionRequest;

public class ScriptTester extends Tester {

	@Handler
	public void process() throws InterruptedException {

		/** Create script component from scratch. */
		String script = "var value=in1.asDouble()*10 + in2.asDouble(); output.setValue(value);\n";

		Map<String, String> binding = new HashMap<String, String>();
		binding.put("PARA3", "in1");
		binding.put("PARA4", "in2");
		injection.sendBody(new StartScriptComponent("ScriptComponent_SYN1", "SYN1", script, new Parameter("ScriptEngine", "SYN1", "Power", "A test script parameter.", new Double(0), "Volt"), binding));

		Thread.sleep(2000);

		/** Send one of the two parameters. Should NOT trigger the script. */
		injection.sendBody(new Parameter("SystemTest", "PARA3", "", "A parameter", 2d, "Volt"));

		Thread.sleep(2000);

		azzert(monitoringListener.lastReceived.getName().equals("SYN_PARA1") == false);

		/** Send the other parameter. Should trigger the script. */
		injection.sendBody(new Parameter("SystemTest", "PARA4", "", "A parameter", 5d, "Volt"));

		Thread.sleep(2000);

		azzert(monitoringListener.lastReceived.getName().equals("SYN1") == true);
		azzert(monitoringListener.lastReceived instanceof Parameter);
		Parameter out = (Parameter) monitoringListener.lastReceived;
		azzert(out.getDescription().equals("A test script parameter."));
		azzert(out.getUnit().equals("Volt"));
		azzert((Double) out.getValue() == 25d);



		/** Test the creation of a state parameter based on a script. */
		script = "if (in1.asInt() == 4) {output.setValid()} else {output.setInvalid()}\n";

		binding = new HashMap<String, String>();
		binding.put("PARA5", "in1");

		injection.sendBody(new StartScriptComponent("ScriptComponent_SYN_STATE1", "SYN_STATE1", script, new State("ScriptEngine", "SYN_STATE1", "", "PARA2", true), binding));

		Thread.sleep(2000);

		injection.sendBody(new Parameter("SystemTest", "PARA5", "", "A parameter", 5d, "Volt"));

		Thread.sleep(2000);

		azzert(monitoringListener.lastReceived.getName().equals("SYN_STATE1") == true);
		azzert(monitoringListener.lastReceived instanceof State);
		State out2 = (State) monitoringListener.lastReceived;
		azzert(out2.getValue() == false);


		injection.sendBody(new Parameter("SystemTest", "PARA5", "", "A parameter", 4d, "Volt"));

		Thread.sleep(2000);

		azzert(monitoringListener.lastReceived.getName().equals("SYN_STATE1") == true);
		azzert(monitoringListener.lastReceived instanceof State);
		State out3 = (State) monitoringListener.lastReceived;
		azzert(out3.getValue() == true);



		/** Test the triggering of a script from the library */			
		new HashMap<String, String>();
		binding.put("PARA6", "in1");

		injection.sendBody(new StartScriptComponent("ScriptComponent3", "Fahrenheit2CelsiusConvertion", binding, new Parameter("ScriptComponent3", "SYN2", "Temperature", "Temperature in CELCIUS.", new Double(0), "Celsius")));

		Thread.sleep(2000);

		injection.sendBody(new Parameter("SystemTest", "PARA6", "Temperature", "The temperature in FAHRENHEIT.", 200d, "Fahrenheit"));

		Thread.sleep(2000);

		azzert(monitoringListener.lastReceived.getName().equals("SYN2") == true);
		azzert(monitoringListener.lastReceived instanceof Parameter);
		Parameter out4 = (Parameter) monitoringListener.lastReceived;
		azzert(out4.getValue().doubleValue() == 93.33333333333333);





		/** Test script that creates a label*/
		binding = new HashMap<String, String>();
		binding.put("PARA8", "in1");
		binding.put("PARA7", "threshold");
		
		injection.sendBody(new StartScriptComponent("ScriptComponent4", "OnOffSpline", binding, new Label("ScriptEngine", "SYN3", "Battery Status", "Whether the battery is ON or OFF", "ON")));

		Thread.sleep(2000);
		
		injection.sendBody(new Parameter("SystemTest", "PARA7", "Threshold", "The ON / OFF threshold.", 300d, "Volt"));
		injection.sendBody(new Parameter("SystemTest", "PARA8", "Measurement", "Dont know.", 200d, "Volt"));

		Thread.sleep(2000);

		azzert(monitoringListener.lastReceived.getName().equals("SYN3") == true);
		azzert(monitoringListener.lastReceived instanceof Label);
		Label out5 = (Label) monitoringListener.lastReceived;
		azzert(out5.getValue().equals("OFF"));

		injection.sendBody(new Parameter("test", "PARA8", "Measurement", "Dont know.", 300d, "Volt"));
		
		Thread.sleep(2000);

		azzert(monitoringListener.lastReceived.getName().equals("SYN3") == true);
		out5 = (Label) monitoringListener.lastReceived;
		azzert(out5.getValue().equals("ON"));

		injection.sendBody(new Parameter("test", "PARA8", "Measurement", "Dont know.", 400d, "Volt"));

		Thread.sleep(2000);

		azzert(monitoringListener.lastReceived.getName().equals("SYN3") == true);
		out5 = (Label) monitoringListener.lastReceived;
		azzert(out5.getValue().equals("ON"));
	}
}
