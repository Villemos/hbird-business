package org.hbird.business.simulator;

import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.hbird.business.simulator.waveforms.Waveform;
import org.hbird.transport.commons.util.BitSetUtility;
import org.hbird.transport.commons.util.exceptions.BitSetOperationException;
import org.hbird.transport.spacesystemmodel.Container;
import org.hbird.transport.spacesystemmodel.ContainerFactory;
import org.hbird.transport.spacesystemmodel.exceptions.UnknownContainerNameException;
import org.hbird.transport.spacesystemmodel.parameters.Parameter;
import org.hbird.transport.spacesystemmodel.parameters.ParameterContainer;

/**
 * CCSDS Space System model defined telemetry simulator. Acronyms used: SSM = Space System Model
 * 
 * @author Mark Doyle
 * @author Johannes Klug
 * 
 */
public class SimulatorSSM {

	@EndpointInject(uri = "seda:simMessages")
	ProducerTemplate template;

	/** Root packet {@link Container} name */
	private static String packetHeaderAlias;

	/** Factory producing the SSM */
	ContainerFactory ssmFactory;
	Container packetRoot;
	Map<String, ParameterContainer> allParams;

	private Map<String, Waveform> waveformMap;

	private long messageInterval = 1000;

	public Map<String, Waveform> getWaveformMap() {
		return waveformMap;
	}

	public void setWaveformMap(final Map<String, Waveform> waveformMap) {
		this.waveformMap = waveformMap;
	}

	public void setMessageInterval(final long messageInterval) {
		this.messageInterval = messageInterval;
	}

	public SimulatorSSM(final ContainerFactory spaceSystemModelFactory, final String packetRootName) throws UnknownContainerNameException {
		this.ssmFactory = spaceSystemModelFactory;
		this.packetRoot = ssmFactory.getContainer(packetRootName);
		this.allParams = ssmFactory.getAllParameters();
	}

	public final Collection<ParameterContainer> getAllParameters() {
		return this.ssmFactory.getAllParameters().values();
	}

	/**
	 * Returns the Telemetry container sets. These are the abstract containers that group related parameters together.
	 * 
	 * @param packetNode
	 * @param sections
	 * @return
	 * @throws UnknownContainerNameException
	 */
	public final List<Container> getAllPacketSections(final String packetNode, final List<Container> sections) throws UnknownContainerNameException {
		Container packetSection = this.ssmFactory.getContainer(packetNode);

		if (!(packetSection instanceof ParameterContainer)) {
			System.out.println("Adding section " + packetSection.getName());
			sections.add(packetSection);

			for (Container c : packetSection.getSubContainers()) {
				this.getAllPacketSections(c.getName(), sections);
			}
			System.out.println("Found " + sections.size() + " Packet sections");
		}

		return sections;
	}

	public final Collection<Container> getAllContainers() {
		return this.ssmFactory.getAllContainers();
	}

	public final Map<Parameter, List<String>> getAllParameterRestrictions() {
		Map<Parameter, List<String>> restrictions = ssmFactory.getAllParameterRestrictions();
		for (Parameter p : restrictions.keySet()) {
			System.out.print("Possible " + p.getName() + " restrictions are: ");
			for (String val : restrictions.get(p)) {
				System.out.print(val + " ");
			}
			System.out.println();
		}

		return restrictions;
	}

	public final void getPackets() {
		Map<Parameter, List<String>> restrictions = this.getAllParameterRestrictions();

		for (Parameter p : restrictions.keySet()) {
			for (String val : restrictions.get(p)) {
				((ParameterContainer) p).setValue(Integer.parseInt(val));
				packetRoot.getSubContainers();
			}
		}
	}

	public synchronized BitSet encode(final String name, final Map<String, Double> fields) throws BitSetOperationException, UnknownContainerNameException {
		for (Map.Entry<String, Double> entry : fields.entrySet()) {
			ParameterContainer parameter = ssmFactory.getParameter(entry.getKey());
			if (parameter == null) {
				throw new UnknownContainerNameException(entry.getKey());
			}
			else {
				parameter.setValue(entry.getValue());
			}
		}

		BitSet packet = new BitSet();
		ssmFactory.getContainer(name).marshall(packet, 0);
		return packet;
	}

	public void generateMessage() {
		Map<String, Double> fields = new HashMap<String, Double>();

		for (Map.Entry<String, Waveform> entry : waveformMap.entrySet()) {
			fields.put(entry.getKey(), entry.getValue().nextValue());
		}

		BitSet encodedPacketAsBitset;
		try {
			encodedPacketAsBitset = encode(packetRoot.getName(), fields);
			template.sendBody(BitSetUtility.toByteArray(encodedPacketAsBitset, encodedPacketAsBitset.size()));
		}
		catch (BitSetOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (UnknownContainerNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	//Setter is needed only for the Humsat-integrationtest of the Transport Tier.  
	public void setTemplate(ProducerTemplate template) {
		this.template = template;
	}
}
