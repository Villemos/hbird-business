package org.hbird.business.api.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hbird.exchange.core.Entity;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.groundstation.GroundStation;
import org.hbird.exchange.navigation.Satellite;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CatalogueTest {
	private InMemoryStore dao;
	private Catalogue catalogue;
	
	@Before
	public void setUp() {
		dao = new InMemoryStore();
		catalogue = new Catalogue("catalogue", "catalogue", dao);
	}
	
	@Test
	public void testGetParameters() throws Exception {
		String id1 = "velocity";
		Parameter p1 = new Parameter(id1, id1);
		dao.save(p1);
		
		String id2 = "acceleration";
		Parameter p2 = new Parameter(id2, id2);
		dao.save(p2);
		
		List<Parameter> params = catalogue.getParameters();
		
		assertEquals(2, params.size());
		
		assertTrue(params.get(0).getID().equals(id1) && params.get(0).getName().equals(id1) ||
				params.get(1).getID().equals(id1) && params.get(1).getName().equals(id1));
		
		assertTrue(params.get(0).getID().equals(id2) && params.get(0).getName().equals(id2) ||
				params.get(1).getID().equals(id2) && params.get(1).getName().equals(id2));
	}
	
	@Test
	public void testGetParametersWhenMultipleInstances() throws Exception {
		String id = "velocity";
		
		int versions[] = new int[] {15, 1, 10, 11, 25, 16, 8};
		
		for(int i = 0; i < versions.length; i++) {
			System.out.println("Creating parameter " + i);
			Parameter par = new Parameter(id, id);
			par.setVersion(versions[i]);
			
			dao.save(par);
		}
		
		dao.dump();
		
		List<Parameter> params = catalogue.getParameters();
		
		assertEquals(1, params.size());
		
		assertEquals(id, params.get(0).getID());
		assertEquals(25, params.get(0).getVersion());
	}
	
	@Test
	public void testGetGroundstations() throws Exception {
		String id1 = "foo";
		String id2 = "bar";
		int versionOld = 5;
		int versionNew = 10;
		
		GroundStation gs1 = new GroundStation(id1, id1);
		gs1.setVersion(versionOld);
		
		GroundStation gs2 = new GroundStation(id2, id2);
		gs2.setVersion(versionOld);
		
		GroundStation gs1_instance2 = new GroundStation(id1, id1);
		gs1_instance2.setVersion(versionNew);
		
		dao.save(gs1);
		dao.save(gs2);
		dao.save(gs1_instance2);
		
		List<GroundStation> groundStations = catalogue.getGroundStations();
		
		assertEquals(2, groundStations.size());
		
		int gs1index = -1;
		int gs2index = -1;
		
		if(groundStations.get(0).getID().equals(id1)) {
			gs1index = 0;
			gs2index = 1;
		} else if(groundStations.get(1).getID().equals(id1)) {
			gs1index = 1;
			gs2index = 0;
		}
		
		assertEquals(id2, groundStations.get(gs2index).getID());
		
		assertEquals(versionOld, groundStations.get(gs2index).getVersion());
		assertEquals(versionNew, groundStations.get(gs1index).getVersion());
	}
	
	@Test
	public void testGetSatelliteByName() throws Exception {
		String name1 = "foo";
		String name2 = "bar";
		
		int versionOld = 5;
		int versionNew = 10;
		
		Satellite sat1 = new Satellite(name1, name1);
		sat1.setVersion(versionOld);
		
		Satellite sat2 = new Satellite(name2, name2);
		sat2.setVersion(versionNew);
		
		Satellite sat1_instance2 = new Satellite(name1, name1);
		sat1_instance2.setVersion(versionNew);
		
		dao.save(sat1_instance2);
		dao.save(sat1);
		dao.save(sat2);
		
		Satellite sat = catalogue.getSatelliteByName(name1);
		
		assertEquals(name1, sat.getID());
		assertEquals(name1, sat.getName());
		assertEquals(versionNew, sat.getVersion());
	}
	
	@Test
	public void testGetGroundstationsByNames() throws Exception {
		String id1 = "foo";
		String id2 = "bar";
		String id3 = "baz";
		int versionOld = 5;
		int versionNew = 10;
		
		GroundStation gs1 = new GroundStation(id1, id1);
		gs1.setVersion(versionOld);
		
		GroundStation gs2 = new GroundStation(id2, id2);
		gs2.setVersion(versionOld);
		
		GroundStation gs1_instance2 = new GroundStation(id1, id1);
		gs1_instance2.setVersion(versionNew);
		
		GroundStation gs3 = new GroundStation(id3, id3);
		gs3.setVersion(versionOld);
		
		dao.save(gs1);
		dao.save(gs2);
		dao.save(gs1_instance2);
		dao.save(gs3);
		
		List<String> names = new ArrayList<String>();
		names.add(id1);
		names.add(id2);
		
		List<GroundStation> groundStations = catalogue.getGroundStationsByName(names);
		
		assertEquals(2, groundStations.size());
		
		int gs1index = -1;
		int gs2index = -1;
		
		if(groundStations.get(0).getID().equals(id1)) {
			gs1index = 0;
			gs2index = 1;
		} else if(groundStations.get(1).getID().equals(id1)) {
			gs1index = 1;
			gs2index = 0;
		}
		
		assertEquals(id2, groundStations.get(gs2index).getID());
		
		assertEquals(versionOld, groundStations.get(gs2index).getVersion());
		assertEquals(versionNew, groundStations.get(gs1index).getVersion());
	}
	
	public static void main(String[] args) throws Exception {
		CatalogueTest test = new CatalogueTest();
		test.setUp();
		test.testGetParametersWhenMultipleInstances();
	}
	
}
