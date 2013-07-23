/**
 * Licensed to the Hummingbird Foundation (HF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The HF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * Licensed to the Hummingbird Foundation (HF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The HF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hbird.business.celestrack.http;

import org.hbird.business.api.ICatalogue;
import org.hbird.business.api.IPublisher;
import org.hbird.business.celestrack.CelestrackComponent;
import org.hbird.business.core.SoftwareComponentDriver;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The driver of the celestract component. Will create the beans and routes required to
 * pull TLEs from the Celestrack website.
 * 
 * @author Gert Villemos
 *
 */
public class CelestrackComponentDriver extends SoftwareComponentDriver<CelestrackComponent> {
	private IPublisher publisher;
	private ICatalogue catalogue;
	
	@Autowired
	public CelestrackComponentDriver(IPublisher publisher, ICatalogue catalogue) {
		this.publisher = publisher;
		this.catalogue = catalogue;
	}

	/* (non-Javadoc)
	 * @see org.hbird.business.core.SoftwareComponentDriver#doConfigure()
	 */
	@Override
	protected void doConfigure() {
        from(addTimer("celestrack", entity.getPeriod())).bean(new CelestrackReader(entity, publisher, catalogue), "read");
	}
}
