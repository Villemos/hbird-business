package org.hbird.business.configurator;

import org.hbird.business.validation.limits.BaseLimitChecker;
import org.hbird.business.validation.limits.LowerLimitChecker;
import org.hbird.business.validation.limits.StaticLimitChecker;
import org.hbird.business.validation.limits.UpperLimitChecker;
import org.hbird.exchange.configurator.LimitComponentRequest;
import org.hbird.exchange.validation.Limit.eLimitType;

public class LimitCheckComponentBuilder extends ComponentBuilder {

	protected String inCheckRoute = "activemq:topic:parameters?selector=name='PARAMETERNAME'";
	protected String inEnableRoute = "activemq:topic:parameters?selector=name='LIMITNAME_SWITCH'";
	protected String inUpdateRoute = "activemq:topic:parameters?selector=name='LIMITNAME_UPDATE'";
	protected String outParameters = "activemq:topic:parameters";

	@Override
	public void doConfigure() {

		LimitComponentRequest request = (LimitComponentRequest) this.request;

		if (request.limit.type == eLimitType.Lower) {
			createRoute(request.limit.parameter, new LowerLimitChecker("LowerLimitChecker_" + request.limit.parameter, "Lower limit checker for " + request.limit.parameter, request.limit));			
		}
		else if (request.limit.type == eLimitType.Upper) {
			createRoute(request.limit.parameter, new UpperLimitChecker("UpperLimitChecker_" + request.limit.parameter, "Upper limit checker for " + request.limit.parameter, request.limit));			
		}
		else if (request.limit.type == eLimitType.Static) {
			createRoute(request.limit.parameter, new StaticLimitChecker("StaticLimitChecker_" + request.limit.parameter, "Static limit checker for " + request.limit.parameter, request.limit));			
		}
	}
	
	protected void createRoute(String parameter, BaseLimitChecker limit) {
		
		/** Create the route for limit checking. */
		from(inCheckRoute.replaceAll("PARAMETERNAME", parameter))
		.bean(limit, "processParameter")
		.setHeader("isStateOf", simple("${in.body.isStateOf}"))
		.setHeader("isStateParameter", simple("true"))
		.setHeader("name", simple("${in.body.name}"))
		.to(outParameters);

		/** Create the route for enabling/disabling limit checking. */
		from(inEnableRoute.replaceAll("LIMITNAME", limit.getName()))
		.bean(limit, "processEnabled")
		.setHeader("isStateOf", simple("${in.body.isStateOf}"))
		.setHeader("name", simple("${in.body.name}"))
		.to(outParameters);

		/** Create the route for changing the limit value. */
		from(inUpdateRoute.replaceAll("LIMITNAME", limit.getName()))
		.bean(limit, "processLimit")
		.setHeader("isStateOf", simple("${in.body.isStateOf}"))
		.setHeader("name", simple("${in.body.name}"))
		.to(outParameters);
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
}
