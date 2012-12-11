package org.hbird.business.configurator;

import org.apache.camel.model.ProcessorDefinition;
import org.hbird.business.validation.limits.BaseLimitChecker;
import org.hbird.business.validation.limits.LowerLimitChecker;
import org.hbird.business.validation.limits.StaticLimitChecker;
import org.hbird.business.validation.limits.UpperLimitChecker;
import org.hbird.exchange.configurator.StartLimitComponent;
import org.hbird.exchange.validation.Limit;
import org.hbird.exchange.validation.Limit.eLimitType;

public class LimitCheckComponentBuilder extends ComponentBuilder {

	@Override
	public void doConfigure() {

		StartLimitComponent request = (StartLimitComponent) this.request;
		
		Limit limit = (Limit) request.getArguments().get("limit");

		String componentname = (String) request.getArguments().get("componentname");

		String limitValueParameterName = componentname + "_VALUE";
		if (request.getArguments().containsKey("limitvalue")) {
			limitValueParameterName = (String) request.getArguments().get("valueparameter");
		}
		
		if (limit.type == eLimitType.Lower) {
			createRoute(limit.limitOfParameter, new LowerLimitChecker(limit), componentname, limitValueParameterName);			
		}
		else if (limit.type == eLimitType.Upper) {
			createRoute(limit.limitOfParameter, new UpperLimitChecker(limit), componentname, limitValueParameterName);			
		}
		else if (limit.type == eLimitType.Static) {
			createRoute(limit.limitOfParameter, new StaticLimitChecker(limit), componentname, limitValueParameterName);			
		}
	}

	protected void createRoute(String parameter, BaseLimitChecker limit, String componentname, String limitValueName) {

		/** Create the route for limit checking. */
		ProcessorDefinition route = from(StandardEndpoints.monitoring + "?selector=name='" + parameter + "'")
				.bean(limit, "processParameter");
		addInjectionRoute(route);

		/** Create the route for enabling/disabling limit checking. */
		route = from(StandardEndpoints.monitoring + "?selector=isStateOf='" + componentname + "'")
				.bean(limit, "processEnabled");
		addInjectionRoute(route);

		/** Create the route for changing the limit value. */
		route = from(StandardEndpoints.monitoring + "?selector=name='" + limitValueName + "'")
				.bean(limit, "processLimit");
		addInjectionRoute(route);

		/** Route for commands to this component, i.e. configuration commands. */
		from(StandardEndpoints.commands + "?" + addDestinationSelector(getComponentName())).bean(defaultCommandHandler, "receiveCommand");
	}
}