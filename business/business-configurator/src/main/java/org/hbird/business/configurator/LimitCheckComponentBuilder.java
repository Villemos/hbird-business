package org.hbird.business.configurator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hbird.business.validation.parameter.BaseLimit;
import org.hbird.business.validation.parameter.LowerLimit;
import org.hbird.business.validation.parameter.UpperLimit;
import org.hbird.exchange.configurator.LimitComponentRequest;

public class LimitCheckComponentBuilder extends ComponentBuilder {

	/** The definition of the routes that the */
	protected String inCheckRoute = "activemq:topic:parameters?selector=name='PARAMETERNAME'";
	protected String inEnableRoute = "activemq:topic:parameters?selector=name='LIMITNAME_SWITCH'";
	protected String inUpdateRoute = "activemq:topic:parameters?selector=name='LIMITNAME_UPDATE'";
	protected String outParameters = "activemq:topic:parameters";

	protected Pattern upperLimitExpression = Pattern.compile("above\\s*(.+)");
	protected Pattern lowerLimitExpression = Pattern.compile("below\\s*(.+)");

	@Override
	public void doConfigure() {

		LimitComponentRequest request = (LimitComponentRequest) this.request;

		/** TODO Update to let the same limit be able to check multiple parameters. 
		 *       1. Lookup the limit in the local registry.
		 *       2. If present, then use the existing bean, else create new. */
		BaseLimit limit = null;

		Matcher upperLimitMatcher = upperLimitExpression.matcher(request.expression);
		if (upperLimitMatcher.find()) {
			limit = new UpperLimit(request.name, request.description, Double.parseDouble(upperLimitMatcher.group(1)));
		}

		Matcher lowerLimitMatcher = lowerLimitExpression.matcher(request.expression);
		if (lowerLimitMatcher.find()) {
			limit = new LowerLimit(request.name, request.description, Double.parseDouble(lowerLimitMatcher.group(1)));
		}

		if (limit == null) {
			/** Error. */
			return;
		}

		from("direct:parameters_" + request.name)
		.setHeader("name", simple("${in.body.name}"))
		.to(outParameters);

		from("direct:stateParameters_" + request.name)
		.setHeader("isStateOf", simple("${in.body.isStateOf}"))
		.to("direct:parameters_" + request.name);

		/** Create the route for limit checking. */
		from(inCheckRoute.replaceAll("PARAMETERNAME", request.parameter))
		.bean(limit, "processParameter")
		.to(getContext().getEndpoint("direct:stateParameters_" + request.name));

		/** Create the route for enabling/disabling limit checking. */
		from(inEnableRoute.replaceAll("LIMITNAME", request.name))
		.bean(limit, "processEnabled")
		.to(getContext().getEndpoint("direct:stateParameters_" + request.name));

		/** Create the route for changing the limit value. */
		from(inUpdateRoute.replaceAll("LIMITNAME", request.name))
		.bean(limit, "processLimit")
		.to(getContext().getEndpoint("direct:stateParameters_" + request.name));
	}

	public String getInCheckRoute() {
		return inCheckRoute;
	}

	public void setInCheckRoute(String inCheckRoute) {
		this.inCheckRoute = inCheckRoute;
	}

	public String getInEnableRoute() {
		return inEnableRoute;
	}

	public void setInEnableRoute(String inEnableRoute) {
		this.inEnableRoute = inEnableRoute;
	}

	public String getInUpdateRoute() {
		return inUpdateRoute;
	}

	public void setInUpdateRoute(String inUpdateRoute) {
		this.inUpdateRoute = inUpdateRoute;
	}

	public String getOutParameters() {
		return outParameters;
	}

	public void setOutParameters(String outParameters) {
		this.outParameters = outParameters;
	}

	public Pattern getUpperLimitExpression() {
		return upperLimitExpression;
	}

	public void setUpperLimitExpression(Pattern upperLimitExpression) {
		this.upperLimitExpression = upperLimitExpression;
	}

	public Pattern getLowerLimitExpression() {
		return lowerLimitExpression;
	}

	public void setLowerLimitExpression(Pattern lowerLimitExpression) {
		this.lowerLimitExpression = lowerLimitExpression;
	}


}
