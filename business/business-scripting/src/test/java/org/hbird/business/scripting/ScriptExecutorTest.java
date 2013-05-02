package org.hbird.business.scripting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.hbird.business.scripting.bean.ScriptExecutor;
import org.hbird.exchange.core.D3Vector;
import org.hbird.exchange.core.Label;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.State;
import org.hbird.exchange.groundstation.GroundStation;
import org.hbird.exchange.scripting.ScriptExecutionRequest;
import org.junit.Test;

public class ScriptExecutorTest {

	// TODO Fix tests
	
//    @Test
//    public void testDoubleParameter() {
//        String script = "var value=in1.asDouble()*10 + in2.asDouble(); output.setValue(value);\n";
//
//        Map<String, String> binding = new HashMap<String, String>();
//        binding.put("Parameter1", "in1");
//        binding.put("Parameter2", "in2");
//        ScriptExecutionRequest request = new ScriptExecutionRequest("", script, "javascript", new Parameter("ScriptEngine", "Parameter10",
//                "A test script parameter.", new Double(0), "Volt"), binding);
//
//        ScriptExecutor executor = new ScriptExecutor(request);
//
//        Object out = executor.calculate(new Parameter("test", "Parameter1", "A parameter", 2d, "Volt"));
//        assertNull(out);
//
//        out = executor.calculate(new Parameter("test", "Parameter1", "A parameter", 3d, "Volt"));
//        assertNull(out);
//
//        out = executor.calculate(new Parameter("test", "Parameter2", "A parameter", 5d, "Volt"));
//        assertNotNull(out);
//        assertTrue(out instanceof Parameter);
//        assertEquals("Parameter10", ((Parameter) out).getName());
//        assertEquals("A test script parameter.", ((Parameter) out).getDescription());
//        assertEquals("Volt", ((Parameter) out).getUnit());
//        assertEquals(35D, (Double) ((Parameter) out).getValue(), 0.0D);
//    }
//
//    @Test
//    public void testStateParameter() {
//
//        String script = "if (in1.asInt() == 4) {output.setValid()} else {output.setInvalid()}\n";
//
//        Map<String, String> binding = new HashMap<String, String>();
//        binding.put("Parameter1", "in1");
//        ScriptExecutionRequest request = new ScriptExecutionRequest("", script, "javascript", new State("ScriptEngine", "TestState", "", "Parameter100", true),
//                binding);
//
//        ScriptExecutor executor = new ScriptExecutor(request);
//
//        State out = (State) executor.calculate(new Parameter("test", "Parameter1", "A parameter", 5d, "Volt"));
//        assertNotNull(out);
//        assertTrue(out instanceof State);
//        assertEquals("Parameter100", out.getIsStateOf());
//        assertFalse(out.getValue());
//
//        out = (State) executor.calculate(new Parameter("test", "Parameter1", "A parameter", 4d, "Volt"));
//        assertNotNull(out);
//        assertTrue(out instanceof State);
//        assertEquals("Parameter100", out.getIsStateOf());
//        assertTrue(out.getValue());
//    }
//
//    @Test
//    public void testLibraryScript() {
//        Map<String, String> binding = new HashMap<String, String>();
//        binding.put("Parameter201", "in1");
//        ScriptExecutionRequest request = new ScriptExecutionRequest("Fahrenheit2CelsiusConvertion", "", "javascript", new Parameter("ScriptEngine",
//                "Parameter200", "Temperature in CELCIUS.", new Double(0), "Celsius"), binding);
//
//        ScriptExecutor executor = new ScriptExecutor(request);
//
//        Parameter out = (Parameter) executor.calculate(new Parameter("test", "Parameter201", "The temperature in FAHRENHEIT.", 200d,
//                "Fahrenheit"));
//        assertNotNull(out);
//        assertTrue(out instanceof Parameter);
//        assertEquals("Parameter200", out.getName());
//        assertEquals("Celsius", out.getUnit());
//        assertEquals(93.33333333333333D, (Double) out.getValue(), 0.0D);
//
//    }
//
//    @Test
//    public void testLibraryLabelScript() {
//        Map<String, String> binding = new HashMap<String, String>();
//        binding.put("Parameter300", "in1");
//        binding.put("Parameter301", "threshold");
//        ScriptExecutionRequest request = new ScriptExecutionRequest("OnOffSpline", "", "javascript", new Label("ScriptEngine", "Parameter300",
//                "Whether the battery is ON or OFF", "ON"), binding);
//
//        ScriptExecutor executor = new ScriptExecutor(request);
//
//        Label out = (Label) executor.calculate(new Parameter("test", "Parameter301", "The ON / OFF threshold.", 300d, "Volt"));
//
//        out = (Label) executor.calculate(new Parameter("test", "Parameter300", "Dont know.", 200d, "Volt"));
//
//        assertNotNull(out);
//        assertTrue(out instanceof Label);
//        assertEquals("Parameter300", out.getName());
//        assertEquals("OFF", out.getValue());
//
//        out = (Label) executor.calculate(new Parameter("test", "Parameter300", "Dont know.", 300d, "Volt"));
//
//        assertNotNull(out);
//        assertTrue(out instanceof Label);
//        assertEquals("Parameter300", out.getName());
//        assertEquals("ON", out.getValue());
//
//        out = (Label) executor.calculate(new Parameter("test", "Parameter300", "Dont know.", 400d, "Volt"));
//
//        assertNotNull(out);
//        assertTrue(out instanceof Label);
//        assertEquals("Parameter300", out.getName());
//        assertEquals("ON", out.getValue());
//    }
//
//    @Test
//    public void testLibraryGeospartialDistanceScript() {
//        Map<String, String> binding = new HashMap<String, String>();
//        binding.put("Copenhagen", "location1");
//        binding.put("Frankfurt", "location2");
//        ScriptExecutionRequest request = new ScriptExecutionRequest("GeospartialDistance", "", "javascript", new Parameter("ScriptEngine", "DistanceFromAtoB",
//                "The distance from A to B", 0d, "Meter"), binding);
//
//        ScriptExecutor executor = new ScriptExecutor(request);
//
//        GroundStation gs1 = new GroundStation("Copenhagen", "Copenhagen", new D3Vector("", "", "", "", 49.982314d, 8.811035d, 0d));
//        GroundStation gs2 = new GroundStation("Frankfurt", "Frankfurt", new D3Vector("", "", "", "", 55.61683d, 12.601318d, 0d));
//
//        Parameter out = (Parameter) executor.calculate(gs1);
//        out = (Parameter) executor.calculate(gs2);
//
//        assertNotNull(out);
//        assertTrue(out instanceof Parameter);
//        assertEquals("DistanceFromAtoB", out.getName());
//        assertEquals(626529.5932975922D, out.asDouble(), 0.0D);
//    }
//
//    @Test
//    public void testLibraryBatteryRechargeTimeScript() {
//        Map<String, String> binding = new HashMap<String, String>();
//        binding.put("Parameter500", "capacity");
//        binding.put("Parameter501", "lostCharge");
//        binding.put("Parameter502", "chargerOutput");
//        ScriptExecutionRequest request = new ScriptExecutionRequest("BatteryRechargeTime", "", "javascript", new Parameter("ScriptEngine", "Recharge Time",
//                "The time it will take to recharge the battery.", 0d, "Seconds"), binding);
//
//        ScriptExecutor executor = new ScriptExecutor(request);
//
//        Parameter out = (Parameter) executor.calculate(new Parameter("", "Parameter500", "", 1000, "mAh"));
//        out = (Parameter) executor.calculate(new Parameter("", "Parameter501", "", 20, "Percentage"));
//        out = (Parameter) executor.calculate(new Parameter("", "Parameter502", "", 50, "mAh"));
//
//        assertNotNull(out);
//        assertTrue(out instanceof Parameter);
//        assertEquals("Recharge Time", out.getName());
//        assertEquals(86400.0D, out.asDouble(), 0.0D);
//    }
}
