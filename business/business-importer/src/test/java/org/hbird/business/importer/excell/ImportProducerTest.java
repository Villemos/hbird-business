package org.hbird.business.importer.excell;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.read.biff.BiffException;

import org.hbird.business.importer.excell.ImportAccessor;
import org.hbird.business.importer.excell.ImportEndpoint;
import org.hbird.exchange.commandrelease.CommandRequest;
import org.hbird.exchange.core.Named;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.State;
import org.hbird.exchange.tasking.SetParameter;
import org.hbird.exchange.tasking.Task;
import org.junit.Test;

public class ImportProducerTest {

	@Test
	public void testProcess() {
	
		ImportEndpoint endpoint = new ImportEndpoint();
		endpoint.filename = "src/test/resources/CommandSequence1.xls";		
		
		ImportAccessor accessor = new ImportAccessor(endpoint);
		
		try {
			List<Object> objects = accessor.getObjects();

			Map<String, Object> readObjects = new HashMap<String, Object>();
			
			/** Unpack the data */
			for (Object obj : objects) {
				System.out.println("Putting name " + ((Named) obj).getName());
				readObjects.put(((Named) obj).getName(), obj);
			}
			
			assertTrue(readObjects.size() == 8);
			
			/** Check the parameters. */
			Parameter para = null;
			State statePara = null;
			
			assertTrue(readObjects.get("Parameter200") != null);
			para = (Parameter) readObjects.get("Parameter200");
			assertTrue(para.getDescription().equals("A test para"));
			assertTrue(para.getValue() instanceof Integer);
			assertTrue( ((Integer)para.getValue()) == 1);
			assertTrue(para.getUnit().equals("Volt"));
			
			assertTrue(readObjects.get("Parameter201") != null);
			para = (Parameter) readObjects.get("Parameter201");
			assertTrue(para.getDescription().equals("A test para"));
			assertTrue(para.getValue() instanceof Double);
			assertTrue( ((Double)para.getValue()) == 2.0);
			assertTrue(para.getUnit().equals("Meter"));
			
			assertTrue(readObjects.get("Parameter202") != null);
			statePara = (State) readObjects.get("Parameter202");
			assertTrue(statePara.getDescription().equals("A test para"));
			assertTrue(statePara.getValue() instanceof Boolean);
			assertTrue( ((Boolean)statePara.getValue()) == false);
			assertTrue(statePara.getIsStateOf().equals("Parameter1"));

			assertTrue(readObjects.get("Parameter203") != null);
			para = (Parameter) readObjects.get("Parameter203");
			assertTrue(para.getDescription().equals("A test para"));
			assertTrue(para.getValue() instanceof Float);
			assertTrue( ((Float)para.getValue()) == 2.0);
			assertTrue(para.getUnit().equals("Kilo"));
			
			SetParameter task = null;
			assertTrue(readObjects.get("Task1") != null);
			task = (SetParameter) readObjects.get("Task1");
			assertTrue(task.getDescription().equals("A test task"));
			assertTrue(task.getExecutionDelay() == 3000);
			assertTrue(task.getParameter().getName().equals("Parameter201"));
			assertTrue(((Double)task.getParameter().getValue()) == 5);

			assertTrue(readObjects.get("Task2") != null);
			task = (SetParameter) readObjects.get("Task2");
			assertTrue(task.getDescription().equals("A test task"));
			assertTrue(task.getExecutionDelay() == 0);
			assertTrue(task.getParameter().getName().equals("Parameter203"));
			assertTrue(((Float)task.getParameter().getValue()) == 3);

			
			
			CommandRequest request = null;
			assertTrue(readObjects.get("CommandContainerCommand1") != null);
			request = (CommandRequest) readObjects.get("CommandContainerCommand1");
			
			assertTrue(request.getArguments().size() == 2);
//			assertTrue(((Parameter) request.getArguments().get(0)).getName().equals("Parameter200"));
//			assertTrue(((Parameter) request.getArguments().get(0)).asInt() == 7);
//			assertTrue(((Parameter) request.getArguments().get(1)).getName().equals("Parameter201"));
//			assertTrue(((Parameter) request.getArguments().get(1)).asDouble() == 8);
			
			assertTrue(request.getLockStates().size() == 1);
			assertTrue(request.getLockStates().get(0).equals("Parameter202"));
			
			assertTrue(request.getTasks().size() == 1);
			assertTrue(request.getTasks().get(0).getName().equals("Task2"));			
			assertTrue(((SetParameter)request.getTasks().get(0)).getParameter().getName().equals("Parameter203"));
			assertTrue(((Float) ((SetParameter)request.getTasks().get(0)).getParameter().getValue()) == 4);
		
			assertTrue(request.getCommand().getName().equals("Command1"));
			assertTrue(request.getCommand().getDescription().equals("A test command"));
			assertTrue(request.getCommand().getReleaseTime() == 0);
			assertTrue(request.getCommand().getExecutionTime() == 312456789);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
