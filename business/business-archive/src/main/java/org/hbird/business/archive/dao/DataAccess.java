package org.hbird.business.archive.dao;

import java.util.List;

public interface DataAccess {
	// TODO: Qualify with <T extends IEntity> ?
	public <T> T getById(String id, Class<T> clazz) throws Exception; // TODO: Specify exception types
	
	// TODO: <T extends IEntityInstance>?
	public <T> T getByInstanceId(String id, Class<T> clazz) throws Exception;
	
	public <T> List<T> getAll(Class<T> clazz) throws Exception;
	
	// XXX: if two Entities (having no timestamp) are saved, should they
	// both be in DB? Same with EntityInstances having the same instance id
	public Object save(Object o) throws Exception;
}
