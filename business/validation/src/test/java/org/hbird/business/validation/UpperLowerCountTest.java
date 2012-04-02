package org.hbird.business.validation;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.hbird.exchange.core.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;
import org.hbird.exchange.core.StateParameter;


@ContextConfiguration (locations={"/UpperLowerCountTest-context.xml"})
public class UpperLowerCountTest extends AbstractJUnit38SpringContextTests {

	@Produce(uri = "direct:Commands")
    protected ProducerTemplate template;

	@Autowired
    protected CamelContext context;
	
	@EndpointInject(uri = "mock:result")
	protected MockEndpoint resultEndpoint;

	
	@Produce(uri = "direct:switchAll")
    protected ProducerTemplate switchAll;

	@Produce(uri = "direct:enabledUpperLimit")
    protected ProducerTemplate enabledUpperLimit1;

	@Produce(uri = "direct:enabledLowerLimit")
    protected ProducerTemplate enabledLowerLimit1;

	
	
	@Produce(uri = "direct:UpperLimitParameter")
    protected ProducerTemplate UpperLimitParameter;

	@Produce(uri = "direct:LowerLimitParameter")
    protected ProducerTemplate LowerLimitParameter;
	
	
	
	@Produce(uri = "direct:parameter")
    protected ProducerTemplate parameter;
	
	@Produce(uri = "direct:StartParameterUpperLimit")
    protected ProducerTemplate startParameterUpperLimit;
	
	@Produce(uri = "direct:StartParameterLowerLimit")
    protected ProducerTemplate startParameterLowerLimit;

	
	
	protected Exchange exchange = null;
	
	protected void sendParameter(ProducerTemplate template, Parameter parameter) {
		template.sendBody(parameter);		
	}
	
	public void testEjectionMessage() throws Exception {
		
		assertTrue(resultEndpoint.getReceivedCounter() == 0);
		
		/** Both limits enabled (null). Send a few parameters that are all in limit. Validate that no 
		 * state changes occur. 
		 * 
		 * Two messages will be send;
		 * 1) UpperLimit = true
		 * 2) LowerLimit = true
		 */

		sendParameter(parameter, new Parameter("", "Parameter1", "", 1d, ""));
		assertTrue(resultEndpoint.getReceivedCounter() == 2);
		assertTrue(((StateParameter) resultEndpoint.getExchanges().get(0).getIn().getBody()).getStateValue() == true);
		assertTrue(((StateParameter) resultEndpoint.getExchanges().get(1).getIn().getBody()).getStateValue() == true);
		
		sendParameter(parameter, new Parameter("", "Parameter1", "", 2d, ""));
		sendParameter(parameter, new Parameter("", "Parameter1", "", 1d, ""));
		sendParameter(parameter, new Parameter("", "Parameter1", "", 0d, ""));
		assertTrue(resultEndpoint.getReceivedCounter() == 2);
		
		
		/** Send a violation. Count is 1 under the limit of violations, so no warning is send. */
		sendParameter(parameter, new Parameter("", "Parameter1", "", 6d, ""));
		assertTrue(resultEndpoint.getReceivedCounter() == 2);
		
		/** Send a valid parameter. Clears the violation counter. */
		sendParameter(parameter, new Parameter("", "Parameter1", "", 2d, ""));		
		assertTrue(resultEndpoint.getReceivedCounter() == 2);
		
		/** Send a violation. Count is 1 under the limit of violations, so no warning is send. */
		sendParameter(parameter, new Parameter("", "Parameter1", "", 6d, ""));
		assertTrue(resultEndpoint.getReceivedCounter() == 2);
		
		/** Send the second violation. */
		
		/**	One messages will be send;
		 * 3) UpperLimit = false
		 */
		sendParameter(parameter, new Parameter("", "Parameter1", "", 7d, ""));
		assertTrue(resultEndpoint.getReceivedCounter() == 3);
		assertTrue(((StateParameter) resultEndpoint.getExchanges().get(2).getIn().getBody()).getStateValue() == false);

		
		
		
		/** Violate lower limit. Upper limit will again become true, lower limit will remain true
		 *  because this is the first violation. */		
		
		/**	One messages will be send;
		 * 4) UpperLimit = true
		 */
		sendParameter(parameter, new Parameter("", "Parameter1", "", -4d, ""));
		assertTrue(resultEndpoint.getReceivedCounter() == 4);
		assertTrue(((StateParameter) resultEndpoint.getExchanges().get(3).getIn().getBody()).getStateValue() == true);

		/** Send a second lower limit violation. */
		
		/**	One messages will be send;
		 * 5) LowerLimit = false
		 */
		sendParameter(parameter, new Parameter("", "Parameter1", "", -5d, ""));
		assertTrue(resultEndpoint.getReceivedCounter() == 5);
		assertTrue(((StateParameter) resultEndpoint.getExchanges().get(4).getIn().getBody()).getStateValue() == false);

		/** Change the limit so that the parameter gets back in limit. */
		
		/**	One messages will be send;
		 * 6) LowerLimit = true
		 */
		sendParameter(LowerLimitParameter, new Parameter("", "Parameter1", "", -6d, ""));
		assertTrue(resultEndpoint.getReceivedCounter() == 6);
		assertTrue(((StateParameter) resultEndpoint.getExchanges().get(5).getIn().getBody()).getStateValue() == true);
		
		
		/** Disable all limit checking. */
		sendParameter(switchAll, new Parameter("", "Disable All", "", new Boolean(false), ""));
		
		/** Violate limits just for fun. */
		sendParameter(startParameterUpperLimit, new Parameter("", "Parameter1", "", 20d, ""));		
		assertTrue(resultEndpoint.getReceivedCounter() == 6);

		sendParameter(startParameterUpperLimit, new Parameter("", "Parameter1", "", 30d, ""));
		assertTrue(resultEndpoint.getReceivedCounter() == 6);

		sendParameter(startParameterUpperLimit, new Parameter("", "Parameter1", "", 20d, ""));
		assertTrue(resultEndpoint.getReceivedCounter() == 6);

		/** Enable limit checking.
		 * 
		 *  This will immediately trigger the calculation of the limit. The counter will register this,
		 *  but no message will be issued.*/
		sendParameter(switchAll, new Parameter("", "Enable All", "", new Boolean(true), ""));
		assertTrue(resultEndpoint.getReceivedCounter() == 6);
		
		/** Send another violation. */
		
		/**	One messages will be send;
		 * 7) LowerLimit = false
		 */
		sendParameter(startParameterUpperLimit, new Parameter("", "Parameter1", "", 30d, ""));
		assertTrue(resultEndpoint.getReceivedCounter() == 7);

	}
 }
