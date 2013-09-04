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
package org.hbird.business.api.impl;

import org.apache.camel.CamelContext;
import org.apache.camel.Handler;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.spring.spi.ApplicationContextRegistry;
import org.hbird.business.api.IPublisher;
import org.hbird.business.core.HbirdRouteBuilder;
import org.hbird.exchange.core.EntityInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 */
public class AmqPublisher extends HbirdRouteBuilder implements IPublisher, ApplicationContextAware {

    public static final String INJECT_ROUTE = "seda://inject";

    private static final Logger LOG = LoggerFactory.getLogger(AmqPublisher.class);

    private ApplicationContext applicationContext;
    private ProducerTemplate producerTemplate;
    private final IPublisher delegate;

    public AmqPublisher(IPublisher delegate) {
        this.delegate = delegate;
    }

    /**
     * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * @see org.hbird.business.api.IPublisher#publish(org.hbird.exchange.core.EntityInstance)
     */
    @Handler
    @Override
    public <T extends EntityInstance> T publish(T object) throws Exception {
        if (object == null) {
            // E.g. limit checker can send null
            return null;
        }
        T saved = delegate.publish(object);
        producerTemplate.sendBody(INJECT_ROUTE, saved);
        return saved;
    }

    /**
     * @see org.apache.camel.builder.RouteBuilder#configure()
     */
    @Override
    public void configure() throws Exception {
        RouteDefinition inject = from(INJECT_ROUTE);
        addInjectionRoute(inject);
    }

    public void start() throws Exception {
        CamelContext camelContext = new DefaultCamelContext(new ApplicationContextRegistry(applicationContext));
        LOG.info("Created new CamelContext '{}' using Spring ApplicationContext; bean registry should be available", camelContext.getName());
        camelContext.addRoutes(this);
        producerTemplate = camelContext.createProducerTemplate();
        camelContext.start();
        LOG.info("CamelContext '{}' started", camelContext.getName());
    }
}
