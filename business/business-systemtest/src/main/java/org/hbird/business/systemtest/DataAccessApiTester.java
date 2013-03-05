package org.hbird.business.systemtest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.camel.Handler;
import org.apache.log4j.Logger;
import org.hbird.business.api.ApiFactory;
import org.hbird.business.api.ICatalogue;
import org.hbird.business.api.IDataAccess;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.State;
import org.hbird.exchange.navigation.GroundStation;
import org.hbird.exchange.navigation.Satellite;

public class DataAccessApiTester extends SystemTest {

    private static org.apache.log4j.Logger LOG = Logger.getLogger(CommandingTester.class);

    @Handler
    public void process() throws InterruptedException {

        LOG.info("------------------------------------------------------------------------------------------------------------");
        LOG.info("Starting");

        startMonitoringArchive();

        Thread.sleep(2000);

        /** Publish parameters. */
        LOG.info("Publishing parameters.");

        publishApi.publishParameter("PARA1", "", "", 1, "", 1);
        publishApi.publishState("STATE1_OF_PARA1", "", "", "PARA1", true, 1);
        publishApi.publishState("STATE2_OF_PARA1", "", "", "PARA1", true, 1);

        publishApi.publishParameter("PARA1", "", "", 1, "", 2);
        publishApi.publishState("STATE1_OF_PARA1", "", "", "PARA1", true, 2);
        publishApi.publishState("STATE2_OF_PARA1", "", "", "PARA1", true, 2);

        publishApi.publishParameter("PARA2", "", "", 2, "", 3);
        publishApi.publishState("STATE1_OF_PARA2", "", "", "PARA2", true, 3);

        publishApi.publishParameter("PARA1", "", "", 1, "", 4);
        publishApi.publishState("STATE1_OF_PARA1", "", "", "PARA1", true, 4);
        publishApi.publishState("STATE2_OF_PARA1", "", "", "PARA1", true, 4);

        publishApi.publishParameter("PARA1", "", "", 1, "", 5);
        publishApi.publishState("STATE1_OF_PARA1", "", "", "PARA1", true, 5);
        publishApi.publishState("STATE2_OF_PARA1", "", "", "PARA1", true, 5);

        publishApi.publishParameter("PARA2", "", "", 1, "", 6);
        publishApi.publishState("STATE1_OF_PARA2", "", "", "PARA2", true, 6);

        publishApi.publishParameter("PARA3", "", "", 1, "", 7);

        publishApi.publishParameter("PARA1", "", "", 1, "", 8);
        publishApi.publishState("STATE1_OF_PARA1", "", "", "PARA1", true, 8);
        publishApi.publishState("STATE2_OF_PARA1", "", "", "PARA1", true, 8);

        publishApi.publishParameter("PARA1", "", "", 1, "", 9);
        publishApi.publishState("STATE1_OF_PARA1", "", "", "PARA1", true, 9);
        publishApi.publishState("STATE2_OF_PARA1", "", "", "PARA1", true, 9);

        publishApi.publishParameter("PARA4", "", "", 1, "", 10);
        publishApi.publishState("STATE1_OF_PARA4", "", "", "PARA4", true, 10);

        publishApi.publishParameter("PARA5", "", "", 1, "", 11);

        publishApi.publishParameter("PARA1", "", "", 1, "", 12);
        publishApi.publishState("STATE1_OF_PARA1", "", "", "PARA1", true, 12);
        publishApi.publishState("STATE2_OF_PARA1", "", "", "PARA1", true, 12);

        publishApi.publishParameter("PARA2", "", "", 1, "", 13);
        publishApi.publishState("STATE1_OF_PARA2", "", "", "PARA2", true, 13);

        publishApi.publishParameter("PARA2", "", "", 1, "", 14);
        publishApi.publishState("STATE1_OF_PARA2", "", "", "PARA2", true, 14);

        publishApi.publishState("STATE1_OF_PARA1", "", "", "PARA1", true, 15);
        publishApi.publishParameter("PARA1", "", "", 1, "", 15);
        publishApi.publishState("STATE2_OF_PARA1", "", "", "PARA1", true, 15);

        publishApi.publishState("STATE1_OF_PARA1", "", "", "PARA1", true, 16);
        publishApi.publishState("STATE2_OF_PARA1", "", "", "PARA1", true, 16);
        publishApi.publishParameter("PARA1", "", "", 1, "", 16);

        publishApi.publishParameter("PARA3", "", "", 1, "", 17);

        publishApi.publishParameter("PARA1", "", "", 1, "", 18);
        publishApi.publishState("STATE1_OF_PARA1", "", "", "PARA1", true, 18);
        publishApi.publishState("STATE2_OF_PARA1", "", "", "PARA1", true, 18);

        publishApi.publishParameter("PARA3", "", "", 1, "", 19);

        publishApi.publishParameter("PARA1", "", "", 1, "", 20);
        publishApi.publishState("STATE1_OF_PARA1", "", "", "PARA1", true, 20);
        publishApi.publishState("STATE2_OF_PARA1", "", "", "PARA1", true, 20);

        publishApi.publishGroundStation("LOC1", "", "", null, null, null);
        publishApi.publishGroundStation("LOC2", "", "", null, null, null);
        publishApi.publishGroundStation("LOC3", "", "", null, null, null);
        publishApi.publishGroundStation("LOC4", "", "", null, null, null);
        
        publishApi.publishSatellite("SAT1", "", "");
        publishApi.publishSatellite("SAT2", "", "");
        publishApi.publishSatellite("SAT3", "", "");

        Thread.sleep(5000);

        /** Send command to commit all changes. */
        forceCommit();

        IDataAccess api = ApiFactory.getDataAccessApi("systemtest");
        ICatalogue catalogueApi = ApiFactory.getCatalogueApi("systemtest");

        Parameter parameter = null;
        Map<Parameter, List<State>> parameterAndStates = null;
        List<Parameter> parameters = null;

        List<String> names = new ArrayList<String>();
        names.add("PARA1");
        names.add("PARA2");

        Iterator<Entry<Parameter, List<State>>> it = null;
        Entry<Parameter, List<State>> entry = null;

        /** Test the CATALOGUE interface. */
        List<Parameter> parameterCatalogue = catalogueApi.getParameters();
        azzert(parameterCatalogue.size() == 5, "Expect to receive 5 parameter metadata. Received " + parameterCatalogue.size());

        List<State> stateCatalogue = catalogueApi.getStates();
        azzert(stateCatalogue.size() == 4, "Expect to receive 4 state metadata. Received " + stateCatalogue.size());

        List<GroundStation> locationCatalogue = catalogueApi.getGroundStations();
        azzert(locationCatalogue.size() == 4, "Expect to receive 3 ground station metadata. Received " + locationCatalogue.size());

        List<Satellite> satelliteCatalogue = catalogueApi.getSatellites();
        azzert(satelliteCatalogue.size() == 3, "Expect to receive 3 satellite metadata. Received " + satelliteCatalogue.size());

        /** Test the initialization API. */

        /** Single parameter, without state. */
        parameter = api.getParameter("PARA1");
        azzert(parameter != null, "Expect to receive 1 value.");
        azzert(parameter.getTimestamp() == 20, "Expect to receive timestamp 20.");

        parameters = api.getParameter("PARA1", 4);
        azzert(parameters.size() == 4, "Expect to receive 4 values. Received " + parameters.size());

        parameter = api.getParameterAt("PARA1", 19);
        azzert(parameter != null, "Expect to receive 1 value.");
        azzert(parameter.getName().equals("PARA1"));
        azzert(parameter.getTimestamp() == 18);

        parameters = api.getParameterAt("PARA1", 19, 3);
        azzert(parameters.size() == 3, "Expect to receive 3 values.");
        azzert(parameters.get(0).getTimestamp() == 18);
        azzert(parameters.get(1).getTimestamp() == 16);
        azzert(parameters.get(2).getTimestamp() == 15);

        /** Single parameter, with state. */
        parameterAndStates = api.getParameterAndStates("PARA1");
        azzert(parameterAndStates.size() == 1, "Expect to receive 1 parameter sample.");

        it = parameterAndStates.entrySet().iterator();
        entry = it.next();
        azzert(entry.getKey().getTimestamp() == 20, "Expect to receive timestamp 20.");
        azzert(entry.getValue().size() == 2, "Expect to receive 2 states.");

        parameterAndStates = api.getParameterAndStatesAt("PARA1", 19);
        azzert(parameterAndStates.size() == 1, "Expect to receive 1 parameter sample.");

        it = parameterAndStates.entrySet().iterator();
        entry = it.next();
        azzert(entry.getKey().getTimestamp() == 18, "Expect to receive timestamp 18.");
        azzert(entry.getValue().size() == 2, "Expect to receive 2 states.");

        /** Multiple parameters, without state. */
        parameters = api.getParameters(names);
        azzert(parameters.size() == 2, "Expect to receive 2 values.");

        parameters = api.getParametersAt(names, 19);
        azzert(parameters.size() == 2, "Expect to receive 2 value.");
        azzert(parameters.get(0).getName().equals("PARA1"), "First parameter PARA1");
        azzert(parameters.get(0).getTimestamp() == 18, "With timestamp 18");
        azzert(parameters.get(1).getName().equals("PARA2"), "Second parameter PARA2");
        azzert(parameters.get(1).getTimestamp() == 14, "With timestamp 14");

        /** Multiple parameters, with state. */
        parameterAndStates = api.getParametersAndStates(names);
        azzert(parameterAndStates.size() == 2, "Expect to receive 2 values.");

        it = parameterAndStates.entrySet().iterator();
        entry = it.next();
        azzert(entry.getKey().getName().equals("PARA1"), "First parameter PARA1");
        azzert(entry.getKey().getTimestamp() == 20, "With timestamp 20");
        azzert(entry.getValue().size() == 2, "And 2 states");

        entry = it.next();
        azzert(entry.getKey().getName().equals("PARA2"), "Second parameter PARA2");
        azzert(entry.getKey().getTimestamp() == 14, "With timestamp 14");
        azzert(entry.getValue().size() == 1, "And 1 state");

        parameters = api.getParametersAt(names, 19);

        azzert(parameters.get(0).getName().equals("PARA1"), "Parameter PARA1");
        azzert(parameters.get(0).getTimestamp() == 18, "With timestamp 18");

        azzert(parameters.get(1).getName().equals("PARA2"), "Parameter PARA2");
        azzert(parameters.get(1).getTimestamp() == 14, "With timestamp 14");

        /** STEPPING */

        /** Test the API for single parameter, no state. */
        parameters = api.retrieveParameter("PARA1", 2, 13);
        azzert(parameters.size() == 6, "Expect 6 parameter samples");

        parameters = api.retrieveParameter("PARA1", 2, 13, 4);
        azzert(parameters.size() == 4, "Expect 4 parameter samples");

        /** Test the API for single parameter, including state. */
        parameterAndStates = api.retrieveParameterAndStates("PARA1", 3, 15);
        azzert(parameterAndStates.size() == 6, "Expect to receive 6 values. Received " + parameterAndStates.size());

        // NOTE: This will retrieve 3 ENTRIES. This will be: PARA1, STATE, STATE. The result mapped on parameters will
        // thus be 1, and not 3.
        parameterAndStates = api.retrieveParameterAndStates("PARA1", 3, 15, 3);
        azzert(parameterAndStates.size() == 1, "Expect to receive 1 values. Received " + parameterAndStates.size());

        /** Test the API for multiple parameter, no state */
        parameters = api.retrieveParameters(names, 3, 15);
        azzert(parameters.size() == 10, "Expect to receive 10 values. Received " + parameters.size());

        parameters = api.retrieveParameters(names, 3, 15, 3);
        azzert(parameters.size() == 3, "Expect to receive 3 values. Received " + parameters.size());

        /** Test the API for multiple parameters, including state */
        parameterAndStates = api.retrieveParametersAndStates(names, 3, 15);
        azzert(parameterAndStates.size() == 10, "Expect to receive 10 values. Received " + parameterAndStates.size());

        parameterAndStates = api.retrieveParametersAndStates(names, 3, 15, 5);
        azzert(parameterAndStates.size() == 2, "Expect to receive 2 values. Received " + parameterAndStates.size());

        LOG.info("Finished");
    }
}
