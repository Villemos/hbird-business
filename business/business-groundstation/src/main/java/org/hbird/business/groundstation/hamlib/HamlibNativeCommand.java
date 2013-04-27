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

import java.util.UUID;

import org.hbird.exchange.core.CommandBase;

/**
 * Class for native commands to execute on target component.
 * 
 * A native command is a command that is generated for the detailed control of
 * specific parts of the ground segment. Within the ground segment monitoring and
 * control system generic commands are distributed. These
 * are received by the drivers of the specific parts. Depending on the parts the
 * generic Command is translated into a sequence of NativeCommands performing the
 * specific control.
 * 
 * As an example the generic command 'Track' (request to track a satellite) will when
 * received by a Hamlib based Ground Station (Rotor and Radio) be translated into <li>The NativeCommand to the Rotor to
 * enter start position</li> <li>The NativeCommands to set the initial frequency</li> <li>A sequence of execution
 * timetagged NativeCommands to the Rotor for tracking.</li> <li>A sequence of execution timetagged NativeCommands to
 * the Radio to change the frequency according to the doppler.</li> <li>The NativeCommand to the Rotor to park the
 * antenna after the parse.</li>
 * 
 * For example antenna rotator command to rotate antenna would look something like:
 * <p>
 * <code>
 * NativeCommand rotateAntenna = new NativeCommand("MCS", "Rotator Identifier", "R 132 145");
 * </code>
 * </p>
 * 
 * Where String "R 132 145" is native command for rotator controlling software.
 * 
 * Hamlib documentation http://sourceforge.net/apps/mediawiki/hamlib/index.php?title=Documentation
 */
public class HamlibNativeCommand extends CommandBase {

    private static final long serialVersionUID = 8335410768161363858L;

    public static final String DESCRIPTION = "A Hamlib native command";

    public static final String STAGE_PRE_TRACKING = "PreTracking";
    public static final String STAGE_TRACKING = "Tracking";
    public static final String STAGE_POST_TRACKING = "PostTracking";

    protected String commandToExecute = "";

    protected String derivedfrom = "";

    protected String commandid = UUID.randomUUID().toString();

    protected String stage = "";

    public HamlibNativeCommand(String commandToExecute, long executionTime, String derivedFrom, String stage) {
        super("", HamlibNativeCommand.class.getSimpleName(), DESCRIPTION);
        this.commandToExecute = commandToExecute;
        this.executionTime = executionTime;
        this.derivedfrom = derivedFrom;
        this.stage = stage;
    }

    public String getCommandToExecute() {
        return commandToExecute;
    }

    public void setCommandToExecute(String commandToExecute) {
        this.commandToExecute = commandToExecute;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getDerivedfrom() {
        return derivedfrom;
    }

    public void setDerivedfrom(String derivedfrom) {
        this.derivedfrom = derivedfrom;
    }

    public String getCommandid() {
        return commandid;
    }

    public void setCommandid(String commandid) {
        this.commandid = commandid;
    }
}
