package org.hbird.exchange.configurator;

public class ParameterBasedLimitComponentRequest extends ComponentConfigurationRequest {

	public String parameter;
	
	/** An expression in the format:
	 * 
	 *    [LL1 Name]:[LL1 Value]:[LL1 Desc] < [LL2 Name]:[LL2 Value]:[LL2 Desc] < ... < Parameter:[Parameter Name] < [UL1 Name]:[UL1 Value]:[UL1 Desc] < ...
	 *  */
	public String expression;
}
