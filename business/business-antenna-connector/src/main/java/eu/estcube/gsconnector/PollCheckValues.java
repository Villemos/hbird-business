package eu.estcube.gsconnector;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultMessage;
import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import eu.estcube.domain.JMSConstants;
import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryObject;
import eu.estcube.domain.TelemetryRadioConstants;
import eu.estcube.domain.TelemetryRotatorConstants;

@Component(JMSConstants.CLASS_POLL_COMMANDS)
public final class PollCheckValues {

    private BidiMap commandsBidiMap;
    private List<String> commandsThatReturnIntegrer;

    @Value("${pollRoundScale}")
    private int pollRoundScale;

    public PollCheckValues() {
        commandsBidiMap = new DualHashBidiMap();
        commandsBidiMap.put(TelemetryRotatorConstants.SET_POSITION, TelemetryRotatorConstants.GET_POSITION);
        commandsBidiMap.put(TelemetryRadioConstants.SET_ANTENNA_NR, TelemetryRadioConstants.GET_ANTENNA_NR);
        commandsBidiMap.put(TelemetryRadioConstants.SET_CTCSS_TONE, TelemetryRadioConstants.GET_CTCSS_TONE);
        commandsBidiMap.put(TelemetryRadioConstants.SET_DCS_CODE, TelemetryRadioConstants.GET_DCS_CODE);
        commandsBidiMap.put(TelemetryRadioConstants.SET_FREQUENCY, TelemetryRadioConstants.GET_FREQUENCY);
        commandsBidiMap.put(TelemetryRadioConstants.SET_MEMORY_CHANNELNR, TelemetryRadioConstants.GET_MEMORY_CHANNELNR);
        commandsBidiMap.put(TelemetryRadioConstants.SET_MODE, TelemetryRadioConstants.GET_MODE);
        commandsBidiMap.put(TelemetryRadioConstants.SET_PTT, TelemetryRadioConstants.GET_PTT);
        commandsBidiMap.put(TelemetryRadioConstants.SET_RIT, TelemetryRadioConstants.GET_RIT);
        commandsBidiMap.put(TelemetryRadioConstants.SET_RPTR_OFFSET, TelemetryRadioConstants.GET_RPTR_OFFSET);
        commandsBidiMap.put(TelemetryRadioConstants.SET_RPTR_SHIFT, TelemetryRadioConstants.GET_RPTR_SHIFT);
        commandsBidiMap.put(TelemetryRadioConstants.SET_SPLIT_VFO, TelemetryRadioConstants.GET_SPLIT_VFO);
        commandsBidiMap.put(TelemetryRadioConstants.SET_TRANCEIVE_MODE, TelemetryRadioConstants.GET_TRANCEIVE_MODE);
        commandsBidiMap.put(TelemetryRadioConstants.SET_TRANSMIT_FREQUENCY,
                TelemetryRadioConstants.GET_TRANSMIT_FREQUENCY);
        commandsBidiMap.put(TelemetryRadioConstants.SET_TRANSMIT_MODE, TelemetryRadioConstants.GET_TRANSMIT_MODE);
        commandsBidiMap.put(TelemetryRadioConstants.SET_TUNING_STEP, TelemetryRadioConstants.GET_TUNING_STEP);
        commandsBidiMap.put(TelemetryRadioConstants.SET_VFO, TelemetryRadioConstants.GET_VFO);

        commandsThatReturnIntegrer = new ArrayList<String>();
        commandsThatReturnIntegrer.add(TelemetryRadioConstants.GET_FREQUENCY);
    }

    public List<Message> checkPolling(Exchange exchange) {
        List<Message> messageList = new ArrayList<Message>();
        TelemetryObject incomingMessage = (TelemetryObject) exchange.getIn().getBody();
        createOriginalMessage(exchange, messageList);

        checkPollingMessageType(exchange, messageList, incomingMessage);
        return messageList;
    }

    private void checkPollingMessageType(Exchange exchange, List<Message> messageList, TelemetryObject incomingMessage) {

        if (exchange.getIn().getHeader(JMSConstants.HEADER_POLLING) != null) {
            if (exchange.getIn().getHeader(JMSConstants.HEADER_POLLING).equals(JMSConstants.POLL_SET_COMMAND)) {

                if (incomingMessage.getParameter(JMSConstants.GS_DEVICE_END_MESSAGE).getValue().equals("0")) {
                    createPollingMessage(exchange, messageList, incomingMessage);
                } else {
                    PollStoreSetValues.getRequiredParameterValueDataStore().remove(incomingMessage.getName());
                }
            }
            if (exchange.getIn().getHeader(JMSConstants.HEADER_POLLING).equals(JMSConstants.POLL_GET_COMMAND)) {
                if (incomingMessage.getParameter(JMSConstants.GS_DEVICE_END_MESSAGE).getValue().equals("0")) {
                    checkStatusUpdate(exchange, messageList, incomingMessage);
                } else {
                    PollStoreSetValues.getRequiredParameterValueDataStore().remove(
                            commandsBidiMap.getKey(incomingMessage.getName()));

                }
            }
        }
    }

