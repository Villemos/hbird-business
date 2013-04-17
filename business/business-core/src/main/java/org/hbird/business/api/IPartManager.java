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
package org.hbird.business.api;

import org.hbird.exchange.interfaces.IPart;
import org.hbird.exchange.interfaces.IStartablePart;

/**
 * @author Admin
 *
 */
public interface IPartManager {

	public void start(IStartablePart part);
	
	public void stop(String name);

	public IPart resolveParent(IPart child);
	
	/**
	 * The qualified name is a unique name of the object within the system. It has 
	 * the format 
	 * <li>[qualifier]/[name]</li>
	 * 
	 * The [qualifier] ensures that Name is put into a context that makes it unique.
	 * 
	 * As examples of qualifiers are
	 * <li>Parameter: A parameter is 'issuedBy' a Part. The Part is unique. The qualifier of a 
	 * Parameter is the name of the Part. The fully qualified name of a Parameter is thus [Part name]/[Parameter name]</li>
	 * 
	 * The qualifier depends on the type of the object, but is is guaranteed to uniquely
	 * identifying the element.
	 * 
	 * @return
	 */
	public String getQualifiedName(IPart part);
	
	public String getQualifiedName(IPart part, String separator);
}
