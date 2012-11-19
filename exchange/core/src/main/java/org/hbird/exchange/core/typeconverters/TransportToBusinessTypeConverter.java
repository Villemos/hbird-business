package org.hbird.exchange.core.typeconverters;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Converter;
import org.apache.log4j.Logger;
import org.hbird.core.commons.tmtc.ParameterGroup;
import org.hbird.exchange.core.Parameter;

@Converter
public class TransportToBusinessTypeConverter {

	private static org.apache.log4j.Logger LOG = Logger.getLogger(TransportToBusinessTypeConverter.class);

	public static List<Parameter> convertToParameterList(ParameterGroup pg) {
		List<Parameter> parameters = new ArrayList<Parameter>();

		int numberOfConvertedParameters = 0;

		for (org.hbird.core.commons.tmtc.Parameter<Integer> p : pg.getIntegerParameters().values()) {
			parameters.add(convertToParameter(p, p.getValue()));
			numberOfConvertedParameters++;
		}

		for (org.hbird.core.commons.tmtc.Parameter<Long> p : pg.getLongParameters().values()) {
			parameters.add(convertToParameter(p, p.getValue()));
			numberOfConvertedParameters++;
		}

		for (org.hbird.core.commons.tmtc.Parameter<Float> p : pg.getFloatParameters().values()) {
			parameters.add(convertToParameter(p, p.getValue()));
			numberOfConvertedParameters++;
		}

		for (org.hbird.core.commons.tmtc.Parameter<Double> p : pg.getDoubleParameters().values()) {
			parameters.add(convertToParameter(p, p.getValue()));
			numberOfConvertedParameters++;
		}

		for (org.hbird.core.commons.tmtc.Parameter<BigDecimal> p : pg.getBigDecimalParameters().values()) {
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

	public static Parameter convertToParameter(org.hbird.core.commons.tmtc.Parameter<?> p, Number v) {
		Parameter newBusinessParameter = new Parameter();
		newBusinessParameter.setDescription(p.getShortDescription());
		newBusinessParameter.setName(p.getQualifiedName());
		newBusinessParameter.setTimestamp(p.getReceivedTime());
		newBusinessParameter.setType("BigDecimal");
		newBusinessParameter.setValue(v);
		return newBusinessParameter;
	}

}
