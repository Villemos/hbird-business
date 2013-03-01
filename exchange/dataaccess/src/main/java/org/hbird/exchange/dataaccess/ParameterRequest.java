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
package org.hbird.exchange.dataaccess;

import java.util.List;

import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.constants.StandardComponents;
import org.hbird.exchange.core.Parameter;

public class ParameterRequest extends DataRequest {

    public static final String DESCRIPTION = "A request for the latest value of a parameter";

    private static final long serialVersionUID = -210475353836504189L;

    public ParameterRequest(String parameter) {
        super(StandardComponents.ASSEMBLY, StandardComponents.PARAMETER_ARCHIVE, ParameterRequest.class.getSimpleName(), DESCRIPTION);
        setClass(Parameter.class.getSimpleName());
        addName(parameter);
    }

    public ParameterRequest(List<String> parameter) {
        super(StandardComponents.ASSEMBLY, StandardComponents.PARAMETER_ARCHIVE, ParameterRequest.class.getSimpleName(), DESCRIPTION);
        setClass(Parameter.class.getSimpleName());
        addName(parameter);
    }

    public ParameterRequest(String parameter, int rows) {
        this(parameter);
        setSort(StandardArguments.TIMESTAMP);
        setSortOrder("DESC");
        setRows(rows);
    }

    public ParameterRequest(String parameter, Long from, Long to) {
        this(parameter);
        setFrom(from);
        setTo(to);
    }

    public ParameterRequest(String parameter, Long from, Long to, int rows) {
        this(parameter);
        setFrom(from);
        setTo(to);
        setRows(rows);
    }

    public ParameterRequest(List<String> parameters, int rows) {
        this(parameters);
        setRows(rows);
    }

    public ParameterRequest(List<String> parameters, Long from, Long to) {
        this(parameters);
        setFrom(from);
        setTo(to);
    }

    public ParameterRequest(List<String> parameters, Long from, Long to, int rows) {
        this(parameters);
        setFrom(from);
        setTo(to);
        setRows(rows);
    }
}
