package org.hbird.business.api.impl;

import java.util.Collections;
import java.util.List;

import org.hbird.business.api.IDataAccess;
import org.hbird.business.api.IPublisher;
import org.hbird.exchange.commandrelease.CommandRequest;
import org.hbird.exchange.core.Binary;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.CommandArgument;
import org.hbird.exchange.core.EntityInstance;
import org.hbird.exchange.core.Label;
import org.hbird.exchange.core.Metadata;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.State;
import org.hbird.exchange.navigation.TleOrbitalParameters;
import org.hbird.exchange.tasking.Task;

public class Publisher extends AbstractHbirdApi implements IPublisher{
	private IDataAccess dao;
	
	public Publisher(String issuedBy, String destination, IDataAccess dao) {
		super(issuedBy, destination);
		
		this.dao = dao;
	}

	@Override
	public void commit(String ID) throws Exception {
		// TODO: Add method to DataAccess to flush the data if possible?
	}

	@Override
	public EntityInstance publish(EntityInstance object) throws Exception {
		if(object.getIssuedBy() == null) {
			object.setIssuedBy(getIssuedBy());
		}
		
		if(object instanceof Command) {
			Command comm = (Command) object;
			if(comm.getDestination() == null) {
				comm.setDestination(getDestination());
			}
		}
		
		dao.save(object);
		
		return object;
	}

	@Override
	public Parameter publishParameter(String ID, String name,
			String description, Number value, String unit) throws Exception {
		Parameter param = new Parameter(ID, name);
		param.setDescription(description);
		param.setValue(value);
		param.setUnit(unit);
		
		return (Parameter) publish(param);
	}

	@Override
	public Parameter publishParameter(String ID, String name,
			String description, Number value, String unit, long timestamp)
			throws Exception {
		Parameter param = new Parameter(ID, name);
		param.setDescription(description);
		param.setValue(value);
		param.setUnit(unit);
		param.setTimestamp(timestamp); // TODO: Maybe set version here, not just timestamp?
		
		return (Parameter) publish(param);
		
	}

	@Override
	public State publishState(String ID, String name, String description,
			String applicableTo, Boolean state) throws Exception {
		State st = new State(ID, name);
		st.setDescription(description);
		st.setApplicableTo(applicableTo);
		st.setValue(state);
		
		return (State) publish(st);
	}

	@Override
	public State publishState(String ID, String name, String description,
			String applicableTo, Boolean state, long timestamp)
			throws Exception {
		State st = new State(ID, name);
		st.setDescription(description);
		st.setApplicableTo(applicableTo);
		st.setValue(state);
		st.setTimestamp(timestamp);
		
		return (State) publish(st);
	}

	@Override
	public Label publishLabel(String ID, String name, String description,
			String value) throws Exception {
		Label label = new Label(ID, name);
		label.setDescription(description);
		label.setValue(value);
		
		return (Label) publish(label);
	}

	@Override
	public Binary publishBinary(String ID, String name, String description,
			byte[] rawdata) throws Exception {
		Binary binary = new Binary(ID, name);
		binary.setDescription(description);
		binary.setRawData(rawdata); // TODO: Copy?
		
		return (Binary) publish(binary);
	}

	@Override
	public Command publishCommand(String ID, String name, String description,
			List<CommandArgument> arguments) throws Exception {
		Command command = new Command(ID, name);
		command.setDescription(description);
		command.setArgumentList(arguments);
		
		return (Command) publish(command);
	}

	@Override
	public CommandRequest publishCommandRequest(String ID, String name,
			String description, Command command) throws Exception {
		CommandRequest request = new CommandRequest(ID, name);
		request.setName(name);
		request.setDescription(description);
		request.setCommand(command);
		
		return (CommandRequest) publish(request);
	}

	@Override
	public CommandRequest publishCommandRequest(String ID, String name,
			String description, Command command, List<String> lockStates,
			List<Task> tasks) throws Exception {
		CommandRequest request = new CommandRequest(ID, name);
		request.setName(name);
		request.setDescription(description);
		request.setCommand(command);
		request.setLockStates(lockStates);
		request.setTasks(tasks);
		
		return (CommandRequest) publish(request);
	}

	@Override
	public Metadata publishMetadata(String ID, String name,
			EntityInstance subject, String key, String metadata)
			throws Exception {
		Metadata md = new Metadata(ID, name);
		md.setMetadata(Collections.singletonMap(key, (Object)metadata));
		md.setApplicableTo(subject.getID());
		
		return (Metadata) publish(md);
	}

	@Override
	public TleOrbitalParameters publishTleParameters(String ID, String name,
			String satellite, String tle1, String tle2) throws Exception {
		TleOrbitalParameters tle = new TleOrbitalParameters(ID, name);
		
		tle.setSatelliteId(satellite);
		tle.setTleLine1(tle1);
		tle.setTleLine2(tle2);
		
		return (TleOrbitalParameters) publish(tle);
	}

}