    private void checkStatusUpdate(Exchange exchange, List<Message> messageList, TelemetryObject incomingMessage) {
        // No reason to check polling, if nothing to poll
        if (PollStoreSetValues.getRequiredParameterValueDataStore().size() == 0)
            return;

        String dataStoreKey;
        if (commandsBidiMap.getKey(incomingMessage.getName()) == null)
            return;

        dataStoreKey = (String) commandsBidiMap.getKey(incomingMessage.getName());
        boolean statusUpdateParametersCorrect = true;
        /*
         * Int starts from 1 because first parameter is always name and doesnt
         * contain value.
         */
        for (int i = 1; i < incomingMessage.getParams().size() - 1; i++) {

            try {
                if (commandsThatReturnIntegrer.contains(incomingMessage.getName())) {
                    statusUpdateParametersCorrect = checkIntegerParameterValue(exchange, messageList, incomingMessage,
                            dataStoreKey, statusUpdateParametersCorrect, i);
                } else {
                    statusUpdateParametersCorrect = checkBigDecimalParameterValue(exchange, messageList,
                            incomingMessage, dataStoreKey, statusUpdateParametersCorrect, i);
                }
            } catch (NumberFormatException e) {
                statusUpdateParametersCorrect = checkStringParameterValue(exchange, messageList, incomingMessage,
                        dataStoreKey, statusUpdateParametersCorrect, i);
            }
            if (statusUpdateParametersCorrect == false) {
                break;
            }
        }
        if (statusUpdateParametersCorrect == true) {
            PollStoreSetValues.getRequiredParameterValueDataStore().remove(dataStoreKey);
        }
    }

    private boolean checkIntegerParameterValue(Exchange exchange, List<Message> messageList,
            TelemetryObject incomingMessage, String dataStoreKey, boolean statusUpdateParametersCorrect, int i) {
        int currentParameterValue = Integer.parseInt(incomingMessage.getParams().get(i).getValue().toString());
        double requiredParameterValue = Double.parseDouble(PollStoreSetValues.getRequiredParameterValueDataStore()
                .get(dataStoreKey).getParameter(incomingMessage.getParams().get(i).getName()).toString());
        if (currentParameterValue != (int) requiredParameterValue) {
            createPollingMessage(exchange, messageList, incomingMessage);
            statusUpdateParametersCorrect = false;
        }
        return statusUpdateParametersCorrect;
    }

    private boolean checkStringParameterValue(Exchange exchange, List<Message> messageList,
            TelemetryObject incomingMessage, String dataStoreKey, boolean statusUpdateParametersCorrect, int i) {
        String currentParameterValue = incomingMessage.getParams().get(i).getValue().toString();
        String requiredParameterValue = PollStoreSetValues.getRequiredParameterValueDataStore().get(dataStoreKey)
                .getParameter(incomingMessage.getParams().get(i).getName()).toString();
        if (!currentParameterValue.equals(requiredParameterValue)) {
            createPollingMessage(exchange, messageList, incomingMessage);
            statusUpdateParametersCorrect = false;
        }
        return statusUpdateParametersCorrect;
    }

    private boolean checkBigDecimalParameterValue(Exchange exchange, List<Message> messageList,
            TelemetryObject incomingMessage, String dataStoreKey, boolean statusUpdateParametersCorrect, int i) {

        BigDecimal currentParameterValue = NumberUtils.createBigDecimal(
                incomingMessage.getParams().get(i).getValue().toString())
                .setScale(pollRoundScale, RoundingMode.HALF_UP);
        BigDecimal requiredParameterValue = NumberUtils.createBigDecimal(
                PollStoreSetValues.getRequiredParameterValueDataStore().get(dataStoreKey)
                        .getParameter(incomingMessage.getParams().get(i).getName()).toString()).setScale(
                pollRoundScale, RoundingMode.HALF_UP);
        if ((currentParameterValue.compareTo(requiredParameterValue)) != 0) {
            createPollingMessage(exchange, messageList, incomingMessage);
            statusUpdateParametersCorrect = false;
        }
        return statusUpdateParametersCorrect;
    }

    private void createOriginalMessage(Exchange exchange, List<Message> messageList) {
        DefaultMessage originalMessage = new DefaultMessage();
        originalMessage.setBody(exchange.getIn().getBody());
        originalMessage.setHeader(JMSConstants.HEADER_DEVICE, exchange.getIn().getHeader(JMSConstants.HEADER_DEVICE));
        originalMessage.setHeader(JMSConstants.HEADER_GROUNDSTATIONID,
                exchange.getIn().getHeader(JMSConstants.HEADER_GROUNDSTATIONID));
        originalMessage.setHeader(JMSConstants.HEADER_FORWARD, JMSConstants.DIRECT_SEND);
        messageList.add(originalMessage);
    }

    private void createPollingMessage(Exchange exchange, List<Message> messageList, TelemetryObject incomingMessage) {
        DefaultMessage pollingMessage = new DefaultMessage();

        if (exchange.getIn().getHeader(JMSConstants.HEADER_POLLING).equals(JMSConstants.POLL_GET_COMMAND)) {
            pollingMessage.setBody(new TelemetryCommand(incomingMessage.getName()));
        } else {
            pollingMessage.setBody(new TelemetryCommand((String) commandsBidiMap.get(incomingMessage.getName())));
        }
        pollingMessage.setHeader(JMSConstants.HEADER_DEVICE, exchange.getIn().getHeader(JMSConstants.HEADER_DEVICE));
        pollingMessage.setHeader(JMSConstants.HEADER_GROUNDSTATIONID,
                exchange.getIn().getHeader(JMSConstants.HEADER_GROUNDSTATIONID));
        pollingMessage.setHeader(JMSConstants.HEADER_POLLING, JMSConstants.POLL_GET_COMMAND);
        pollingMessage.setHeader(JMSConstants.HEADER_FORWARD, JMSConstants.DIRECT_CHOOSE_DEVICE);
        messageList.add(pollingMessage);
    }
}
