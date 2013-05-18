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
package org.hbird.business.core;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.core.CommandBase;
import org.hbird.exchange.interfaces.IApplicableTo;
import org.hbird.exchange.interfaces.IDerivedFrom;
import org.hbird.exchange.interfaces.IEntity;
import org.hbird.exchange.interfaces.IEntityInstance;
import org.hbird.exchange.interfaces.IGroundStationSpecific;
import org.hbird.exchange.interfaces.ISatelliteSpecific;
import org.hbird.exchange.interfaces.IScheduled;
import org.hbird.exchange.interfaces.ITransferable;

/**
 */
public class AddHeaders implements Processor {

    // XXX - 17.05.2013, kimmell - this constant has to be defined in some other place actually; no time to look at the
    // moment
    public static final String AMQ_SCHEDULED_DELAY = "AMQ_SCHEDULED_DELAY";

    // XXX - 17.05.2013, kimmell - this constant has to be defined in some other place actually; no time to look at the
    // moment
    public static final String DELIVERYTIME = "deliverytime";

    /**
     * @see org.apache.camel.Processor#process(org.apache.camel.Exchange)
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        Message in = exchange.getIn();
        Object body = in.getBody();

        if (body == null) {
            // body is null; no point to continue; drop the exchange
            exchange.setProperty(Exchange.ROUTE_STOP, Boolean.TRUE);
            return;
        }

        Message out = exchange.getOut();
        out.copyFrom(in);

        // set class value
        out.setHeader(StandardArguments.CLASS, body.getClass().getSimpleName());

        if (body instanceof IEntity) {
            IEntity entity = (IEntity) body;
            out.setHeader(StandardArguments.ENTITY_ID, entity.getID());
            out.setHeader(StandardArguments.NAME, entity.getName());
            out.setHeader(StandardArguments.ISSUED_BY, entity.getIssuedBy());
        }

        if (body instanceof IEntityInstance) {
            IEntityInstance instance = (IEntityInstance) body;
            out.setHeader(StandardArguments.ENTITY_INSTANCE_ID, instance.getInstanceID());
            out.setHeader(StandardArguments.TIMESTAMP, instance.getTimestamp());
        }

        if (body instanceof IApplicableTo) {
            IApplicableTo applicableTo = (IApplicableTo) body;
            out.setHeader(StandardArguments.APPLICABLE_TO, applicableTo.getApplicableTo());
        }

        if (body instanceof CommandBase) {
            CommandBase command = (CommandBase) body;
            out.setHeader(StandardArguments.DESTINATION, command.getDestination());
        }

        if (body instanceof IDerivedFrom) {
            IDerivedFrom derivedFrom = (IDerivedFrom) body;
            out.setHeader(StandardArguments.DERIVED_FROM, derivedFrom.getDerivedFromId());
        }

        if (body instanceof IGroundStationSpecific) {
            IGroundStationSpecific groundStationSpecific = (IGroundStationSpecific) body;
            out.setHeader(StandardArguments.GROUND_STATION_ID, groundStationSpecific.getGroundStationID());
        }

        if (body instanceof ISatelliteSpecific) {
            ISatelliteSpecific satelliteSpecific = (ISatelliteSpecific) body;
            out.setHeader(StandardArguments.SATELLITE_ID, satelliteSpecific.getSatelliteID());
        }

        if (body instanceof ITransferable) {
            ITransferable tranferable = (ITransferable) body;
            // current moment
            long now = System.currentTimeMillis();
            // transfer time
            long transferTime = tranferable.getTransferTime();
            // calculate delay for AMQ
            long delay = calculateDelay(transferTime, now);
            out.setHeader(AMQ_SCHEDULED_DELAY, delay);
            out.setHeader(DELIVERYTIME, transferTime);
        }
    }

    long calculateDelay(long transferTime, long now) {
        return now >= transferTime ? IScheduled.IMMEDIATE : transferTime - now;
    }
}
