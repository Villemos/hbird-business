package org.hbird.business.systemtest;

import java.util.List;

import org.apache.camel.Handler;
import org.apache.log4j.Logger;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.State;
import org.hbird.exchange.groundstation.GroundStation;
import org.hbird.exchange.navigation.Satellite;

public class DataAccessApiTester extends SystemTest {

    private static org.apache.log4j.Logger LOG = Logger.getLogger(DataAccessApiTester.class);

    @Handler
    public void process() throws Exception {

        LOG.info("------------------------------------------------------------------------------------------------------------");
        LOG.info("Starting");
        LOG.info("DataAccessApiTester is disabled for now");

        startMonitoringArchive();

        Thread.sleep(2000);

        /** Publish parameters. */
        LOG.info("Publishing parameters.");

        publishParameter("PARA1", "PARA1", "", 1, "", 1);
        publishState("STATE1_OF_PARA1", "STATE1_OF_PARA1", "", "PARA1", true, 1);
        publishState("STATE2_OF_PARA1", "STATE2_OF_PARA1", "", "PARA1", true, 1);

        publishParameter("PARA1", "PARA1", "", 1, "", 2);
        publishState("STATE1_OF_PARA1", "STATE1_OF_PARA1", "", "PARA1", true, 2);
        publishState("STATE2_OF_PARA1", "STATE2_OF_PARA1", "", "PARA1", true, 2);

        publishParameter("PARA2", "PARA2", "", 2, "", 3);
        publishState("STATE1_OF_PARA2", "STATE1_OF_PARA2", "", "PARA2", true, 3);

        publishParameter("PARA1", "PARA1", "", 1, "", 4);
        publishState("STATE1_OF_PARA1", "STATE1_OF_PARA1", "", "PARA1", true, 4);
        publishState("STATE2_OF_PARA1", "STATE2_OF_PARA1", "", "PARA1", true, 4);

        publishParameter("PARA1", "PARA1", "", 1, "", 5);
        publishState("STATE1_OF_PARA1", "STATE1_OF_PARA1", "", "PARA1", true, 5);
        publishState("STATE2_OF_PARA1", "STATE2_OF_PARA1", "", "PARA1", true, 5);

        publishParameter("PARA2", "PARA2", "", 1, "", 6);
        publishState("STATE1_OF_PARA2", "STATE1_OF_PARA2", "", "PARA2", true, 6);

        publishParameter("PARA3", "PARA3", "", 1, "", 7);

        publishParameter("PARA1", "PARA1", "", 1, "", 8);
        publishState("STATE1_OF_PARA1", "STATE1_OF_PARA1", "", "PARA1", true, 8);
        publishState("STATE2_OF_PARA1", "STATE2_OF_PARA1", "", "PARA1", true, 8);

        publishParameter("PARA1", "PARA1", "", 1, "", 9);
        publishState("STATE1_OF_PARA1", "STATE1_OF_PARA1", "", "PARA1", true, 9);
        publishState("STATE2_OF_PARA1", "STATE2_OF_PARA1", "", "PARA1", true, 9);

        publishParameter("PARA4", "PARA4", "", 1, "", 10);
        publishState("STATE1_OF_PARA4", "STATE1_OF_PARA4", "", estcube1.getID() + "/PARA4", true, 10);

        publishParameter("PARA5", "PARA5", "", 1, "", 11);

        publishParameter("PARA1", "PARA1", "", 1, "", 12);
        publishState("STATE1_OF_PARA1", "STATE1_OF_PARA1", "", "PARA1", true, 12);
        publishState("STATE2_OF_PARA1", "STATE2_OF_PARA1", "", "PARA1", true, 12);

        publishParameter("PARA2", "PARA2", "", 1, "", 13);
        publishState("STATE1_OF_PARA2", "STATE1_OF_PARA2", "", "PARA2", true, 13);

        publishParameter("PARA2", "PARA2", "", 1, "", 14);
        publishState("STATE1_OF_PARA2", "STATE1_OF_PARA2", "", "PARA2", true, 14);

        publishState("STATE1_OF_PARA1", "STATE1_OF_PARA1", "", "PARA1", true, 15);
        publishParameter("PARA1", "PARA1", "", 1, "", 15);
        publishState("STATE2_OF_PARA1", "STATE2_OF_PARA1", "", "PARA1", true, 15);

        publishState("STATE1_OF_PARA1", "STATE1_OF_PARA1", "", "PARA1", true, 16);
        publishState("STATE2_OF_PARA1", "STATE2_OF_PARA1", "", "PARA1", true, 16);
        publishParameter("PARA1", "PARA1", "", 1, "", 16);

        publishParameter("PARA3", "PARA3", "", 1, "", 17);

        publishParameter("PARA1", "PARA1", "", 1, "", 18);
        publishState("STATE1_OF_PARA1", "STATE1_OF_PARA1", "", "PARA1", true, 18);
        publishState("STATE2_OF_PARA1", "STATE2_OF_PARA1", "", "PARA1", true, 18);

        publishParameter("PARA3", "PARA3", "", 1, "", 19);

        publishParameter("PARA1", "PARA1", "", 1, "", 20);
        publishState("STATE1_OF_PARA1", "STATE1_OF_PARA1", "", "PARA1", true, 20);
        publishState("STATE2_OF_PARA1", "STATE2_OF_PARA1", "", "PARA1", true, 20);

        publishApi.publish(es5ec);
        publishApi.publish(gsAalborg);
        publishApi.publish(gsDarmstadt);
        publishApi.publish(gsNewYork);

        publishApi.publish(estcube1);
        publishApi.publish(strand);
        publishApi.publish(deCube1);
        publishApi.publish(dkCube1);

        Thread.sleep(5000);

        Parameter parameter = null;
        List<Parameter> parameters = null;

        /** Test the CATALOGUE interface. */
        List<Parameter> parameterCatalogue = catalogueApi.getParameters();
        azzert(parameterCatalogue.size() == 5, "Expect to receive 5 parameter metadata. Received " +
                parameterCatalogue.size());

        List<State> stateCatalogue = catalogueApi.getStates();
        azzert(stateCatalogue.size() == 4, "Expect to receive 4 state metadata. Received " + stateCatalogue.size());

        List<GroundStation> locationCatalogue = catalogueApi.getGroundStations();
        azzert(locationCatalogue.size() == 4, "Expect to receive 4 ground station metadata. Received " +
                locationCatalogue.size());

        List<Satellite> satelliteCatalogue = catalogueApi.getSatellites();
        azzert(satelliteCatalogue.size() == 4, "Expect to receive 4 satellite metadata. Received " +
                satelliteCatalogue.size());

        // Single parameter, without state. */
        parameter = accessApi.getById("PARA1", Parameter.class);
        azzert(parameter != null, "Expect to receive 1 value.");
        azzert(parameter.getTimestamp() == 20, "Expect to receive timestamp 20.");

        // Single parameter at timestamp 1
        parameters = accessApi.getById("PARA1", 1, 1, Parameter.class);
        azzert(parameters.size() == 1, "Expect to receive one value in range [1,1].");
        azzert(parameters.get(0).getTimestamp() == 1, "Expect to receive timestamp 1.");

        // Multiple values of the same parameter
        parameters = accessApi.getById("PARA1", 2, 5, Parameter.class);
        azzert(parameters.size() == 3, "Except to receive 3 values in range [2,5]");
        azzert(parameters.get(0).getTimestamp() == 2);
        azzert(parameters.get(1).getTimestamp() == 4);
        azzert(parameters.get(2).getTimestamp() == 5);

        // All parameters
        parameters = accessApi.getAll(Parameter.class);
        azzert(parameters.size() == 5, "Expect to receive 5 parameters");

        // All states for a parameter
        List<State> states = accessApi.getApplicableTo("PARA1", State.class);
        azzert(states.size() == 2, "Expect to receive 2 states");
        azzert(states.get(0).getName().equals("STATE1_OF_PARA1") ||
                states.get(1).getName().equals("STATE1_OF_PARA1"), "STATE1 is present");
        azzert(states.get(0).getName().equals("STATE2_OF_PARA1") ||
                states.get(1).getName().equals("STATE2_OF_PARA1"), "STATE2 is present");
        azzert(states.get(0).getTimestamp() == 20 && states.get(0).getValue());
        azzert(states.get(1).getTimestamp() == 20 && states.get(1).getValue());

        // States in a range for a parameter
        states = accessApi.getApplicableTo("PARA1", State.class, 3, 7);
        azzert(states.size() == 4, "Expect to receive 4 states, received " + states.size());

        // Order of two states with the same timestamp is not determined
        if (states.get(0).getID().equals("STATE1_OF_PARA1")) {
            azzert(states.get(1).getID().equals("STATE2_OF_PARA1"), "STATE2 is at index 1, got" + states.get(1).getID());
        }
        else {
            azzert(states.get(0).getID().equals("STATE2_OF_PARA1"), "STATE2 is at index 0, got" + states.get(0).getID());
            azzert(states.get(1).getID().equals("STATE1_OF_PARA1"), "STATE1 is at index 1, got" + states.get(1).getID());
        }

        azzert(states.get(0).getTimestamp() == 4, "Index 0 has timestamp 4");
        azzert(states.get(1).getTimestamp() == 4, "Index 1 has timestamp 4");

        if (states.get(2).getID().equals("STATE1_OF_PARA1")) {
            azzert(states.get(3).getID().equals("STATE2_OF_PARA1"), "STATE2 is at index 3, got" + states.get(3).getID());
        }
        else {
            azzert(states.get(2).getID().equals("STATE2_OF_PARA1"), "STATE2 is at index 2, got " + states.get(2).getID());
            azzert(states.get(3).getID().equals("STATE1_OF_PARA1"), "STATE1 is at index 3, got " + states.get(3).getID());
        }

        azzert(states.get(2).getTimestamp() == 5, "Index 2 has timestamp 5");
        azzert(states.get(3).getTimestamp() == 5, "Index 3 has timestamp 5");

        LOG.info("Finished");
    }
}
