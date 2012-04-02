package org.hbird.business.simulator;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.hbird.business.simulator.SimulatorSSM;
import org.hbird.transport.commons.util.BitSetUtility;
import org.hbird.transport.commons.util.exceptions.BitSetOperationException;
import org.hbird.transport.spacesystemmodel.Container;
import org.hbird.transport.spacesystemmodel.exceptions.UnknownContainerNameException;
import org.hbird.transport.spacesystemmodel.parameters.Parameter;
import org.hbird.transport.spacesystemmodel.parameters.ParameterContainer;
import org.hbird.transport.spacesystemmodel.testsupport.MockParameterContainerModel;
import org.hbird.transport.xtce.XtceModelFactory;
import org.hbird.transport.xtce.exceptions.InvalidXtceFileException;

/**
 * FIXME A lot of these aren't tests they are just drivers for me to test functionality.  Asserts need to 
 * be added ASAP!  We need to get the sim up and running yesterday so I have no time right now. - Mark
 * 
 * @author Mark
 *
 */
public class SimulatorSSMTest {

	private static final String TM_PACKET_ALIAS = "TMPacket";
	SimulatorSSM sim;
	private static XtceModelFactory xtce;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		xtce = new XtceModelFactory(SimulatorSSMTest.class.getResource("/spacesystemdefs/humsat.xml").getFile());
	}

	@Before
	public void setUp() throws Exception {
		sim = new SimulatorSSM(new MockParameterContainerModel(), MockParameterContainerModel.TM_PACKET_ALIAS);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetAllParameters() throws UnknownContainerNameException, InvalidXtceFileException {
		Collection<ParameterContainer> allParams = sim.getAllParameters();
		System.out.println("Number params = " + allParams.size());

		for (Container c : allParams) {
			for (Container p : c.getParents()) {
				System.out.println("Parameter " + c.getName() + " has a parent called = " + p.getName());
			}
		}
		
		System.out.println("###############################################");
		sim = new SimulatorSSM(xtce, TM_PACKET_ALIAS);
		allParams = sim.getAllParameters();
		System.out.println("Number params = " + allParams.size());

		for (Container c : allParams) {
			for (Container p : c.getParents()) {
				System.out.println("Parameter " + c.getName() + " has a parent called = " + p.getName());
			}
		}
	}

	@Test
	public void testGetAllPacketSections() throws UnknownContainerNameException, InvalidXtceFileException {
		List<Container> sections = new ArrayList<Container>();
		sections = sim.getAllPacketSections(MockParameterContainerModel.TM_PACKET_ALIAS, sections);
		for (Container c : sections) {
			for (Container p : c.getParents()) {
				System.out.println(c.getName() + " has a parent called = " + p.getName());
			}
		}

		System.out.println("###############################################");

		sections = new ArrayList<Container>();
		sim = new SimulatorSSM(xtce, TM_PACKET_ALIAS);
		sections = sim.getAllPacketSections(TM_PACKET_ALIAS, sections);
		for (Container c : sections) {
			for (Container p : c.getParents()) {
				System.out.println(c.getName() + " has a parent called = " + p.getName());
			}
		}

		System.out.println("###############################################");

		sections = new ArrayList<Container>();
		sim = new SimulatorSSM(xtce, TM_PACKET_ALIAS);
		sections = sim.getAllPacketSections(TM_PACKET_ALIAS, sections);
		for (Container c : sections) {
			for (Container p : c.getParents()) {
				System.out.println(c.getName() + " has a parent called = " + p.getName());
			}
		}
	}

	@Test
	public void testGetContainerRestrictions() {
		Map<Parameter, List<String>> restrictions = sim.getAllParameterRestrictions();
		assertEquals(restrictions.size(), 1);
		assertEquals(restrictions.get(sim.ssmFactory.getParameter(MockParameterContainerModel.PAYLOAD_APID_ALIAS)).size(), 3);
	}
	
	@Test
	public void encode() throws UnknownContainerNameException, BitSetOperationException {
		// This test resembles the marshall() test for the Packet Broker.
		Map<String, Double> fields = new HashMap<String, Double>();
		
		fields.put("ApId", (double) 555);
		fields.put("PayloadLength", (double) 64);
		fields.put("LaserTemp", 17959.25);
		
		BitSet result = sim.encode(TM_PACKET_ALIAS, fields);
		
		/* APID 555 LE unsigned */
		final String LASER_DATA_APID = "11010100010";
		final String PACKET_LENGTH_64 = "0000001000000000";
		/* 17949.25 Laser temperature (64 bit signed float) */
		final String LASER_TEMP_17959_25 = "0100000011010001100010011101000000000000000000000000000000000000";
		
		BitSet expected = BitSetUtility.stringToBitSet(LASER_DATA_APID + PACKET_LENGTH_64 + LASER_TEMP_17959_25, false, false);
		
		assertEquals(expected, result);
	}

	@Test
	public void testgetPackets() {
		sim.getPackets();
	}

}
