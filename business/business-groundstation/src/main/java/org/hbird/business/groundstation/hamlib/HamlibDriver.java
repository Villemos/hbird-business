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
import org.apache.camel.LoggingLevel;
import org.apache.camel.component.netty.NettyComponent;
import org.apache.camel.component.netty.NettyConfiguration;
import org.apache.camel.model.ProcessorDefinition;
import org.hbird.business.api.ApiFactory;
import org.hbird.business.api.IPointingData;
import org.hbird.business.api.deprecated.IDataAccess;
import org.hbird.business.core.InMemoryScheduler;
import org.hbird.business.core.SoftwareComponentDriver;
import org.hbird.business.groundstation.base.DriverContext;
import org.hbird.business.groundstation.base.GroundStationCommandFilter;
import org.hbird.business.groundstation.base.OnChange;
import org.hbird.business.groundstation.base.TrackingSupport;
import org.hbird.business.groundstation.base.Verifier;
import org.hbird.business.groundstation.configuration.GroundStationDriverConfiguration;
import org.hbird.business.groundstation.device.response.ResponseHandler;
import org.hbird.business.groundstation.device.response.ResponseHandlersMap;
import org.hbird.business.groundstation.hamlib.protocol.HamlibCommandEncoder;
import org.hbird.business.groundstation.hamlib.protocol.HamlibErrorLogger;
import org.hbird.business.groundstation.hamlib.protocol.HamlibLineDecoder;
import org.hbird.business.groundstation.hamlib.protocol.HamlibProtocolConstants;
import org.hbird.business.groundstation.hamlib.protocol.HamlibResponseBufferer;
import org.hbird.exchange.configurator.StandardEndpoints;
import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.groundstation.IPointingDataOptimizer;
import org.hbird.exchange.interfaces.IStartableEntity;
import org.jboss.netty.channel.ChannelHandler;
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

    // TODO - 28.04.2013, kimmell - replace with java.util.concurrent.ScheduledExecutorService
    protected InMemoryScheduler inMemoryScheduler;

    protected DriverContext<C, String, String> driverContext;

    @Override
    public void doConfigure() {

        if (entity == null) {
            throw new IllegalStateException("No IPart definition available");
        }

        CamelContext camelContext = entity.getContext();

        driverContext = createDriverContext(camelContext, entity);

        String name = entity.getName();
        String entityId = entity.getID();
        C config = driverContext.getConfiguration();

        NettyComponent nettyComponent = camelContext.getComponent("netty", NettyComponent.class);
        nettyComponent.setConfiguration(createNettyConfiguration(config));

        LOG.info("Configuring '{}'", name);

        inMemoryScheduler = new InMemoryScheduler(camelContext.createProducerTemplate());
        inMemoryScheduler.setInjectUrl(asRoute("direct://inject-%s", name));

        // @formatter:off
        from(asRoute("seda:toHamlib-%s", name))
            .startupOrder(4)

//            .to("log:" + name + "-toHamlib?level=DEBUG&showAll=true")
            .doTry()
                .inOut(asRoute("netty:tcp://%s:%s", config.getDeviceHost(), config.getDevicePort()))
                .to(asRoute("seda:fromHamlib-%s", name))
            .doCatch(Exception.class)
                .to(asRoute("log:hamlibError-%s?level=ERROR&showAll=true&multiline=true", name))
            .end();

        ResponseHandlersMap<C, String, String> handlers = getResponseHandlerMap(driverContext);
        
        from(asRoute("seda:fromHamlib-%s", name))
            .startupOrder(3)
//            .to("log:" + name + ".fromHamlib?level=DEBUG&showAll=true")
            .bean(handlers)
            .split(body())
            .inOnly(asRoute("direct:parameters-%s", name));

            
        /*
         * Setup route for the PART to receive the commands. The part will generate the
         * NativeCommands. These will be routed to the INTERNAL schedule.
         *
         * The NativeCommands are at their execution time read through the EXECUTION below.
         */

        IDataAccess dao = ApiFactory.getDataAccessApi(entityId, camelContext);
        IPointingData calulator = ApiFactory.getOrbitDataApi(entityId, camelContext);
        IPointingDataOptimizer<C> optimizer = createOptimizer(config.getPointingDataOptimzerClassName()); // can be null
        TrackingSupport<C> tracker = createTrackingSupport(config, dao, calulator, optimizer);    
        GroundStationCommandFilter commandFilter = new GroundStationCommandFilter(config);
        SetHamlibNativeCommandHeaders setHeaders = new SetHamlibNativeCommandHeaders();
        
         from(StandardEndpoints.COMMANDS + "?selector=name='Track'")
             .startupOrder(6)
             .filter().method(commandFilter, "acceptTrack")
             .log(asRoute("Received 'Track' command from '%s'. Will generate Hamlib commands for '%s'", simple("${body.issuedBy}").getText(), name))
             .split()
                 .method(tracker, "track")
                 .process(setHeaders)
             .bean(verifier, "register")
             .bean(inMemoryScheduler, "add")
             .routeId(asRoute("%s: Command injection", name));
         
         
        
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
             .startupOrder(5)
             .process(setHeaders)
             .bean(new NativeCommandExtractor())
             .log(LoggingLevel.INFO, asRoute("Sending command '%s' to %s", simple("${body}").getText(), name))
             .to(asRoute("seda:toHamlib-%s", name));
             
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

         ProcessorDefinition<?> publish = from(asRoute("direct:publish-%s", name))
                                             .startupOrder(1);
         addInjectionRoute(publish);
         
         from(asRoute("direct:parameters-%s", name))
             .startupOrder(2)
             .bean(new OnChange())
             .choice()
                 .when(header(StandardArguments.VALUE_HAS_CHANGED).isEqualTo(false))
                     .log(LoggingLevel.TRACE, asRoute("Value of '%s' not changed; skipping update",  simple("${in.body.name}").getText()))
                 .otherwise()
                     .to(asRoute("direct:publish-%s", name));
         
        // @formatter:on
    }

    @SuppressWarnings("unchecked")
    protected IPointingDataOptimizer<C> createOptimizer(String className) {
        if (className == null) {
            return null;
        }
        try {
            return (IPointingDataOptimizer<C>) Class.forName(className).newInstance();
        }
        catch (Exception e) {
            LOG.error("Failed to create instance of IPointingDataOptimizer for name {}", className, e);
            return null;
        }
    }

    protected static String asRoute(String routeTemplate, Object... params) {
        return String.format(routeTemplate, params);
    }

    /**
     * @param config
     * @return
     */
    NettyConfiguration createNettyConfiguration(C config) {
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
        decoders.add(new HamlibLineDecoder());
        decoders.add(new StringDecoder(Charset.forName(HamlibProtocolConstants.STRING_ENCODING)));
        decoders.add(new HamlibResponseBufferer());
        decoders.add(new HamlibErrorLogger());
        return decoders;
    }

    protected ResponseHandlersMap<C, String, String> getResponseHandlerMap(DriverContext<C, String, String> driverContext) {
        ResponseHandlersMap<C, String, String> handlerMap = new ResponseHandlersMap<C, String, String>(driverContext);
        for (ResponseHandler<C, String, String> handler : createResponseHandlers()) {
            handlerMap.addHandler(handler);
        }
        return handlerMap;
    }

    protected abstract DriverContext<C, String, String> createDriverContext(CamelContext camelContext, IStartableEntity part);

    protected abstract List<ResponseHandler<C, String, String>> createResponseHandlers();

    protected abstract TrackingSupport<C> createTrackingSupport(C config, IDataAccess dao, IPointingData calculator,
            IPointingDataOptimizer<C> optimizer);

}
