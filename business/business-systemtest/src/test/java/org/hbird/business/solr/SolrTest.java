package org.hbird.business.solr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.junit4.ExchangeTestSupport;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.State;
import org.hbird.exchange.dataaccess.StateRequest;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

// public class SolrTest extends CamelTestSupport {
public class SolrTest extends ExchangeTestSupport {

	@EndpointInject(uri = "mock:result")
    protected MockEndpoint resultEndpoint;

    @Produce(uri = "direct:start")
    protected ProducerTemplate template;

    @Test
    public void testParameterStore() throws Exception {
    	
    	/** Test delete. Force commit. */
        template.sendBodyAndHeader(null, SolrOptions.delete, "*:*");
        template.sendBodyAndHeader(null, SolrOptions.commit, true);
        
    	/** Store parameters. */
        template.sendBody(new Parameter("SystemTestSuite", "PARA1", "", "A test description,", 2d, "Volt"));
        template.sendBody(new Parameter("SystemTestSuite", "PARA1", "", "A test description,", 2.1d, "Volt"));
        template.sendBody(new Parameter("SystemTestSuite", "PARA1", "", "A test description,", 2.2d, "Volt"));
        template.sendBody(new Parameter("SystemTestSuite", "PARA1", "", "A test description,", 2.3d, "Volt"));
        template.sendBody(new Parameter("SystemTestSuite", "PARA1", "", "A test description,", 2.4d, "Volt"));
        template.sendBody(new Parameter("SystemTestSuite", "PARA1", "", "A test description,", 2.5d, "Volt"));
        template.sendBody(new Parameter("SystemTestSuite", "PARA1", "", "A test description,", 2.6d, "Volt"));
        
        template.sendBody(new Parameter("SystemTestSuite", "PARA2", "", "A test description,", 2l, "Meter"));
        template.sendBody(new Parameter("SystemTestSuite", "PARA2", "", "A test description,", 3l, "Meter"));
        template.sendBody(new Parameter("SystemTestSuite", "PARA2", "", "A test description,", 4l, "Meter"));
        template.sendBody(new Parameter("SystemTestSuite", "PARA2", "", "A test description,", 5l, "Meter"));

        template.sendBody(new Parameter("SystemTestSuite", "PARA3", "", "A test description,", 10f, "Seconds"));
        template.sendBody(new Parameter("SystemTestSuite", "PARA3", "", "A test description,", 15f, "Seconds"));
        template.sendBody(new Parameter("SystemTestSuite", "PARA3", "", "A test description,", 20f, "Seconds"));
        template.sendBody(new Parameter("SystemTestSuite", "PARA3", "", "A test description,", 35f, "Seconds"));
    
        /** Test retrieval. */
        template.sendBodyAndHeader(null, SolrOptions.commit, true);
        
        Exchange exchange = createExchange();
        exchange.getIn().setBody("*:*");
        template.send(exchange);
        assertTrue(((List)exchange.getOut().getBody()).size() == 15);
    }

