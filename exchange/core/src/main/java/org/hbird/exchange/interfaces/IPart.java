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
package org.hbird.exchange.interfaces;

import java.util.List;

import org.hbird.exchange.core.Command;

/**
 * Indicates that this object is 'part of' a whole.
 * 
 * The interface can be used to navigate to the parent part. Each part
 * can only be part of one other object.
 * 
 * @author Gert Villemos
 *
 */
public interface IPart {
	
	/**
	 * Method to get the Named object that this object is a part of.
	 * 
	 * @return The parent object of which this is a part.
	 */
	public IPart getIsPartOf();
	
	
	/**
	 * Method to set the object that this object is a part of.
	 * 
	 * @param parent The parent object of this object.
	 */
	public void setIsPartOf(IPart parent);

	public String getQualifiedName();
	
	public String getQualifiedName(String separator);
	
	public List<Command> getCommands();
}
