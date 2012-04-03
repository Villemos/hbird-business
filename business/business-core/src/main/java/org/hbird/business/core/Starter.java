package org.hbird.business.core;

import org.apache.camel.Main;
import org.apache.log4j.Logger;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class Starter {

	private static org.apache.log4j.Logger LOG = Logger.getLogger("main");

	protected Main main = null;	

	public static void main(String[] args) throws Exception {
		Starter starter = new Starter();
		starter.boot();
	}
	
	protected void boot() throws Exception {
		LOG.info("Starting Hummingbird based system.");
				
		main = new Main();
		main.enableHangupSupport();
		
		/** Read the configuration file as the first argument. If not set, then we try the default name. */
		String assemblyFile = System.getProperty("assembly") == null ? "classpath:assembly.xml" : System.getProperty("assembly");

		LOG.info("Reading assembly file '" + assemblyFile + "'");
		new FileSystemXmlApplicationContext(assemblyFile);

		main.run();
	}	
}