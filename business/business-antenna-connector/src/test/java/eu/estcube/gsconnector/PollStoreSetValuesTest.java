package eu.estcube.gsconnector;

import static org.junit.Assert.assertTrue;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import eu.estcube.domain.JMSConstants;
import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRotatorConstants;

public class PollStoreSetValuesTest {

    private PollStoreSetValues storeSetValues;
    private DefaultExchange exchange;
    private CamelContext ctx;

    @Before
    public void setUp() throws Exception {
        storeSetValues = new PollStoreSetValues();
        ctx = Mockito.mock(CamelContext.class);
        exchange = new DefaultExchange(ctx);
    }

    @Test
    public void testStoreData() {
        TelemetryCommand command = new TelemetryCommand(TelemetryRotatorConstants.SET_POSITION);
        command.addParameter(new TelemetryParameter("Elevation", "10"));
        exchange.getIn().setBody(command);
        storeSetValues.storeData(exchange);
        assertTrue(PollStoreSetValues.getRequiredParameterValueDataStore().get(TelemetryRotatorConstants.SET_POSITION).equals(command));
        assertTrue(PollStoreSetValues.getRequiredParameterValueDataStore().size() == 1);
        assertTrue(exchange.getIn().getHeader(JMSConstants.HEADER_POLLING).equals(JMSConstants.POLL_SET_COMMAND));
        PollStoreSetValues.getRequiredParameterValueDataStore().remove(TelemetryRotatorConstants.SET_POSITION);

        command = new TelemetryCommand(TelemetryRotatorConstants.GET_POSITION);
        command.addParameter(new TelemetryParameter("Elevation", "10"));
        exchange.getIn().setHeader(JMSConstants.HEADER_POLLING, JMSConstants.POLL_GET_COMMAND);
        exchange.getIn().setBody(command);
        storeSetValues.storeData(exchange);
        assertTrue(PollStoreSetValues.getRequiredParameterValueDataStore().size() == 0);
        assertTrue(exchange.getIn().getHeader(JMSConstants.HEADER_POLLING).equals(JMSConstants.POLL_GET_COMMAND));

        command = new TelemetryCommand("AnyCommand");
        command.addParameter(new TelemetryParameter("Elevation", "10"));
        exchange.getIn().removeHeader(JMSConstants.HEADER_POLLING);
        exchange.getIn().setBody(command);
        storeSetValues.storeData(exchange);
        assertTrue(PollStoreSetValues.getRequiredParameterValueDataStore().size() == 0);
        assertTrue(exchange.getIn().getHeader(JMSConstants.HEADER_POLLING).equals(JMSConstants.POLL_NOT_REQUIRED));

        exchange.getIn().removeHeader(JMSConstants.HEADER_POLLING);
        exchange.getIn().setBody("SomeKindOfWrongMessage");
        storeSetValues.storeData(exchange);
        assertTrue(exchange.getIn().getHeaders().isEmpty());
        assertTrue(exchange.getIn().getBody() == null);
    }

}
