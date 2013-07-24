package org.hbird.business.api;

import org.apache.camel.CamelContext;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.EntityInstance;

/* Old IPublish is now split into two parts:
 * 	- IPublisher puts objects into the archive
 * 	- Injector throws them onto AMQ
 */
public class Injector extends HbirdApi {

	public Injector(String issuedBy, String destination) {
		super(issuedBy, destination);
	}
	
	public Injector(String issuedBy, String destination, CamelContext context) {
		super(issuedBy, destination, context);
	}
	
	public void inject(EntityInstance object) {
		if (object.getIssuedBy() == null) {
            object.setIssuedBy(issuedBy);
        }

        if (object instanceof Command) {
            Command cmd = (Command) object;
            if (cmd.getDestination() == null) {
                cmd.setDestination(destination);
            }
        }

        template.sendBody(inject, object);
	}

}
