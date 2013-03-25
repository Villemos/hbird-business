package eu.estcube.gsconnector;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;


import org.apache.camel.CamelExchangeException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import eu.estcube.domain.JMSConstants;
import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRadioConstants;
import eu.estcube.domain.TelemetryRotatorConstants;

public class Connector extends RouteBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(Connector.class);

    @Autowired
    private SplitterRadioStatus radioStatusSplitter;

    @Autowired
    private SplitterRotatorStatus rotatorStatusSplitter;

    @Autowired
    private PollStoreSetValues storeSetValues;

    @Value("${rotctldAddress}")
    private String rotAddress;

    @Value("${rigctldAddress}")
    private String rigAddress;

    @Value("${gsName}")
    private String gsName;

    @Value("${timerFireInterval}")
    private String timerFireInterval;

    @Value("${pollDelayInterval}")
    private int pollDelayInterval;

    @Value("${version}")
    private String connectorVersion;

    private boolean rotOnline = true;
    private boolean rigOnline = true;

    @Override
    public void configure() {
        String nettyRigCtld = "netty:tcp://" + rigAddress + "?sync=true&decoders=#" + "radioDecoderNewLine"
            + ",#" + "radioDecoderToString" + ",#" + "radioDecoderHamlib" + ",#"
            + JMSConstants.GS_RADIO_PROTOCOL_DECODER + "&encoders=#" + JMSConstants.GS_ENCODE_STR_TO_BYTE + ",#"
            + JMSConstants.GS_RADIO_PROTOCOL_ENCODER;

        String nettyRotCtld = "netty:tcp://" + rotAddress + "?sync=true&decoders=#" + "rotatorDecoderNewLine"
            + ",#" + "rotatorDecoderToString" + ",#" + "rotatorDecoderHamlib" + ",#"
            + JMSConstants.GS_ROTATOR_PROTOCOL_DECODER + "&encoders=#" + JMSConstants.GS_ENCODE_STR_TO_BYTE + ",#"
            + JMSConstants.GS_ROTATOR_PROTOCOL_ENCODER;

// Real route
        confUniversal();
        confRig(nettyRigCtld);
        confRotator(nettyRotCtld);

        // Hamlib info
        // sudo rpc.rotd -m 1 -vvvv
        // sudo rotctld -m 1 -vvvvv //4533
        // sudo rpc.rigd -m 1 -vvvv
        // sudo rigctld -m 1901 -vvvvv //4532

// Test route

        from("stream:in")
            .process(new Processor() {
                public void process(Exchange exchange) {

                    // set radio frequency
                    exchange.getIn().setHeader(JMSConstants.HEADER_DEVICE, JMSConstants.GS_RIG_CTLD);
                    TelemetryCommand command = new TelemetryCommand(TelemetryRadioConstants.SET_FREQUENCY);
                    command.addParameter(new TelemetryParameter("Frequency", 143.456));

                    // get radio frequency
// TelemetryCommand command = new
// TelemetryCommand(TelemetryRadioConstants.GET_FREQUENCY);

                    // set rotator azimuth and elevation
// exchange.getIn().setHeader(JMSConstants.HEADER_DEVICE,
// JMSConstants.GS_ROT_CTLD);
// TelemetryCommand command = new
// TelemetryCommand(TelemetryRotatorConstants.SET_POSITION);
// command.addParameter(new TelemetryParameter("Azimuth", 125));
// command.addParameter(new TelemetryParameter("Elevation", 25));

                    exchange.getIn().setBody(command);
                }
            })
            .to(JMSConstants.DIRECT_CHOOSE_DEVICE);

        from(JMSConstants.AMQ_VERSIONS_REQUEST).process(new Processor() {
            public void process(Exchange ex) {
                ex.getOut().setHeader(JMSConstants.HEADER_GROUNDSTATIONID, gsName);
                ex.getOut().setHeader(JMSConstants.HEADER_COMPONENT, JMSConstants.COMPONENT_CONNECTOR);
                ex.getOut().setBody(connectorVersion, String.class);
            }
        }).to(JMSConstants.AMQ_VERSIONS_RECEIVE);

        from("timer://sendVersion?repeatCount=1").process(new Processor() {
            public void process(Exchange ex) {
                ex.getOut().setHeader(JMSConstants.HEADER_GROUNDSTATIONID, gsName);
                ex.getOut().setHeader(JMSConstants.HEADER_COMPONENT, JMSConstants.COMPONENT_CONNECTOR);
                ex.getOut().setBody(connectorVersion, String.class);
            }
        }).to(JMSConstants.AMQ_VERSIONS_RECEIVE);
    }

    @SuppressWarnings("unchecked")
    private void confRotator(String nettyRotCtld) {
        /**
         * rotctld
         * Direction to the device - Route 9
         */
        from(JMSConstants.DIRECT_ROT_CTLD)
            .log("log:DebugRotDeviceBeforeResponse")
            .doTry()
            .to(nettyRotCtld)
            .process(new Processor() {
                public void process(Exchange exchange) {
                    String forwardRoute = JMSConstants.DIRECT_SEND;
                    exchange.getIn().setHeader(JMSConstants.HEADER_GROUNDSTATIONID, gsName);
                    exchange.getIn().setHeader(JMSConstants.HEADER_DEVICE, JMSConstants.GS_ROT_CTLD);
                    if (rotOnline) {
                        forwardRoute += "," + JMSConstants.DIRECT_ROT_STATUS;
                        rotOnline = false;
                    }
                    exchange.getIn().setHeader(JMSConstants.HEADER_FORWARD, forwardRoute);
                }
            })
            .doCatch(IOException.class, Exception.class, CamelExchangeException.class, ClosedChannelException.class)
            .process(new Processor() {
                public void process(Exchange exchange) {
                    LOG.warn("Rotator channel is closed!");
                    rotOnline = true;
                }
            })
            .end();

        // Periodical time fire rotctld - Route 10
        Processor processor = new Processor() {
            public void process(Exchange exchange) {
                exchange.getIn().setBody(new TelemetryCommand(TelemetryRotatorConstants.CAPABILITIES));
            }
        };

        from("timer://rotTimer?period=" + timerFireInterval)
            .process(processor)
            .log("log:DebugRotPeriodicalTimeFire")
            .doTry()
            .to(JMSConstants.DIRECT_ROT_CTLD)
            .recipientList(header(JMSConstants.HEADER_FORWARD))
            .endDoTry()
            .doCatch(IOException.class, Exception.class, CamelExchangeException.class, ClosedChannelException.class)
            .log("Timerfire failed, probably channel closed")
            .end();

        // STATUS ROUTES - Route 11
        from(JMSConstants.DIRECT_ROT_STATUS)
            .split().method(rotatorStatusSplitter, "splitMessage")
            .to(JMSConstants.DIRECT_ROT_CTLD, JMSConstants.DIRECT_SEND);
    }

    @SuppressWarnings("unchecked")
    private void confRig(String nettyRigCtld) {
        /**
        * rigctld
        * Direction to the device - Route 6
        */
        Processor rigProcessor = new Processor() {
            public void process(Exchange exchange) {
                String forwardRoute = JMSConstants.DIRECT_SEND;
                exchange.getIn().setHeader(JMSConstants.HEADER_GROUNDSTATIONID, gsName);
                exchange.getIn().setHeader(JMSConstants.HEADER_DEVICE, JMSConstants.GS_RIG_CTLD);
                if (rigOnline) {
                    forwardRoute += "," + JMSConstants.DIRECT_RIG_STATUS;
                    rigOnline = false;
                }
                exchange.getIn().setHeader(JMSConstants.HEADER_FORWARD, forwardRoute);
            }
        };
        Processor channelClosedProcessor = new Processor() {
            public void process(Exchange exchange) {
                LOG.warn("Radio channel is closed!");
                rigOnline = true;
            }
        };
        from(JMSConstants.DIRECT_RIG_CTLD)
            .log("log:DebugRigDeviceBeforeResponse")
            .doTry()
            .to(nettyRigCtld)
            .process(rigProcessor)
            .doCatch(IOException.class, Exception.class, CamelExchangeException.class, ClosedChannelException.class)
            .process(channelClosedProcessor)
            .end();

        // Periodical time fire rigctld - Route 7
        Processor capabilitiesProcessor = new Processor() {
            public void process(Exchange exchange) {
                exchange.getIn().setBody(new TelemetryCommand(TelemetryRadioConstants.CAPABILITIES));
            }
        };
        from("timer://rigTimer?period=" + timerFireInterval)
            .process(capabilitiesProcessor)
            .log("log:DebugRigPeriodicalTimeFire")
            .doTry()
            .to(JMSConstants.DIRECT_RIG_CTLD)
            .recipientList(header(JMSConstants.HEADER_FORWARD))
            .endDoTry()
            .doCatch(IOException.class, Exception.class, CamelExchangeException.class, ClosedChannelException.class)
            .log("Timerfire failed, probably channel closed")
            .end();

        // STATUS ROUTES - Route 8
        from(JMSConstants.DIRECT_RIG_STATUS)
            .split().method(radioStatusSplitter, "splitMessage")
            .to(JMSConstants.DIRECT_RIG_CTLD, JMSConstants.DIRECT_SEND);
    }

    private void confUniversal() {
        /**
         * Incoming messages - Route 1
         */
        from(JMSConstants.AMQ_GS_RECEIVE)
            .log("log:RecievedMessage")
            .choice()
            .when(header(JMSConstants.HEADER_GROUNDSTATIONID).isEqualTo(gsName))
            .to(JMSConstants.DIRECT_CHOOSE_DEVICE)
            .when(header(JMSConstants.HEADER_GROUNDSTATIONID).isEqualTo(JMSConstants.GS_ALL_NAMES))
            .to(JMSConstants.DIRECT_CHOOSE_DEVICE)
            .otherwise()
            .log("log:WrongStation");

        from(JMSConstants.DIRECT_CHOOSE_DEVICE)
            .bean(storeSetValues)
            .choice()
            .when(header(JMSConstants.HEADER_DEVICE).isEqualTo(JMSConstants.GS_RIG_CTLD))
            .to(JMSConstants.DIRECT_RIG_CTLD, JMSConstants.DIRECT_POLL_COMMAND)
            .when(header(JMSConstants.HEADER_DEVICE).isEqualTo(JMSConstants.GS_ROT_CTLD))
            .to(JMSConstants.DIRECT_ROT_CTLD, JMSConstants.DIRECT_POLL_COMMAND)
            .when(header(JMSConstants.HEADER_DEVICE).isEqualTo("status"))
            .to(JMSConstants.DIRECT_STATUS)
            .otherwise()
            .log("log:InvalidDevice");

        // monitors status updates - Route 3
        from(JMSConstants.DIRECT_POLL_COMMAND)
            .split().method(JMSConstants.CLASS_POLL_COMMANDS, "checkPolling")
            .delay(pollDelayInterval)
            .recipientList(header(JMSConstants.HEADER_FORWARD));

        // Outgoing messages - Route 4
        from(JMSConstants.DIRECT_SEND)
            .log("log:SendMessage")
            .to(JMSConstants.AMQ_GS_SEND);

        // status - Route 5
        from(JMSConstants.DIRECT_STATUS)
            .multicast()
            .parallelProcessing()
            .to(JMSConstants.DIRECT_RIG_STATUS)
            .to(JMSConstants.DIRECT_ROT_STATUS);
    }

    public static void main(String[] args) {
        LOG.info("Starting Connector");
        try {
            new Main().run(args);
        } catch (Exception e) {
            LOG.error("Connector main method failed: " + e.toString());
        }
    }
}
