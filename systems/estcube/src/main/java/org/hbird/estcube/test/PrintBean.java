package org.hbird.estcube.test;

import org.apache.camel.Body;

public class PrintBean {

	public void process(@Body String obj) {
		System.out.println("TEST " + obj);
	}
}
