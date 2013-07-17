package org.hbird.business.api.impl;

import java.util.ArrayList;
import java.util.List;

import org.hbird.business.api.dao.DataAccess;
import org.hbird.exchange.core.EntityInstance;

class InMemoryStore implements DataAccess {
	private List<EntityInstance> entities;
	
	public InMemoryStore() {
		entities = new ArrayList<EntityInstance>();
	}

	@Override
	public <T> T getById(String id, Class<T> clazz) throws Exception {
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
	public <T> T getByInstanceId(String id, Class<T> clazz) throws Exception {
		for(EntityInstance entity : entities) {
			if(clazz.isInstance(entity) && entity.getInstanceID().equals(id)) {
				return (T)entity;
			}
		}
		
		throw new Exception("Object with instance ID " + id + " and class " + clazz.getName() + " not found");
	}

	@Override
	public <T> List<T> getAll(Class<T> clazz) throws Exception {
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
	
}