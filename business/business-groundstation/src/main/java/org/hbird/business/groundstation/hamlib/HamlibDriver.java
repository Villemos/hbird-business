/**
 * Licensed to the Hummingbird Foundation (HF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The HF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * Licensed to the Hummingbird Foundation (HF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The HF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hbird.business.groundstation.hamlib;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.component.netty.NettyComponent;
import org.apache.camel.component.netty.NettyConfiguration;
import org.hbird.business.api.ApiFactory;
import org.hbird.business.api.ICatalogue;
import org.hbird.business.api.IOrbitPrediction;
import org.hbird.business.core.InMemoryScheduler;
import org.hbird.business.core.SoftwareComponentDriver;
import org.hbird.business.groundstation.base.DriverContext;
import org.hbird.business.groundstation.base.OnChange;
import org.hbird.business.groundstation.base.TrackingSupport;
import org.hbird.business.groundstation.base.Verifier;
import org.hbird.business.groundstation.configuration.GroundStationDriverConfiguration;
import org.hbird.business.groundstation.device.response.ResponseHandler;
import org.hbird.business.groundstation.device.response.ResponseHandlersMap;
import org.hbird.business.groundstation.hamlib.protocol.HamlibCommandEncoder;
import org.hbird.business.groundstation.hamlib.protocol.HamlibErrorLogger;
import org.hbird.business.groundstation.hamlib.protocol.HamlibProtocolConstants;
import org.hbird.business.groundstation.hamlib.protocol.HamlibResponseBufferer;
import org.hbird.exchange.configurator.StandardEndpoints;
import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.groundstation.IPointingDataOptimizer;
import org.hbird.exchange.interfaces.IPart;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract base class for device drivers.
 * 
 * @author Gert Villemos
 * 
 */
public abstract class HamlibDriver<C extends GroundStationDriverConfiguration> extends SoftwareComponentDriver {

    private static final Logger LOG = LoggerFactory.getLogger(HamlibDriver.class);

    protected Verifier verifier = new Verifier();

    protected InMemoryScheduler inMemoryScheduler = new InMemoryScheduler(getContext().createProducerTemplate());

    protected boolean failOnOldCommand = true;

    protected DriverContext<C, String, String> driverContext;

    @Override
    public void doConfigure() {

        if (part == null) {
            throw new IllegalStateException("No IPart definition available");
        }

        CamelContext camelContext = getContext();

        driverContext = createDriverContext(camelContext, part);

        String name = part.getName();
        C config = driverContext.getConfiguration();

        NettyComponent nettyComponent = camelContext.getComponent("netty", NettyComponent.class);
        nettyComponent.setConfiguration(createNettyConfiguration(config));

        LOG.info("Configuring '{}'", name);

        inMemoryScheduler.setInjectUrl("direct://inject." + name);

        // @formatter:off
        from("seda:"+ name + ".toHamlib")
//            .to("log:" + name + "-toHamlib?level=DEBUG&showAll=true")
            .doTry()
                .inOut("netty:tcp://" + config.getAddress())
                .to("seda:" + name + ".fromHamlib")
            .doCatch(Exception.class)
                .to("log:" + name + "-hamlibError?level=ERROR&showAll=true&multiline=true")
            .end();

        ResponseHandlersMap<C, String, String> handlers = getResponseHandlerMap(driverContext);
        
        from("seda:" + name + ".fromHamlib")
//            .to("log:" + name + ".fromHamlib?level=DEBUG&showAll=true")
            .bean(handlers)
            .split(body())
            .inOnly("direct:parameters" + name);

            
         /*
         * Setup route for the PART to receive the commands. The part will generate the
         * NativeCommands. These will be routed to the INTERNAL schedule.
         *
         * The NativeCommands are at their execution time read through the EXECUTION below.
         */

        ICatalogue catalogue = ApiFactory.getCatalogueApi(part.getID());
        IOrbitPrediction prediction = ApiFactory.getOrbitPredictionApi(part.getID());
        IPointingDataOptimizer<C> optimizer = null; // TODO - 27.04.2013, kimmell - create optimizer from class name 
        TrackingSupport<C> tracker = createTrackingSupport(config, catalogue, prediction, optimizer);    
        
         from(StandardEndpoints.COMMANDS + "?selector=name='Track'")
             .log("Received 'Track' command from '" + simple("${body.issuedBy}").getText() + "'. Will generate Hamlib commands for '" + name + "'")
             .split()
                 .method(tracker, "track")
                 .setHeader("stage", simple("${body.stage}"))
                 .setHeader("derivedfrom", simple("${body.derivedfrom}"))
                 .setHeader("commandid", simple("${body.commandid}"))
                 .setHeader("executiontime", simple("${body.executionTime}"))
             .bean(verifier, "register")
             .bean(inMemoryScheduler, "add")
             .routeId(name + ": Command injection");
         
         
        
//        /*
//         * Setup route for the PART to receive the verifications. The part will verify the respond and if
//         * it fails, or the verification stage is complete, issue a State.
//         */
//        from("direct:hbird" + name + ".verification")
//            .bean(verifier, "verify")
//            .choice()
//            .when(simple("${in.body} == null"))
//                .stop()
//            .otherwise()
//                .to(StandardEndpoints.MONITORING)
//            .end()
//            .routeId(name + ": Verification");
        
        /*
         * Setup the EXECUTION route. The route reads the scheduled NativeCommands from the INTERNAL topic, send each to
         * Hamlib through TCPIP and route the response back to the INTERNAL topic.
         */
        
        /*
         * Read from the internal queue. The commands on the queue will be scheduled, i.e. be send to the
         * driver when they need to be executed.
         *
         * The execution path should be as short and efficient as possible. The verification is not done in
         * this thread route but in a separate route.
         */
         from(inMemoryScheduler.getInjectUrl())
             .setHeader("stage", simple("${body.stage}"))
             .setHeader("derivedfrom", simple("${body.derivedfrom}"))
             .setHeader("commandid", simple("${body.commandid}"))
             .setHeader("executiontime", simple("${body.executionTime}"))
             .bean(new NativeCommandExtractor())
             .log(LoggingLevel.INFO, "Sending command '" + simple("${body}").getText() + "' to " + name)
             .to("seda:"+ name + ".toHamlib");
             
//         .inOut("netty:tcp://" + getAddress())
//         .removeHeader("AMQ_SCHEDULED_DELAY")
//         .to("direct:hbird" + name + ".verification")
//         .routeId(name + ": Command execution");
        
        // /** Create route for cleanup of stages. */
        // from("timer://cleanup?period=10000")
        // .split().method(verifier, "cleanup")
        // .to(StandardEndpoints.MONITORING)
        // .routeId(name + ": Cleanup");
        //
         
         from("direct:parameters" + name)
             .bean(new OnChange())
             .choice()
                 .when(header("hbird.haschanged").isEqualTo(false))
                     .log(LoggingLevel.INFO, "Not sending parameter. Not updated.")
                 .otherwise()
                     .setHeader(StandardArguments.NAME, simple("${in.body.name}"))
                     .setHeader(StandardArguments.ISSUED_BY, simple("${in.body.issuedBy}"))
//                     .setHeader(StandardArguments.TYPE, simple("${in.body.type}"))
                     .setHeader(StandardArguments.CLASS, simple("${in.body.class.simpleName}"))
                     .process(new Processor() {
                        
                        @Override
                        public void process(Exchange exchange) throws Exception {
                            LOG.debug("PARAMETER: {}", exchange.getIn().getBody(Parameter.class).prettyPrint());
                        }
                    })
                     .to(StandardEndpoints.MONITORING);

        // @formatter:on
    }

