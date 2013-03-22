package eu.estcube.gsconnector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.impl.DefaultMessage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import eu.estcube.domain.JMSConstants;
import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryObject;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRadioConstants;
import eu.estcube.domain.TelemetryRotatorConstants;

public class PollCheckValuesTest {

    private PollCheckValues pollCommands;
    private DefaultExchange exchange;
    private CamelContext ctx;
    PollStoreSetValues storeSetValues;

    @Before
    public void setUp() throws Exception {
        pollCommands = new PollCheckValues();
        ctx = Mockito.mock(CamelContext.class);
        exchange = new DefaultExchange(ctx);
        storeSetValues = new PollStoreSetValues();
    }

    @Test
    public void testCheckPolling() {


        // if RPRT is wrong with set_command
        exchange.getIn().setHeader(JMSConstants.HEADER_POLLING, JMSConstants.POLL_SET_COMMAND);
        TelemetryObject telemetryObject = new TelemetryObject(TelemetryRotatorConstants.SET_POSITION, new Date());
        telemetryObject.addParameter(new TelemetryParameter("RPRT", "1"));
        exchange.getIn().setBody(telemetryObject);
        TelemetryCommand telemetryCommandForDataStore = new TelemetryCommand(TelemetryRotatorConstants.SET_POSITION);
        telemetryCommandForDataStore.addParameter(new TelemetryParameter("Azimuth", "10"));
        telemetryCommandForDataStore.addParameter(new TelemetryParameter("Elevation", "10"));
        PollStoreSetValues.getRequiredParameterValueDataStore().put(telemetryCommandForDataStore.getCommandName(),
                telemetryCommandForDataStore);
        List<Message> messageList = pollCommands.checkPolling(exchange);
        assertTrue(messageList.size() == 1);
        assertEquals(exchange.getIn().toString(), messageList.get(0).toString());
        assertTrue(PollStoreSetValues.getRequiredParameterValueDataStore().size() == 0);

        // if RPRT is wrong with get_command
        exchange.getIn().setHeader(JMSConstants.HEADER_POLLING, JMSConstants.POLL_GET_COMMAND);
        telemetryObject = new TelemetryObject(TelemetryRotatorConstants.GET_POSITION, new Date());
        telemetryObject.addParameter(new TelemetryParameter("RPRT", "1"));
        exchange.getIn().setBody(telemetryObject);
        telemetryCommandForDataStore = new TelemetryCommand(TelemetryRotatorConstants.SET_POSITION);
        telemetryCommandForDataStore.addParameter(new TelemetryParameter("Azimuth", "10"));
        telemetryCommandForDataStore.addParameter(new TelemetryParameter("Elevation", "10"));
        PollStoreSetValues.getRequiredParameterValueDataStore().put(telemetryCommandForDataStore.getCommandName(),
                telemetryCommandForDataStore);
        messageList = pollCommands.checkPolling(exchange);
        assertTrue(messageList.size() == 1);
        assertEquals(exchange.getIn().toString(), messageList.get(0).toString());
        assertTrue(PollStoreSetValues.getRequiredParameterValueDataStore().size() == 0);
        
        //if if is neither set or get 
        exchange.getIn().setHeader(JMSConstants.HEADER_POLLING, JMSConstants.POLL_NOT_REQUIRED);
        telemetryObject = new TelemetryObject(TelemetryRotatorConstants.HELP, new Date());
        telemetryObject.addParameter(new TelemetryParameter("RPRT", "0"));
        exchange.getIn().setBody(telemetryObject);
        messageList = pollCommands.checkPolling(exchange);
        assertTrue(messageList.size() == 1);
        assertEquals(exchange.getIn().toString(), messageList.get(0).toString());
        assertTrue(PollStoreSetValues.getRequiredParameterValueDataStore().size() == 0);
        
        //if has not poll header
        exchange.getIn().removeHeader(JMSConstants.HEADER_POLLING);
        telemetryObject = new TelemetryObject(TelemetryRotatorConstants.HELP, new Date());
        telemetryObject.addParameter(new TelemetryParameter("RPRT", "0"));
        exchange.getIn().setBody(telemetryObject);
        messageList = pollCommands.checkPolling(exchange);
        assertTrue(messageList.size() == 1);
        assertEquals(exchange.getIn().toString(), messageList.get(0).toString());

        // if set command is received
        exchange.getIn().setHeader(JMSConstants.HEADER_POLLING, JMSConstants.POLL_SET_COMMAND);
        telemetryObject = new TelemetryObject(TelemetryRotatorConstants.SET_POSITION, new Date());
        telemetryObject.addParameter(new TelemetryParameter("Azimuth", "10"));
        telemetryObject.addParameter(new TelemetryParameter("Elevation", "10"));
        telemetryObject.addParameter(new TelemetryParameter("RPRT", "0"));
        exchange.getIn().setBody(telemetryObject);
        messageList = pollCommands.checkPolling(exchange);
        DefaultMessage pollingMessage = new DefaultMessage();
        TelemetryCommand command = new TelemetryCommand(TelemetryRotatorConstants.GET_POSITION);
        pollingMessage.setBody(command);
        pollingMessage.setHeader(JMSConstants.HEADER_DEVICE, exchange.getIn().getHeader(JMSConstants.HEADER_DEVICE));
        pollingMessage.setHeader(JMSConstants.HEADER_GROUNDSTATIONID, exchange.getIn().getHeader(JMSConstants.HEADER_GROUNDSTATIONID));
        pollingMessage.setHeader(JMSConstants.HEADER_POLLING, JMSConstants.POLL_GET_COMMAND);
        pollingMessage.setHeader(JMSConstants.HEADER_FORWARD, JMSConstants.DIRECT_CHOOSE_DEVICE);
        TelemetryCommand testTelemetryCommand = (TelemetryCommand) messageList.get(1).getBody();
        assertTrue(messageList.size() == 2);
        assertEquals(exchange.getIn().toString(), messageList.get(0).toString());
        assertEquals(command.getCommandName(), testTelemetryCommand.getCommandName());
        assertEquals(command.getParams(), testTelemetryCommand.getParams());
        assertEquals(pollingMessage.getHeaders(), messageList.get(1).getHeaders());

        // if StoreSetValues cache is empty
        telemetryObject = new TelemetryObject(TelemetryRotatorConstants.GET_POSITION, new Date());
        telemetryObject.addParameter(new TelemetryParameter("Azimuth", "10"));
        telemetryObject.addParameter(new TelemetryParameter("Elevation", "10"));
        telemetryObject.addParameter(new TelemetryParameter("RPRT", "0"));
        exchange.getIn().setBody(telemetryObject);
        exchange.getIn().setHeader(JMSConstants.HEADER_POLLING, JMSConstants.POLL_GET_COMMAND);
        messageList = pollCommands.checkPolling(exchange);
        pollingMessage = new DefaultMessage();
        command = new TelemetryCommand(TelemetryRotatorConstants.GET_POSITION);
        pollingMessage.setBody(command);
        pollingMessage.setHeader(JMSConstants.HEADER_DEVICE, exchange.getIn().getHeader(JMSConstants.HEADER_DEVICE));
        pollingMessage.setHeader(JMSConstants.HEADER_GROUNDSTATIONID, exchange.getIn().getHeader(JMSConstants.HEADER_GROUNDSTATIONID));
        pollingMessage.setHeader(JMSConstants.HEADER_POLLING, JMSConstants.POLL_GET_COMMAND);
        pollingMessage.setHeader(JMSConstants.HEADER_FORWARD, JMSConstants.DIRECT_CHOOSE_DEVICE);
        assertTrue(messageList.size() == 1);
        assertEquals(exchange.getIn().toString(), messageList.get(0).toString());
        assertEquals(command.getCommandName(), testTelemetryCommand.getCommandName());

        // If parameters decimals and true
        telemetryObject = new TelemetryObject(TelemetryRotatorConstants.GET_POSITION, new Date());
        exchange.getIn().setHeader(JMSConstants.HEADER_POLLING, JMSConstants.POLL_GET_COMMAND);
        telemetryObject.addParameter(new TelemetryParameter("get_pos", ""));
        telemetryObject.addParameter(new TelemetryParameter("Azimuth", "10"));
        telemetryObject.addParameter(new TelemetryParameter("Elevation", "10"));
        telemetryObject.addParameter(new TelemetryParameter("RPRT", "0"));
        telemetryCommandForDataStore = new TelemetryCommand(TelemetryRotatorConstants.SET_POSITION);
        telemetryCommandForDataStore.addParameter(new TelemetryParameter("Azimuth", "10"));
        telemetryCommandForDataStore.addParameter(new TelemetryParameter("Elevation", "10"));
        PollStoreSetValues.getRequiredParameterValueDataStore().put(telemetryCommandForDataStore.getCommandName(),
                telemetryCommandForDataStore);
        exchange.getIn().setBody(telemetryObject);
        messageList = pollCommands.checkPolling(exchange);
        pollingMessage = new DefaultMessage();
        command = new TelemetryCommand(TelemetryRotatorConstants.GET_POSITION);
        pollingMessage.setBody(command);
        pollingMessage.setHeader(JMSConstants.HEADER_DEVICE, exchange.getIn().getHeader(JMSConstants.HEADER_DEVICE));
        pollingMessage.setHeader(JMSConstants.HEADER_GROUNDSTATIONID, exchange.getIn().getHeader(JMSConstants.HEADER_GROUNDSTATIONID));
        pollingMessage.setHeader(JMSConstants.HEADER_POLLING, JMSConstants.POLL_GET_COMMAND);
        pollingMessage.setHeader(JMSConstants.HEADER_FORWARD, JMSConstants.DIRECT_CHOOSE_DEVICE);
        assertTrue(messageList.size() == 1);
        assertEquals(exchange.getIn().toString(), messageList.get(0).toString());

        // if parameters are decimals and false
        telemetryObject = new TelemetryObject(TelemetryRotatorConstants.GET_POSITION, new Date());
        exchange.getIn().setHeader(JMSConstants.HEADER_POLLING, JMSConstants.POLL_GET_COMMAND);
        telemetryObject.addParameter(new TelemetryParameter("get_pos", ""));
        telemetryObject.addParameter(new TelemetryParameter("Azimuth", "20"));
        telemetryObject.addParameter(new TelemetryParameter("Elevation", "20"));
        telemetryObject.addParameter(new TelemetryParameter("RPRT", "0"));
        telemetryCommandForDataStore = new TelemetryCommand(TelemetryRotatorConstants.SET_POSITION);
        telemetryCommandForDataStore.addParameter(new TelemetryParameter("Azimuth", "10"));
        telemetryCommandForDataStore.addParameter(new TelemetryParameter("Elevation", "10"));
        PollStoreSetValues.getRequiredParameterValueDataStore().put(telemetryCommandForDataStore.getCommandName(),
                telemetryCommandForDataStore);
        exchange.getIn().setBody(telemetryObject);
        messageList = pollCommands.checkPolling(exchange);
        pollingMessage = new DefaultMessage();
        command = new TelemetryCommand(TelemetryRotatorConstants.GET_POSITION);
        pollingMessage.setBody(command);
        pollingMessage.setHeader(JMSConstants.HEADER_DEVICE, exchange.getIn().getHeader(JMSConstants.HEADER_DEVICE));
        pollingMessage.setHeader(JMSConstants.HEADER_GROUNDSTATIONID, exchange.getIn().getHeader(JMSConstants.HEADER_GROUNDSTATIONID));
        pollingMessage.setHeader(JMSConstants.HEADER_POLLING, JMSConstants.POLL_GET_COMMAND);
        pollingMessage.setHeader(JMSConstants.HEADER_FORWARD, JMSConstants.DIRECT_CHOOSE_DEVICE);
        assertTrue(messageList.size() == 2);
        testTelemetryCommand = (TelemetryCommand) messageList.get(1).getBody();
        assertEquals(exchange.getIn().toString(), messageList.get(0).toString());
        assertEquals(command.getCommandName(), testTelemetryCommand.getCommandName());
        assertEquals(command.getParams(), testTelemetryCommand.getParams());
        assertEquals(pollingMessage.getHeaders(), messageList.get(1).getHeaders());

        // If parameters are Strings and false
        telemetryObject = new TelemetryObject(TelemetryRotatorConstants.GET_POSITION, new Date());
        exchange.getIn().setHeader(JMSConstants.HEADER_POLLING, JMSConstants.POLL_GET_COMMAND);
        telemetryObject.addParameter(new TelemetryParameter("get_pos", ""));
        telemetryObject.addParameter(new TelemetryParameter("Azimuth", "Hello"));
        telemetryObject.addParameter(new TelemetryParameter("Elevation", "VAK"));
        telemetryObject.addParameter(new TelemetryParameter("RPRT", "0"));
        telemetryCommandForDataStore = new TelemetryCommand(TelemetryRotatorConstants.SET_POSITION);
        telemetryCommandForDataStore.addParameter(new TelemetryParameter("Azimuth", "OMG"));
        telemetryCommandForDataStore.addParameter(new TelemetryParameter("Elevation", "ZOMG"));
        PollStoreSetValues.getRequiredParameterValueDataStore().put(telemetryCommandForDataStore.getCommandName(),
                telemetryCommandForDataStore);
        exchange.getIn().setBody(telemetryObject);
        messageList = pollCommands.checkPolling(exchange);
        pollingMessage = new DefaultMessage();
        command = new TelemetryCommand(TelemetryRotatorConstants.GET_POSITION);
        pollingMessage.setBody(command);
        pollingMessage.setHeader(JMSConstants.HEADER_DEVICE, exchange.getIn().getHeader(JMSConstants.HEADER_DEVICE));
        pollingMessage.setHeader(JMSConstants.HEADER_GROUNDSTATIONID, exchange.getIn().getHeader(JMSConstants.HEADER_GROUNDSTATIONID));
        pollingMessage.setHeader(JMSConstants.HEADER_POLLING, JMSConstants.POLL_GET_COMMAND);
        pollingMessage.setHeader(JMSConstants.HEADER_FORWARD, JMSConstants.DIRECT_CHOOSE_DEVICE);
        assertTrue(messageList.size() == 2);
        testTelemetryCommand = (TelemetryCommand) messageList.get(1).getBody();
        assertEquals(exchange.getIn().toString(), messageList.get(0).toString());
        assertEquals(command.getCommandName(), testTelemetryCommand.getCommandName());
        assertEquals(command.getParams(), testTelemetryCommand.getParams());
        assertEquals(pollingMessage.getHeaders(), messageList.get(1).getHeaders());

        // if parameters are Strings and true
        telemetryObject = new TelemetryObject(TelemetryRotatorConstants.GET_POSITION, new Date());
        exchange.getIn().setHeader(JMSConstants.HEADER_POLLING, JMSConstants.POLL_GET_COMMAND);
        telemetryObject.addParameter(new TelemetryParameter("get_pos", ""));
        telemetryObject.addParameter(new TelemetryParameter("Azimuth", "a"));
        telemetryObject.addParameter(new TelemetryParameter("Elevation", "b"));
        telemetryObject.addParameter(new TelemetryParameter("RPRT", "0"));
        telemetryCommandForDataStore = new TelemetryCommand(TelemetryRotatorConstants.SET_POSITION);
        telemetryCommandForDataStore.addParameter(new TelemetryParameter("Azimuth", "a"));
        telemetryCommandForDataStore.addParameter(new TelemetryParameter("Elevation", "b"));
        PollStoreSetValues.getRequiredParameterValueDataStore().put(telemetryCommandForDataStore.getCommandName(),
                telemetryCommandForDataStore);
        exchange.getIn().setBody(telemetryObject);
        messageList = pollCommands.checkPolling(exchange);
        pollingMessage = new DefaultMessage();
        command = new TelemetryCommand(TelemetryRotatorConstants.GET_POSITION);
        pollingMessage.setBody(command);
        pollingMessage.setHeader(JMSConstants.HEADER_DEVICE, exchange.getIn().getHeader(JMSConstants.HEADER_DEVICE));
        pollingMessage.setHeader(JMSConstants.HEADER_GROUNDSTATIONID, exchange.getIn().getHeader(JMSConstants.HEADER_GROUNDSTATIONID));
        pollingMessage.setHeader(JMSConstants.HEADER_POLLING, JMSConstants.POLL_GET_COMMAND);
        pollingMessage.setHeader(JMSConstants.HEADER_FORWARD, JMSConstants.DIRECT_CHOOSE_DEVICE);
        assertTrue(messageList.size() == 1);
        assertEquals(exchange.getIn().toString(), messageList.get(0).toString());

        // if one parameter is decimal and one is String and both are true
        telemetryObject = new TelemetryObject(TelemetryRotatorConstants.GET_POSITION, new Date());
        exchange.getIn().setHeader(JMSConstants.HEADER_POLLING, JMSConstants.POLL_GET_COMMAND);
        telemetryObject.addParameter(new TelemetryParameter("get_pos", ""));
        telemetryObject.addParameter(new TelemetryParameter("Azimuth", "a"));
        telemetryObject.addParameter(new TelemetryParameter("Elevation", "1"));
        telemetryObject.addParameter(new TelemetryParameter("RPRT", "0"));
        telemetryCommandForDataStore = new TelemetryCommand(TelemetryRotatorConstants.SET_POSITION);
        telemetryCommandForDataStore.addParameter(new TelemetryParameter("Azimuth", "a"));
        telemetryCommandForDataStore.addParameter(new TelemetryParameter("Elevation", "1"));
        PollStoreSetValues.getRequiredParameterValueDataStore().put(telemetryCommandForDataStore.getCommandName(),
                telemetryCommandForDataStore);
        exchange.getIn().setBody(telemetryObject);
        messageList = pollCommands.checkPolling(exchange);
        pollingMessage = new DefaultMessage();
        command = new TelemetryCommand(TelemetryRotatorConstants.GET_POSITION);
        pollingMessage.setBody(command);
        pollingMessage.setHeader(JMSConstants.HEADER_DEVICE, exchange.getIn().getHeader(JMSConstants.HEADER_DEVICE));
        pollingMessage.setHeader(JMSConstants.HEADER_GROUNDSTATIONID, exchange.getIn().getHeader(JMSConstants.HEADER_GROUNDSTATIONID));
        pollingMessage.setHeader(JMSConstants.HEADER_POLLING, JMSConstants.POLL_GET_COMMAND);
        pollingMessage.setHeader(JMSConstants.HEADER_FORWARD, JMSConstants.DIRECT_CHOOSE_DEVICE);
        assertTrue(messageList.size() == 1);
        assertEquals(exchange.getIn().toString(), messageList.get(0).toString());

        // If first parameters are String and one is true and other is false
        telemetryObject = new TelemetryObject(TelemetryRotatorConstants.GET_POSITION, new Date());
        exchange.getIn().setHeader(JMSConstants.HEADER_POLLING, JMSConstants.POLL_GET_COMMAND);
        telemetryObject.addParameter(new TelemetryParameter("get_pos", ""));
        telemetryObject.addParameter(new TelemetryParameter("Azimuth", "Hello"));
        telemetryObject.addParameter(new TelemetryParameter("Elevation", "VAK"));
        telemetryObject.addParameter(new TelemetryParameter("RPRT", "0"));
        telemetryCommandForDataStore = new TelemetryCommand(TelemetryRotatorConstants.SET_POSITION);
        telemetryCommandForDataStore.addParameter(new TelemetryParameter("Azimuth", "Hello"));
        telemetryCommandForDataStore.addParameter(new TelemetryParameter("Elevation", "goodbye"));
        PollStoreSetValues.getRequiredParameterValueDataStore().put(telemetryCommandForDataStore.getCommandName(),
                telemetryCommandForDataStore);
        exchange.getIn().setBody(telemetryObject);
        messageList = pollCommands.checkPolling(exchange);
        pollingMessage = new DefaultMessage();
        command = new TelemetryCommand(TelemetryRotatorConstants.GET_POSITION);
        pollingMessage.setBody(command);
        pollingMessage.setHeader(JMSConstants.HEADER_DEVICE, exchange.getIn().getHeader(JMSConstants.HEADER_DEVICE));
        pollingMessage.setHeader(JMSConstants.HEADER_GROUNDSTATIONID, exchange.getIn().getHeader(JMSConstants.HEADER_GROUNDSTATIONID));
        pollingMessage.setHeader(JMSConstants.HEADER_POLLING, JMSConstants.POLL_GET_COMMAND);
        pollingMessage.setHeader(JMSConstants.HEADER_FORWARD, JMSConstants.DIRECT_CHOOSE_DEVICE);
        assertTrue(messageList.size() == 2);
        testTelemetryCommand = (TelemetryCommand) messageList.get(1).getBody();
        assertEquals(exchange.getIn().toString(), messageList.get(0).toString());
        assertEquals(command.getCommandName(), testTelemetryCommand.getCommandName());
        assertEquals(command.getParams(), testTelemetryCommand.getParams());
        assertEquals(pollingMessage.getHeaders(), messageList.get(1).getHeaders());
        
        //if Parameter is int and true
        telemetryObject = new TelemetryObject(TelemetryRadioConstants.GET_FREQUENCY, new Date());
        exchange.getIn().setHeader(JMSConstants.HEADER_POLLING, JMSConstants.POLL_GET_COMMAND);
        telemetryObject.addParameter(new TelemetryParameter("get_freq", ""));
        telemetryObject.addParameter(new TelemetryParameter("Frequency", "150"));
        telemetryObject.addParameter(new TelemetryParameter("RPRT", "0"));
        telemetryCommandForDataStore = new TelemetryCommand(TelemetryRadioConstants.SET_FREQUENCY);
        telemetryCommandForDataStore.addParameter(new TelemetryParameter("Frequency", "150"));
        PollStoreSetValues.getRequiredParameterValueDataStore().put(telemetryCommandForDataStore.getCommandName(),
                telemetryCommandForDataStore);
        exchange.getIn().setBody(telemetryObject);
        messageList = pollCommands.checkPolling(exchange);
        pollingMessage = new DefaultMessage();
        command = new TelemetryCommand(TelemetryRadioConstants.GET_FREQUENCY);
        pollingMessage.setBody(command);
        pollingMessage.setHeader(JMSConstants.HEADER_DEVICE, exchange.getIn().getHeader(JMSConstants.HEADER_DEVICE));
        pollingMessage.setHeader(JMSConstants.HEADER_GROUNDSTATIONID, exchange.getIn().getHeader(JMSConstants.HEADER_GROUNDSTATIONID));
        pollingMessage.setHeader(JMSConstants.HEADER_POLLING, JMSConstants.POLL_GET_COMMAND);
        pollingMessage.setHeader(JMSConstants.HEADER_FORWARD, JMSConstants.DIRECT_CHOOSE_DEVICE);
        assertTrue(messageList.size() == 1);
        assertEquals(exchange.getIn().toString(), messageList.get(0).toString());
        
        //if Parameter is int that is converted from decimal and true
        telemetryObject = new TelemetryObject(TelemetryRadioConstants.GET_FREQUENCY, new Date());
        exchange.getIn().setHeader(JMSConstants.HEADER_POLLING, JMSConstants.POLL_GET_COMMAND);
        telemetryObject.addParameter(new TelemetryParameter("get_freq", ""));
        telemetryObject.addParameter(new TelemetryParameter("Frequency", "110"));
        telemetryObject.addParameter(new TelemetryParameter("Frequency2", "100"));
        telemetryObject.addParameter(new TelemetryParameter("RPRT", "0"));
        telemetryCommandForDataStore = new TelemetryCommand(TelemetryRadioConstants.SET_FREQUENCY);
        telemetryCommandForDataStore.addParameter(new TelemetryParameter("Frequency", "110.99"));
        telemetryCommandForDataStore.addParameter(new TelemetryParameter("Frequency2", "100.000001"));
        PollStoreSetValues.getRequiredParameterValueDataStore().put(telemetryCommandForDataStore.getCommandName(),
                telemetryCommandForDataStore);
        exchange.getIn().setBody(telemetryObject);
        messageList = pollCommands.checkPolling(exchange);
        pollingMessage = new DefaultMessage();
        command = new TelemetryCommand(TelemetryRadioConstants.GET_FREQUENCY);
        pollingMessage.setBody(command);
        pollingMessage.setHeader(JMSConstants.HEADER_DEVICE, exchange.getIn().getHeader(JMSConstants.HEADER_DEVICE));
        pollingMessage.setHeader(JMSConstants.HEADER_GROUNDSTATIONID, exchange.getIn().getHeader(JMSConstants.HEADER_GROUNDSTATIONID));
        pollingMessage.setHeader(JMSConstants.HEADER_POLLING, JMSConstants.POLL_GET_COMMAND);
        pollingMessage.setHeader(JMSConstants.HEADER_FORWARD, JMSConstants.DIRECT_CHOOSE_DEVICE);
        assertTrue(messageList.size() == 1);
        assertEquals(exchange.getIn().toString(), messageList.get(0).toString());
        
        //if Parameter is int and false
        telemetryObject = new TelemetryObject(TelemetryRadioConstants.GET_FREQUENCY, new Date());
        exchange.getIn().setHeader(JMSConstants.HEADER_POLLING, JMSConstants.POLL_GET_COMMAND);
        telemetryObject.addParameter(new TelemetryParameter("get_freq", ""));
        telemetryObject.addParameter(new TelemetryParameter("Frequency", "125"));
        telemetryObject.addParameter(new TelemetryParameter("RPRT", "0"));
        telemetryCommandForDataStore = new TelemetryCommand(TelemetryRadioConstants.SET_FREQUENCY);
        telemetryCommandForDataStore.addParameter(new TelemetryParameter("get_freq", ""));
        telemetryCommandForDataStore.addParameter(new TelemetryParameter("Frequency", "130"));
        PollStoreSetValues.getRequiredParameterValueDataStore().put(telemetryCommandForDataStore.getCommandName(),
                telemetryCommandForDataStore);
        exchange.getIn().setBody(telemetryObject);
        messageList = pollCommands.checkPolling(exchange);
        pollingMessage = new DefaultMessage();
        command = new TelemetryCommand(TelemetryRadioConstants.GET_FREQUENCY);
        pollingMessage.setBody(command);
        pollingMessage.setHeader(JMSConstants.HEADER_DEVICE, exchange.getIn().getHeader(JMSConstants.HEADER_DEVICE));
        pollingMessage.setHeader(JMSConstants.HEADER_GROUNDSTATIONID, exchange.getIn().getHeader(JMSConstants.HEADER_GROUNDSTATIONID));
        pollingMessage.setHeader(JMSConstants.HEADER_POLLING, JMSConstants.POLL_GET_COMMAND);
        pollingMessage.setHeader(JMSConstants.HEADER_FORWARD, JMSConstants.DIRECT_CHOOSE_DEVICE);
        assertTrue(messageList.size() == 2);
        testTelemetryCommand = (TelemetryCommand) messageList.get(1).getBody();
        assertEquals(exchange.getIn().toString(), messageList.get(0).toString());
        assertEquals(command.getCommandName(), testTelemetryCommand.getCommandName());
        assertEquals(command.getParams(), testTelemetryCommand.getParams());
        assertEquals(pollingMessage.getHeaders(), messageList.get(1).getHeaders());

    }

}
