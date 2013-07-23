package org.hbird.business.api.impl;

import java.util.ArrayList;
import java.util.List;

import org.hbird.business.api.IPublisher;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.CommandArgument;
import org.hbird.exchange.core.Label;
import org.junit.Test;

import static org.junit.Assert.*;

public class PublisherTest {
	private final static String COMPONENT_NAME = "Publish";
	
	private InMemoryStore dao;
	private IPublisher publish;
	
	public PublisherTest() {
		dao = new InMemoryStore();
		publish = new Publisher(COMPONENT_NAME, COMPONENT_NAME, dao);
	}

	@Test
	public void testPublishLabel() throws Exception {
		String id = "foo";
		String name = "bar";
		String description = "baz";
		String value = "foobarbaz";
		
		Label lbl = publish.publishLabel(id, name, description, value);
		
		assertEquals(id, lbl.getID());
		assertEquals(name, lbl.getName());
		assertEquals(description, lbl.getDescription());
		assertEquals(value, lbl.getValue());
		assertEquals(COMPONENT_NAME, lbl.getIssuedBy());
		
		lbl = dao.getById(id, Label.class);
		
		assertEquals(id, lbl.getID());
		assertEquals(name, lbl.getName());
		assertEquals(description, lbl.getDescription());
		assertEquals(value, lbl.getValue());
		assertEquals(COMPONENT_NAME, lbl.getIssuedBy());
	}
	
	@Test
	public void testPublishCommand() throws Exception {
		String id = "foo";
		String name = "bar";
		String description = "baz";
		String argname = "arg1";
		int argvalue = 5;
		
		List<CommandArgument> args = new ArrayList<CommandArgument>();
		
		CommandArgument arg1 = new CommandArgument(argname, argname, Integer.class, false);
		arg1.setIntValue(argvalue);
		
		args.add(arg1);
		
		Command comm = publish.publishCommand(id, name, description, args);
		
		assertEquals(id, comm.getID());
		assertEquals(name, comm.getName());
		assertEquals(description, comm.getDescription());
		assertEquals(COMPONENT_NAME, comm.getIssuedBy());
		assertEquals(COMPONENT_NAME, comm.getDestination());
		
		assertEquals(1, comm.getArgumentList().size());
		
		CommandArgument arg = comm.getArgumentList().get(0);
		
		assertEquals(argname, arg.getName());
		assertEquals(argname, arg.getDescription());
		assertEquals(Integer.class, arg.getType());
		assertEquals(false, arg.getMandatory());
		assertEquals((Integer) argvalue, (Integer) arg.getValue());
	}
}