    /**
     * @param config
     * @return
     */
    NettyConfiguration createNettyConfiguration(GroundStationDriverConfiguration config) {
        NettyConfiguration nettyConfig = new NettyConfiguration();
        nettyConfig.setHost(config.getDeviceHost());
        nettyConfig.setPort(config.getDevicePort());
        nettyConfig.setSync(true);
        nettyConfig.setAllowDefaultCodec(false);
        nettyConfig.setEncoders(createHamlibProtocolEncoders());
        nettyConfig.setDecoders(createHamlibProtocolDecoders());
        return nettyConfig;
    }

    /**
     * @return
     */
    List<ChannelHandler> createHamlibProtocolEncoders() {
        List<ChannelHandler> encoders = new ArrayList<ChannelHandler>(1);
        encoders.add(new HamlibCommandEncoder());
        return encoders;
    }

    /**
     * @return
     */
    List<ChannelHandler> createHamlibProtocolDecoders() {
        List<ChannelHandler> decoders = new ArrayList<ChannelHandler>(4);
        decoders.add(new DelimiterBasedFrameDecoder(HamlibProtocolConstants.MAX_FRAME_LENGTH, false, Delimiters.lineDelimiter()));
        decoders.add(new StringDecoder(Charset.forName(HamlibProtocolConstants.STRING_ENCODING)));
        decoders.add(new HamlibResponseBufferer());
        decoders.add(new HamlibErrorLogger());
        return decoders;
    }

    public boolean isFailOnOldCommand() {
        return failOnOldCommand;
    }

    public void setFailOnOldCommand(boolean failOnOldCommand) {
        this.failOnOldCommand = failOnOldCommand;
    }

    protected ResponseHandlersMap<C, String, String> getResponseHandlerMap(DriverContext<C, String, String> driverContext) {
        ResponseHandlersMap<C, String, String> handlerMap = new ResponseHandlersMap<C, String, String>(driverContext);
        for (ResponseHandler<C, String, String> handler : createResponseHandlers()) {
            handlerMap.addHandler(handler);
        }
        return handlerMap;
    }

    protected abstract DriverContext<C, String, String> createDriverContext(CamelContext camelContext, IPart part);

    protected abstract List<ResponseHandler<C, String, String>> createResponseHandlers();

    protected abstract TrackingSupport<C> createTrackingSupport(C config, ICatalogue catalogue,
            IOrbitPrediction prediction, IPointingDataOptimizer<C> optimizer);

}
