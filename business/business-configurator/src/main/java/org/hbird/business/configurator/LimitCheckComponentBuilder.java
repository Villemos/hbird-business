package org.hbird.business.configurator;

import org.hbird.business.validation.limits.BaseLimitChecker;
import org.hbird.business.validation.limits.LowerLimitChecker;
import org.hbird.business.validation.limits.StaticLimitChecker;
import org.hbird.business.validation.limits.UpperLimitChecker;
import org.hbird.exchange.configurator.LimitComponentRequest;
import org.hbird.exchange.validation.Limit.eLimitType;

public class LimitCheckComponentBuilder extends ComponentBuilder {

	protected String inCheckRoute = "activemq:topic:monitoringdata?selector=name='PARAMETERNAME'";
	protected String inEnableRoute = "activemq:topic:monitoringdata?selector=name='LIMITNAME_SWITCH'";
	protected String inUpdateRoute = "activemq:topic:monitoringdata?selector=name='LIMITNAME_UPDATE'";
	protected String outParameters = "activemq:topic:monitoringdata";

	@Override
	public void doConfigure() {

		LimitComponentRequest request = (LimitComponentRequest) this.request;

		if (request.limit.type == eLimitType.Lower) {
			createRoute(request.limit.limitOfParameter, new LowerLimitChecker(request.limit));			
		}
		else if (request.limit.type == eLimitType.Upper) {
			createRoute(request.limit.limitOfParameter, new UpperLimitChecker(request.limit));			
		}
		else if (request.limit.type == eLimitType.Static) {
			createRoute(request.limit.limitOfParameter, new StaticLimitChecker(request.limit));			
		}
	}
	
	protected void createRoute(String parameter, BaseLimitChecker limit) {
		
		/** Create the route for limit checking. */
		from(inCheckRoute.replaceAll("PARAMETERNAME", parameter))
		.bean(limit, "processParameter")
		.setHeader("issuedBy", simple("${in.body.issuedBy}"))
		.setHeader("name", simple("${in.body.name}"))
		.setHeader("type", simple("${in.body.type}"))
		.setHeader("isStateOf", simple("${in.body.isStateOf}"))
		.to(outParameters);

		/** Create the route for enabling/disabling limit checking. */
		from(inEnableRoute.replaceAll("LIMITNAME", limit.getLimit().getName()))
		.bean(limit, "processEnabled")
		.setHeader("issuedBy", simple("${in.body.issuedBy}"))
		.setHeader("name", simple("${in.body.name}"))
		.setHeader("type", simple("${in.body.type}"))
		.setHeader("isStateOf", simple("${in.body.isStateOf}"))
		.to(outParameters);

		/** Create the route for changing the limit value. */
		from(inUpdateRoute.replaceAll("LIMITNAME", limit.getLimit().getName()))
		.bean(limit, "processLimit")
		.setHeader("issuedBy", simple("${in.body.issuedBy}"))
		.setHeader("name", simple("${in.body.name}"))
		.setHeader("type", simple("${in.body.type}"))
		.setHeader("isStateOf", simple("${in.body.isStateOf}"))
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
