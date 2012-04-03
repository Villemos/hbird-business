package com.villemos.estcube.generator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hbird.exchange.configurator.LimitComponentRequest;
import org.hbird.exchange.core.Parameter;

public class TemperatureGenerator {

	protected String source = "WeatherStation";
	protected String name = "WeatherStation.temperature";
	protected String description = "The temperature at the weather station.";
	protected String unit = "degree";
	protected Pattern pattern = Pattern.compile("Temperature:</td><td align=\"left\" valign=\"top\" class=\"small1\">(.*?)</td>");

	public Parameter generate(String htmlpage) {

		/** Extract the temperature from the HTML page. */
		Matcher matcher = pattern.matcher(htmlpage);
		matcher.find();
		Double value = Double.parseDouble(matcher.group(1).substring(0, matcher.group(1).length() - 2));

		return new Parameter(source, name, description, value, unit);
	}

	protected String lowerLimit = "below 5";	
	
	public LimitComponentRequest configure() {
		return new LimitComponentRequest(name, name + ".limit", "Limit of temperature.", lowerLimit);
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
}
