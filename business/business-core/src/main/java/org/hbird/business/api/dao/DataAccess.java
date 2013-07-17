package org.hbird.business.api.dao;

import java.util.List;

public interface DataAccess {
	// TODO: Qualify with <T extends IEntity> ?
	public <T> T getById(String id, Class<T> clazz) throws Exception; // TODO: Specify exception types
	
	// TODO: <T extends IEntityInstance>?
	public <T> T getByInstanceId(String id, Class<T> clazz) throws Exception;
	
	public <T> List<T> getAll(Class<T> clazz) throws Exception;
	
	public Object save(Object o) throws Exception;
}
