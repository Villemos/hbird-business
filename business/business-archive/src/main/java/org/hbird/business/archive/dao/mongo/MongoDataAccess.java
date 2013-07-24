package org.hbird.business.archive.dao.mongo;

import java.net.UnknownHostException;
import java.util.List;
import java.util.ListIterator;

import org.hbird.business.api.IDataAccess;
import org.hbird.exchange.core.EntityInstance;
import org.hbird.exchange.core.Metadata;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.State;
import org.hbird.exchange.interfaces.IEntityInstance;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.exchange.navigation.OrbitalState;
import org.hbird.exchange.navigation.TleOrbitalParameters;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import com.mongodb.Mongo;

import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Criteria.where;

public class MongoDataAccess implements IDataAccess {
	public static final String DEFAULT_DATABASE_NAME = "hbird";
	
	private static final String VERSION_FIELD = "version";
	private static final String TIMESTAMP_FIELD = "timestamp";
	
	private static final String FIELD_NAME = "name";
	private static final String FIELD_APPLICABLE_TO = "applicableTo";
	private static final String FIELD_SATELLITE_ID = "satelliteId";
	private static final String FIELD_GROUND_STATION_ID = "groundStationId";
	private static final String FIELD_START_TIME = "startTime";
	private static final String FIELD_DERIVED_FROM = "derivedFromId";

	
	public final Sort sortByVersionDesc = new Sort(new Sort.Order(Sort.Direction.DESC, VERSION_FIELD));
	
	public final Sort sortByTimestampAsc = new Sort(new Sort.Order(Sort.Direction.ASC, TIMESTAMP_FIELD));
	public final Sort sortByTimestampDesc = new Sort(new Sort.Order(Sort.Direction.DESC, TIMESTAMP_FIELD));
	
	public final Sort sortByStartTimeAsc = new Sort(new Sort.Order(Sort.Direction.ASC, FIELD_START_TIME));
	public final Sort sortByStartTimeDesc = new Sort(new Sort.Order(Sort.Direction.DESC, FIELD_START_TIME));
	
	protected MongoOperations template = null;

	public MongoDataAccess(MongoOperations template) {
		this.template = template;
	}
	
	// TODO: This is just for testing - instantiate in spring context if possible
	public MongoDataAccess() throws UnknownHostException {
		Mongo mongo = new Mongo("localhost", 27017);
		MongoTemplate temp = new MongoTemplate(mongo, DEFAULT_DATABASE_NAME);
		//temp.setWriteConcern(WriteConcern.ACKNOWLEDGED);
		
		template = temp;
	}
	
	private <T> T wrapReturn(T value) throws Exception {
		if(value == null) {
			throw new Exception("Object not found");
		}
		
		return value;
	}
	
	@Override
	public <T extends IEntityInstance> T getById(String id, Class<T> clazz) throws Exception {
		Query query = new Query(Criteria.where("ID").is(id)).with(sortByVersionDesc);
		
		return wrapReturn(template.findOne(query, clazz));
	}

	@Override
	public <T extends IEntityInstance> T getByInstanceId(String id, Class<T> clazz) throws Exception {
		return wrapReturn(template.findById(id, clazz));
	}

	@Override
	public <T extends IEntityInstance> List<T> getAll(Class<T> clazz) throws Exception {
		return template.findAll(clazz);
	}

	@Override
	public Object save(Object o) throws Exception {
		template.save(o);
		return o;
	}
	
	private Query queryByFieldInRange(String field, String value, long from, long to) {
		return new Query(where(field).is(value).andOperator(
						where(TIMESTAMP_FIELD).gte(from),
						where(TIMESTAMP_FIELD).lte(to)))
						.with(sortByTimestampAsc);
	}

	private <T extends IEntityInstance> List<T> getByFieldInRange(String field, String value,
			Class<T> clazz, long from, long to) throws Exception {
		Query query = queryByFieldInRange(field, value, from, to);
		
		return template.find(query, clazz);
	}
	
	// Retun the entity with a biggest version satisfying the filter
	private <T extends IEntityInstance> T getLastVersionByField(Class<T> clazz, String field, String value) {
		Query query = new Query(Criteria.where(field).is(value)).with(sortByVersionDesc).limit(1);
		return template.findOne(query, clazz);
	}

