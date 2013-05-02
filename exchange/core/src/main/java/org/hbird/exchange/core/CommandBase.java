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
package org.hbird.exchange.core;

import org.hbird.exchange.interfaces.IScheduled;
import org.hbird.exchange.interfaces.ITransferable;

/**
 *
 */
public class CommandBase extends EntityInstance implements IScheduled, ITransferable {

    private static final long serialVersionUID = -5289428765905258384L;

    /** The name of the component to which this command is destined. */
    protected String destination = null;

    /**
     * The time at which this command should be transfered to its destination. A value of
     * 0 indicates immediate.
     */
    protected long transferTime = IScheduled.IMMEDIATE;

    /**
     * The time at which the command should be executed at the destination. A value of 0 indicates
     * immediate.
     */
    protected long executionTime = IScheduled.IMMEDIATE;

    public CommandBase(String ID, String name) {
        super(ID, name);
    }

    /**
     * Getter of the command execution time.
     * 
     * @return The time (ms from 1970) at which the command should execute.
     */
    @Override
    public long getExecutionTime() {
        return executionTime;
    }

    /**
     * @return the destination
     */
    @Override
    public String getDestination() {
        return destination;
    }

    /**
     * @param destination the destination to set
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * @return the transferTime
     */
    @Override
    public long getTransferTime() {
        return transferTime;
    }

    /**
     * @param transferTime the transferTime to set
     */
    public void setTransferTime(long transferTime) {
        this.transferTime = transferTime;
    }

    /**
     * @param executionTime the executionTime to set
     */
    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }
}