    @Test
    public void testStateRetrieval() throws Exception {
    	        
    	/** Store states. */
        template.sendBody(new State("SystemTestSuite", "STATE1", "A test description,", "COMMAND1", true));
        template.sendBody(new State("SystemTestSuite", "STATE2", "A test description,", "COMMAND1", true));
        template.sendBody(new State("SystemTestSuite", "STATE3", "A test description,", "COMMAND1", false));
        template.sendBody(new State("SystemTestSuite", "STATE4", "A test description,", "COMMAND1", true));
        template.sendBody(new State("SystemTestSuite", "STATE5", "A test description,", "COMMAND1", true));
        template.sendBody(new State("SystemTestSuite", "STATE6", "A test description,", "COMMAND1", false));
        template.sendBody(new State("SystemTestSuite", "STATE7", "A test description,", "COMMAND1", true));
        
        template.sendBody(new State("SystemTestSuite", "STATE8", "A test description,", "COMMAND2", true));
        template.sendBody(new State("SystemTestSuite", "STATE9", "A test description,", "COMMAND2", true));
        template.sendBody(new State("SystemTestSuite", "STATE10", "A test description,", "COMMAND2", false));
        template.sendBody(new State("SystemTestSuite", "STATE11", "A test description,", "COMMAND2", true));
        template.sendBody(new State("SystemTestSuite", "STATE12", "A test description,", "COMMAND2", true));
        
        template.sendBody(new State("SystemTestSuite", "STATE13", "A test description,", "COMMAND3", true));
        template.sendBody(new State("SystemTestSuite", "STATE14", "A test description,", "COMMAND3", true));
        template.sendBody(new State("SystemTestSuite", "STATE15", "A test description,", "COMMAND3", true));
        template.sendBody(new State("SystemTestSuite", "STATE16", "A test description,", "COMMAND3", true));

        template.sendBodyAndHeader(null, SolrOptions.commit, true);

        /** Test retrieval. */
        Exchange exchange = createExchange();
        StateRequest stateRequest = new StateRequest("", "", "COMMAND1");
        exchange.getIn().setBody(stateRequest);
        template.send(exchange);
        
        Map<String, State> states = new HashMap<String, State>();
        for (State state : (List<State>) exchange.getOut().getBody()) {
        	states.put(state.getName(), state);
        }
        assertTrue(states.get("STATE1").getValue() == true);
        assertTrue(states.get("STATE2").getValue() == true);
        assertTrue(states.get("STATE3").getValue() == false);
        assertTrue(states.get("STATE4").getValue() == true);
        assertTrue(states.get("STATE5").getValue() == true);
        assertTrue(states.get("STATE6").getValue() == false);
        assertTrue(states.get("STATE7").getValue() == true);

    	/** Store a new set of states. Notice that STATE1 and STATE3 doesnt change. */
        template.sendBody(new State("SystemTestSuite", "STATE2", "A test description,", "COMMAND1", true));
        template.sendBody(new State("SystemTestSuite", "STATE4", "A test description,", "COMMAND1", true));
        template.sendBody(new State("SystemTestSuite", "STATE5", "A test description,", "COMMAND1", false));
        template.sendBody(new State("SystemTestSuite", "STATE6", "A test description,", "COMMAND1", true));
        template.sendBody(new State("SystemTestSuite", "STATE7", "A test description,", "COMMAND1", true));

        template.sendBodyAndHeader(null, SolrOptions.commit, true);
        
        template.send(exchange);
        
        states = new HashMap<String, State>();
        for (State state : (List<State>) exchange.getOut().getBody()) {
        	states.put(state.getName(), state);
        }
        assertTrue(states.get("STATE1").getValue() == true);
        assertTrue(states.get("STATE2").getValue() == true);
        assertTrue(states.get("STATE3").getValue() == false);
        assertTrue(states.get("STATE4").getValue() == true);
        assertTrue(states.get("STATE5").getValue() == false);
        assertTrue(states.get("STATE6").getValue() == true);
        assertTrue(states.get("STATE7").getValue() == true);
        
        stateRequest.addName("STATE11");
        stateRequest.addName("STATE15");
        template.send(exchange);
        
        states = new HashMap<String, State>();
        for (State state : (List<State>) exchange.getOut().getBody()) {
        	states.put(state.getName(), state);
        }

        assertTrue(states.get("STATE1").getValue() == true);
        assertTrue(states.get("STATE2").getValue() == true);
        assertTrue(states.get("STATE3").getValue() == false);
        assertTrue(states.get("STATE4").getValue() == true);
        assertTrue(states.get("STATE5").getValue() == false);
        assertTrue(states.get("STATE6").getValue() == true);
        assertTrue(states.get("STATE7").getValue() == true);
        assertTrue(states.get("STATE11").getValue() == true);
        assertTrue(states.get("STATE15").getValue() == true);
    }
    
    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {
                from("direct:start").to("solr:store?solrhome=c:/solr");
            }
        };
    }
}