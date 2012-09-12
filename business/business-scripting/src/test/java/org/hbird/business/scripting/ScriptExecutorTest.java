package org.hbird.business.scripting;

import static org.junit.Assert.*;

import org.hbird.business.scripting.ScriptExecutor;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.StateParameter;
import org.hbird.exchange.scripting.ScriptExecutionRequest;
import org.junit.Test;

public class ScriptExecutorTest {

	
	
	@Test
	public void testDoubleParameter() {	
		String script = "// This is a comment\n" +
						"// name=\"Parameter10\"\n" +
						"// description=\"A test script parameter\"\n" +
						"// unit=\"Volt\"\n" +
						"// input=\"Parameter1\"\n" +
						"// input=\"Parameter2\"\n" +
						"var Parameter10=Parameter1*10 + Parameter2;\n";
		
		ScriptExecutionRequest request = new ScriptExecutionRequest("unittest", "testScript", "A test script", script);
		
		ScriptExecutor executor = new ScriptExecutor(request);
		
		assertTrue(executor.parameterName.equals("Parameter10"));
		assertTrue(executor.parameterDescription.equals("A test script parameter"));
		assertTrue(executor.parameterUnit.equals("Volt"));
		
		assertTrue(executor.input.size() == 2);
		
		Parameter out = executor.calculate(new Parameter("test", "Parameter1", "A parameter", 2d, "Volt"));
		assertTrue(out == null);
		
		out = executor.calculate(new Parameter("test", "Parameter1", "A parameter", 3d, "Volt"));
		assertTrue(out == null);

		out = executor.calculate(new Parameter("test", "Parameter2", "A parameter", 5d, "Volt"));
		assertTrue(out != null);
		assertTrue(out instanceof Parameter);
		assertTrue(out.getName().equals("Parameter10"));
		assertTrue(out.getDescription().equals("A test script parameter"));
		assertTrue(out.getUnit().equals("Volt"));
		assertTrue((Double) out.getValue() == 35d);
	}
	
	@Test
	public void testStateParameter() {	
	
		String script = 	"// This is a comment\n" +
									"// name=\"Parameter10\"\n" +
									"// description=\"A test script parameter\"\n" +
									"// unit=\"Volt\"\n" +
									"// isStateOf=\"Parameter100\"\n" +
									"// input=\"Parameter1\"\n" +
									"if (Parameter1 == 4) {Parameter10=true} else {Parameter10=false}\n";

		ScriptExecutionRequest request = new ScriptExecutionRequest("unittest", "testScript", "A test script", script);

		ScriptExecutor executor = new ScriptExecutor(request);

		Parameter out = executor.calculate(new Parameter("test", "Parameter1", "A parameter", 5d, "Volt"));
		assertTrue(out != null);
		assertTrue(out instanceof StateParameter);
		assertTrue(((StateParameter) out).getIsStateOf().equals("Parameter100"));
		assertTrue(((StateParameter) out).getStateValue() == false);
		
		out = executor.calculate(new Parameter("test", "Parameter1", "A parameter", 4d, "Volt"));
		assertTrue(out != null);
		assertTrue(out instanceof StateParameter);
		assertTrue(((StateParameter) out).getIsStateOf().equals("Parameter100"));
		assertTrue(((StateParameter) out).getStateValue() == true);		
	}
	
	
	@Test
	public void testStringParameter() {	
	
		String script = 	"// This is a comment\n" +
									"// name=\"Parameter10\"\n" +
									"// description=\"A test script parameter\"\n" +
									"// unit=\"Volt\"\n" +
									"// input=\"Parameter1\"\n" +
									"if (Parameter1 == 4) {Parameter10='Parameter 1 is 4'} else {Parameter10='Parameter 1 is not 4'}\n";

		ScriptExecutionRequest request = new ScriptExecutionRequest("unittest", "testScript", "A test script", script);

		ScriptExecutor executor = new ScriptExecutor(request);

		Parameter out = executor.calculate(new Parameter("test", "Parameter1", "A parameter", 5d, "Volt"));
		assertTrue(out != null);
		assertTrue(out instanceof Parameter);
		assertTrue(((String)out.getValue()).equals("Parameter 1 is not 4") == true);
		
		out = executor.calculate(new Parameter("test", "Parameter1", "A parameter", 4d, "Volt"));
		assertTrue(out != null);
		assertTrue(out instanceof Parameter);
		assertTrue(((String)out.getValue()).equals("Parameter 1 is 4") == true);
	}
}
