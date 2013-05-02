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
package org.hbird.exchange.core.typeconverters;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Converter;
import org.apache.log4j.Logger;
import org.hbird.core.spacesystemmodel.tmtc.CommandGroup;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.Parameter;



/**
 * Type converter to convert from the Transport Tier representation of parameters
 * to the business tier representation of parameters.
 * 
 * @author Johaness Klug
 */
@Converter
public class TransportToBusinessTypeConverter {

	/** Da' logger. */
	private static org.apache.log4j.Logger LOG = Logger.getLogger(TransportToBusinessTypeConverter.class);

	
	/** TODO */
//	public static CommandGroup convertToCommand(Command bCommand) {
//		CommandGroup tCommand = new TmTcGroup();
//		
//		return tCommand; 
//	}
	
	/**
	 * Method to convert a Transport Tier parameter group to a List of Business Tier parameters.
	 * 
	 * @param pg The Transport Tier parameter group.
	 * @return A list of Business Tier parameters
	 */
	public static List<Parameter> convertToParameterList(org.hbird.core.spacesystemmodel.tmtc.ParameterGroup pg) {
		List<Parameter> parameters = new ArrayList<Parameter>();

		int numberOfConvertedParameters = 0;

		for (org.hbird.core.spacesystemmodel.tmtc.Parameter<Integer> p : pg.getIntegerParameters().values()) {
			parameters.add(convertToParameter(p, p.getValue()));
			numberOfConvertedParameters++;
		}

		for (org.hbird.core.spacesystemmodel.tmtc.Parameter<Long> p : pg.getLongParameters().values()) {
			parameters.add(convertToParameter(p, p.getValue()));
			numberOfConvertedParameters++;
		}

		for (org.hbird.core.spacesystemmodel.tmtc.Parameter<Float> p : pg.getFloatParameters().values()) {
			parameters.add(convertToParameter(p, p.getValue()));
			numberOfConvertedParameters++;
		}

		for (org.hbird.core.spacesystemmodel.tmtc.Parameter<Double> p : pg.getDoubleParameters().values()) {
			parameters.add(convertToParameter(p, p.getValue()));
			numberOfConvertedParameters++;
		}

		for (org.hbird.core.spacesystemmodel.tmtc.Parameter<BigDecimal> p : pg.getBigDecimalParameters().values()) {
			parameters.add(convertToParameter(p, p.getValue()));
			numberOfConvertedParameters++;
		}

		int numberOfInputParameters = pg.getAllParameters().size();

		if (numberOfConvertedParameters != numberOfInputParameters) {
			LOG.warn("ParameterGroup "
					+ pg.getName()
					+ " contains "
					+ numberOfInputParameters
					+ ", but only "
					+ numberOfConvertedParameters
					+ " could be converted. Most likely cause: there were parameters in the input parameter group which could not be cast to Number parameters, for instance raw parameters that are plain byte arrays.");
		}

		return parameters;

	}

	/**
	 * Helper method to convert a Transport Tier parameter to a Business Tier parameter.
	 * 
	 * @param p The transport tier parameter definition
	 * @param v The value of the transport tier parameter
	 * @return The business tier parameter
	 */
	protected static Parameter convertToParameter(org.hbird.core.spacesystemmodel.tmtc.Parameter<?> p, Number v) {
//		Parameter newBusinessParameter = new Parameter("TransportTier", p.getQualifiedName(), p.getShortDescription(), "");
//		newBusinessParameter.setTimestamp(p.getReceivedTime());
//		newBusinessParameter.setValue(v);
//		return newBusinessParameter;
		
		return null;
	}

}
