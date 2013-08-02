package org.hbird.business.api.impl;

import java.util.ArrayList;
import java.util.List;

import org.hbird.business.api.IDataAccess;
import org.hbird.exchange.core.EntityInstance;
import org.hbird.exchange.core.Metadata;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.State;
import org.hbird.exchange.interfaces.IEntityInstance;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.exchange.navigation.OrbitalState;
import org.hbird.exchange.navigation.TleOrbitalParameters;

class InMemoryStore implements IDataAccess {
	private List<EntityInstance> entities;
	
	public InMemoryStore() {
		entities = new ArrayList<EntityInstance>();
	}

	@Override
	public <T extends IEntityInstance> T getById(String id, Class<T> clazz) throws Exception {
		T current = null;
		long current_version = -1;
		
		for(EntityInstance entity : entities) {
			if(clazz.isInstance(entity) && entity.getID().equals(id)) {
				if(current == null || current_version < entity.getVersion()) {
					current = (T) entity;
					current_version = entity.getVersion();
				}
			}
		}
		
		if(current == null) {
			throw new Exception("Object with ID " + id + " and class " + clazz.getName() + " not found");
		} else {
			return current;
		}
	}

	@Override
	public <T extends IEntityInstance> T getByInstanceId(String id, Class<T> clazz) throws Exception {
		for(EntityInstance entity : entities) {
			if(clazz.isInstance(entity) && entity.getInstanceID().equals(id)) {
				return (T)entity;
			}
		}
		
		throw new Exception("Object with instance ID " + id + " and class " + clazz.getName() + " not found");
	}

	@Override
	public <T extends IEntityInstance> List<T> getAll(Class<T> clazz) throws Exception {
		List<T> objects = new ArrayList<T>();
		
		for(EntityInstance entity : entities) {
			if(clazz.isInstance(entity)) {
				objects.add((T)entity);
			}
		}
		
		return objects;
	}

	@Override
	public Object save(Object o) throws Exception {
		if(EntityInstance.class.isInstance(o)) {
			entities.add((EntityInstance) o);
			
			return o;
		} else {
			throw new Exception("Can't save instances of class " + o.getClass().getName() + "; must be a subclass of EntityInstance");
		}
	}
	
	public void dump() {
		System.out.println("Store dump: ");
		for(EntityInstance entity : entities) {
			System.out.println("Entity: id = " + entity.getID() + " name = " + 
					entity.getName() + " version = " + entity.getVersion());
		}
	}

	@Override
	public Parameter getParameter(String name) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Parameter> getParameter(String name, long from, long to)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<State> getState(String applicableTo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<State> getState(String applicableTo, long from, long to)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<State> getStates(List<String> names) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrbitalState getOrbitalStateFor(String satelliteID) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OrbitalState> getOrbitalStatesFor(String satelliteID,
			long from, long to) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TleOrbitalParameters getTleFor(String satelliteID) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TleOrbitalParameters> getTleFor(String satelliteID, long from,
			long to) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LocationContactEvent getNextLocationContactEventForGroundStation(
			String groundStationID) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LocationContactEvent getNextLocationContactEventForGroundStation(
			String groundStationID, long from) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LocationContactEvent getNextLocationContactEventFor(
			String groundStationID, String satelliteID) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LocationContactEvent getNextLocationContactEventFor(
			String groundStationID, String satelliteID, long from)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Metadata> getMetadata(String id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}