	@Override
	public Parameter getParameter(String name) throws Exception {
		return wrapReturn(getLastVersionByField(Parameter.class, FIELD_NAME, name));
	}

	@Override
	public List<Parameter> getParameter(String name, long from, long to) throws Exception {
		return getByFieldInRange(FIELD_NAME, name, Parameter.class, from, to);
	}

	@Override
	public List<State> getState(String applicableTo) { // TODO: Use version instead of timestamp?
		return template.find(query(where(FIELD_APPLICABLE_TO).is(applicableTo)).with(sortByTimestampDesc), State.class);
	}

	@Override
	public List<State> getState(String applicableTo, long from, long to) throws Exception {
		return getByFieldInRange(FIELD_APPLICABLE_TO, applicableTo, State.class, from, to);
	}

	@Override
	public List<State> getStates(List<String> names) {
		return template.find(query(where(FIELD_NAME).in(names)), State.class);
	}

	@Override
	public OrbitalState getOrbitalStateFor(String satelliteID) throws Exception {
		List<TleOrbitalParameters> tles = getTleFor(satelliteID, Long.MIN_VALUE, Long.MAX_VALUE);
		
		ListIterator<TleOrbitalParameters> iter = tles.listIterator(tles.size());
		
		while(iter.hasPrevious()) {
			TleOrbitalParameters tle = iter.previous();
			OrbitalState state = template.findOne(query(where(FIELD_SATELLITE_ID).is(satelliteID)
													.and(FIELD_DERIVED_FROM).is(tle.getInstanceID()))
													.with(sortByTimestampDesc), OrbitalState.class);
			
			if(state != null) {
				return state;
			}
		}
		
		throw new Exception("Could not find any orbital state for satellite " + satelliteID);
	}

	@Override
	public List<OrbitalState> getOrbitalStatesFor(String satelliteID, long from, long to) throws Exception {
		return getByFieldInRange(FIELD_SATELLITE_ID, satelliteID, OrbitalState.class, from, to);
	}

	@Override
	public TleOrbitalParameters getTleFor(String satelliteID) throws Exception {
		return wrapReturn(getLastVersionByField(TleOrbitalParameters.class, FIELD_SATELLITE_ID, satelliteID));
	}

	@Override
	public List<TleOrbitalParameters> getTleFor(String satelliteID, long from, long to) throws Exception {
		return getByFieldInRange(FIELD_SATELLITE_ID, satelliteID, TleOrbitalParameters.class, from, to);
	}

	@Override
	public LocationContactEvent getNextLocationContactEventForGroundStation(String groundStationID) throws Exception {
		return getNextLocationContactEventForGroundStation(groundStationID, System.currentTimeMillis());
	}

	@Override
	public LocationContactEvent getNextLocationContactEventForGroundStation(String groundStationID, long from) throws Exception {
		Query query = query(where(FIELD_GROUND_STATION_ID).is(groundStationID).and(FIELD_START_TIME).gte(from))
				.with(sortByStartTimeAsc).limit(1);

		return wrapReturn(template.findOne(query, LocationContactEvent.class));
	}

	@Override
	public LocationContactEvent getNextLocationContactEventFor(String groundStationID, String satelliteID) throws Exception {
		return getNextLocationContactEventFor(groundStationID, satelliteID, System.currentTimeMillis());
	}

	@Override
	public LocationContactEvent getNextLocationContactEventFor(String groundStationID, String satelliteID, long from) throws Exception {
		Query query = query(where(FIELD_GROUND_STATION_ID).is(groundStationID)
				.and(FIELD_SATELLITE_ID).is(satelliteID)
				.and(FIELD_START_TIME).gte(from))
				.with(sortByStartTimeAsc)
				.limit(1);
		
		return wrapReturn(template.findOne(query, LocationContactEvent.class));
	}

	// XXX: Once again, what if we have different instances of metadata with the same ID?
	// They might both get into the result, but filtering here is O(n*log(n)).
	// If this turns out to be a real issue, might have to add "last-version" flag
	// to Mongo DBObjects
	@Override
	public List<Metadata> getMetadata(EntityInstance subject) {
		Query query = query(where(FIELD_APPLICABLE_TO).is(subject.getID()));
		
		return template.find(query, Metadata.class);
	}

}
