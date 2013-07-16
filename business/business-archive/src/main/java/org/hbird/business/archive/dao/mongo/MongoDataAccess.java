package org.hbird.business.archive.dao.mongo;

import java.net.UnknownHostException;
import java.util.List;

import org.hbird.business.archive.dao.DataAccess;
import org.hbird.exchange.interfaces.IEntityInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import com.mongodb.Mongo;

public class MongoDataAccess implements DataAccess {
	public static final String DEFAULT_DATABASE_NAME = "hbird";
	
	private static Logger LOG = LoggerFactory.getLogger(MongoDataAccess.class);
	
	private Sort sortByVersion = new Sort(new Sort.Order(Sort.Direction.DESC, "version"));
	
	private MongoOperations template = null;

	public MongoDataAccess(MongoOperations template) {
		this.template = template;
	}
	
	// TODO: This is just for testing - instantiate in spring context if possible
	public MongoDataAccess() throws UnknownHostException {
		Mongo mongo = new Mongo("localhost", 27017);
		template = new MongoTemplate(mongo, DEFAULT_DATABASE_NAME);
	}
	
	@Override
	public <T> T getById(String id, Class<T> clazz) throws Exception {
		Query query = new Query(Criteria.where("ID").is(id));
		
		// Need to return latest, but IEntity has no version, only IEntityInstance
		if(IEntityInstance.class.isAssignableFrom(clazz)) { // XXX: Don't really like it - can smth be done without reflection?
			query = query.with(sortByVersion);
		}
		
		return template.findOne(query, clazz);
	}

	@Override
	public <T> T getByInstanceId(String id, Class<T> clazz) throws Exception {
		return template.findById(id, clazz);
	}

	@Override
	public <T> List<T> getAll(Class<T> clazz) throws Exception {
		return template.findAll(clazz);
	}

	@Override
	public Object save(Object o) throws Exception {
		template.save(o);
		//template.insert(o);
		return o;
	}

}
