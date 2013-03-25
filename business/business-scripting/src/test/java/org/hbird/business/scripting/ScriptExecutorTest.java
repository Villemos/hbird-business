package org.hbird.business.scripting;

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

    @Test
    public void testDoubleParameter() {
        String script = "var value=in1.asDouble()*10 + in2.asDouble(); output.setValue(value);\n";

        Map<String, String> binding = new HashMap<String, String>();
        binding.put("Parameter1", "in1");
        binding.put("Parameter2", "in2");
        ScriptExecutionRequest request = new ScriptExecutionRequest("", script, "javascript", new Parameter("ScriptEngine", "Parameter10",
                "A test script parameter.", new Double(0), "Volt"), binding);

        ScriptExecutor executor = new ScriptExecutor(request);

        Object out = executor.calculate(new Parameter("test", "Parameter1", "A parameter", 2d, "Volt"));
        assertTrue(out == null);

        out = executor.calculate(new Parameter("test", "Parameter1", "A parameter", 3d, "Volt"));
        assertTrue(out == null);

        out = executor.calculate(new Parameter("test", "Parameter2", "A parameter", 5d, "Volt"));
        assertTrue(out != null);
        assertTrue(out instanceof Parameter);
        assertTrue(((Parameter) out).getName().equals("Parameter10"));
        assertTrue(((Parameter) out).getDescription().equals("A test script parameter."));
        assertTrue(((Parameter) out).getUnit().equals("Volt"));
        assertTrue((Double) ((Parameter) out).getValue() == 35d);
    }

    @Test
    public void testStateParameter() {

        String script = "if (in1.asInt() == 4) {output.setValid()} else {output.setInvalid()}\n";

        Map<String, String> binding = new HashMap<String, String>();
        binding.put("Parameter1", "in1");
        ScriptExecutionRequest request = new ScriptExecutionRequest("", script, "javascript", new State("ScriptEngine", "TestState", "", "Parameter100", true),
                binding);

        ScriptExecutor executor = new ScriptExecutor(request);

        State out = (State) executor.calculate(new Parameter("test", "Parameter1", "A parameter", 5d, "Volt"));
        assertTrue(out != null);
        assertTrue(out instanceof State);
        assertTrue(out.getIsStateOf().equals("Parameter100"));
        assertTrue(out.getValue() == false);

        out = (State) executor.calculate(new Parameter("test", "Parameter1", "A parameter", 4d, "Volt"));
        assertTrue(out != null);
        assertTrue(out instanceof State);
        assertTrue(out.getIsStateOf().equals("Parameter100"));
        assertTrue(out.getValue() == true);
    }

    @Test
    public void testLibraryScript() {
        Map<String, String> binding = new HashMap<String, String>();
        binding.put("Parameter201", "in1");
        ScriptExecutionRequest request = new ScriptExecutionRequest("Fahrenheit2CelsiusConvertion", "", "javascript", new Parameter("ScriptEngine",
                "Parameter200", "Temperature in CELCIUS.", new Double(0), "Celsius"), binding);

        ScriptExecutor executor = new ScriptExecutor(request);

        Parameter out = (Parameter) executor.calculate(new Parameter("test", "Parameter201", "The temperature in FAHRENHEIT.", 200d,
                "Fahrenheit"));
        assertTrue(out != null);
        assertTrue(out instanceof Parameter);
        assertTrue(out.getName().equals("Parameter200"));
        assertTrue(out.getUnit().equals("Celsius"));
        assertTrue((Double) out.getValue() == 93.33333333333333);

    }

    @Test
    public void testLibraryLabelScript() {
        Map<String, String> binding = new HashMap<String, String>();
        binding.put("Parameter300", "in1");
        binding.put("Parameter301", "threshold");
        ScriptExecutionRequest request = new ScriptExecutionRequest("OnOffSpline", "", "javascript", new Label("ScriptEngine", "Parameter300",
                "Whether the battery is ON or OFF", "ON"), binding);

        ScriptExecutor executor = new ScriptExecutor(request);

        Label out = (Label) executor.calculate(new Parameter("test", "Parameter301", "The ON / OFF threshold.", 300d, "Volt"));

        out = (Label) executor.calculate(new Parameter("test", "Parameter300", "Dont know.", 200d, "Volt"));

        assertTrue(out != null);
        assertTrue(out instanceof Label);
        assertTrue(out.getName().equals("Parameter300"));
        assertTrue(out.getValue().equals("OFF"));

        out = (Label) executor.calculate(new Parameter("test", "Parameter300", "Dont know.", 300d, "Volt"));

        assertTrue(out != null);
        assertTrue(out instanceof Label);
        assertTrue(out.getName().equals("Parameter300"));
        assertTrue(out.getValue().equals("ON"));

        out = (Label) executor.calculate(new Parameter("test", "Parameter300", "Dont know.", 400d, "Volt"));

        assertTrue(out != null);
        assertTrue(out instanceof Label);
        assertTrue(out.getName().equals("Parameter300"));
        assertTrue(out.getValue().equals("ON"));
    }

    @Test
    public void testLibraryGeospartialDistanceScript() {
        // FIXME - 27.02.2013, kimmell - use GroundStation instead of Location
        Map<String, String> binding = new HashMap<String, String>();
        binding.put("Copenhagen", "location1");
        binding.put("Frankfurt", "location2");
        ScriptExecutionRequest request = new ScriptExecutionRequest("GeospartialDistance", "", "javascript", new Parameter("ScriptEngine", "DistanceFromAtoB",
                "The distance from A to B", 0d, "Meter"), binding);

        ScriptExecutor executor = new ScriptExecutor(request);

        Parameter out = (Parameter) executor.calculate(new GroundStation("Copenhagen", new D3Vector("", "", "", "", 49.982314d, 8.811035d, 0d)));
        out = (Parameter) executor.calculate(new GroundStation("Frankfurt", new D3Vector("", "", "", "", 55.61683d, 12.601318d, 0d)));

        assertTrue(out != null);
        assertTrue(out instanceof Parameter);
        assertTrue(out.getName().equals("DistanceFromAtoB"));
        assertTrue(out.asDouble() == 626529.5932975922);
    }

    @Test
    public void testLibraryBatteryRechargeTimeScript() {
        Map<String, String> binding = new HashMap<String, String>();
        binding.put("Parameter500", "capacity");
        binding.put("Parameter501", "lostCharge");
        binding.put("Parameter502", "chargerOutput");
        ScriptExecutionRequest request = new ScriptExecutionRequest("BatteryRechargeTime", "", "javascript", new Parameter("ScriptEngine", "Recharge Time",
                "The time it will take to recharge the battery.", 0d, "Seconds"), binding);

        ScriptExecutor executor = new ScriptExecutor(request);

        Parameter out = (Parameter) executor.calculate(new Parameter("", "Parameter500", "", 1000, "mAh"));
        out = (Parameter) executor.calculate(new Parameter("", "Parameter501", "", 20, "Percentage"));
        out = (Parameter) executor.calculate(new Parameter("", "Parameter502", "", 50, "mAh"));

        assertTrue(out != null);
        assertTrue(out instanceof Parameter);
        assertTrue(out.getName().equals("Recharge Time"));
        assertTrue(out.asDouble() == 86400.0);
    }
}
