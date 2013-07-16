package org.hbird.business.archive.dao.mongo;

import java.net.UnknownHostException;
import java.util.List;

import org.hbird.business.archive.dao.mongo.MongoDataAccess;
import org.hbird.exchange.core.Entity;
import org.hbird.exchange.core.EntityInstance;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.Mongo;

import static org.junit.Assert.*;

class TestEntityInstance extends EntityInstance {
	private String testField;

	public TestEntityInstance(String ID, String name, String testField) {
		super(ID, name);
		
		this.testField = testField;
	}
	
	public String getTestField() {
		return testField;
	}
} 
public class MongoDataAccessTest {
	private static final String TEST_DATABASE_NAME = "hbird_test";
	
	private MongoDataAccess dao;
	private Mongo mongo;
	
	@Before
	public void setUp() throws UnknownHostException {
		mongo = new Mongo("localhost", 27017);
		
		MongoOperations ops = new MongoTemplate(mongo, TEST_DATABASE_NAME);
		dao = new MongoDataAccess(ops);
	}
	
	@After
	public void tearDown() {
		mongo.dropDatabase(TEST_DATABASE_NAME);
		mongo.close();
	}
	
	@Test
	public void testSavingAndRetrievingOneByID() throws Exception {
		String id = "foo";
		String name = "an entity";
		
		TestEntityInstance entity = new TestEntityInstance(id, name, name);
		
		dao.save(entity);
		TestEntityInstance retrieved = dao.getById(id, TestEntityInstance.class);
		
		assertEquals(id, retrieved.getID());
		assertEquals(name, retrieved.getName());
	}
	
	@Test
	public void testOverwritingByInstanceID() throws Exception {
		String id = "foo";
		String name1 = "first entity";
		String name2 = "second entity";
		
		TestEntityInstance first = new TestEntityInstance(id, name1, name1);
		first.setVersion(0);
		TestEntityInstance second = new TestEntityInstance(id, name2, name2);
		second.setVersion(0);
		
		assertEquals(first.getInstanceID(), second.getInstanceID());
		
		TestEntityInstance retrieved;
		
		dao.save(first);
		
		retrieved = dao.getByInstanceId(first.getInstanceID(), TestEntityInstance.class);
		assertEquals(first.getName(), retrieved.getName());
		
		dao.save(second);
		
		retrieved = dao.getByInstanceId(second.getInstanceID(), TestEntityInstance.class);
		assertEquals(second.getName(), retrieved.getName());
		
		List<TestEntityInstance> all = dao.getAll(TestEntityInstance.class);
		
		assertEquals(1, all.size());
	}
	
	@Test
	public void testSavingAndRetrievingOneByInstanceID() throws Exception {
		String id = "foo";
		String name = "an entity instance";
		String testField = "bar";
		
		TestEntityInstance entity = new TestEntityInstance(id, name, testField);
		
		dao.save(entity);
		TestEntityInstance retrieved = dao.getByInstanceId(entity.getInstanceID(), TestEntityInstance.class);
		
		assertEquals(id, retrieved.getID());
		assertEquals(name, retrieved.getName());
		assertEquals(testField, retrieved.getTestField());
		
		assertEquals(entity.getTimestamp(), retrieved.getTimestamp());
		assertEquals(entity.getVersion(), retrieved.getVersion());
	}
	
	@Test
	public void testSavingManyInstancesAndRetrievingLatestByID() throws Exception {
		String id = "foo";
		
		TestEntityInstance first = new TestEntityInstance(id, id, id);
		first.setVersion(0);
		TestEntityInstance second = new TestEntityInstance(id, id, id);
		second.setVersion(1);
		
		assertTrue("Versions of two successively created objects are different", first.getVersion() < second.getVersion());
		
		dao.save(second);
		dao.save(first);
		
		TestEntityInstance retrieved = dao.getById(id, TestEntityInstance.class);
		
		assertEquals(id, retrieved.getID());
		assertEquals(second.getVersion(), retrieved.getVersion());
		assertEquals(second.getInstanceID(), retrieved.getInstanceID());
	}
	
	@Test
	public void testSavingManyAndRetrievingAll() throws Exception {
		TestEntityInstance ent1 = new TestEntityInstance("foo", "foo", "foo");
		TestEntityInstance ent2 = new TestEntityInstance("bar", "bar", "bar");
		
		dao.save(ent1);
		dao.save(ent2);
		
		List<TestEntityInstance> retrieved = dao.getAll(TestEntityInstance.class);
		
		assertEquals(2, retrieved.size());
		assertTrue(retrieved.get(0).getID().equals(ent1.getID()) && retrieved.get(1).getID().equals(ent2.getID()) || 
				retrieved.get(1).getID().equals(ent1.getID()) && retrieved.get(0).getID().equals(ent2.getID()));
	}

}
