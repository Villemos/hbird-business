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
package org.hbird.exchange.core;

import java.lang.reflect.Method;

/** 
 * Comperator of two objects. The comparison depends heavily on the types of the 
 * objects being compared, and depends on the objects implementing a 'compareTo'
 * method.
 * 
 * The comparison is done using reflection, detecting the types of the objects
 * and (attempting) to invoke a 'compareTo' method if available.
 */
public class Comperator {

	/**
	 * The comparison will return
	 *  -1 if the left hand side (first argument) is considered HIGHER than the right hand side (second argument).
	 *   0 if the two values are considered equal.
	 *   1 if the right hand side (second argument) is considered HIGHER than the right hand side (second argument).
	 * 
	 * A value of NULL is always considered lower than all other values.
	 * 
	 * The comparison is done in the following steps, in the given order;
	 * - If both objects are null, then lhs == rhs (0).
	 * - If the lhs == null, then lhs < rhs (-1)
	 * - If the rhs == null, then the lhs > rhs (1)
	 * - If the class of lhs and rhs re NOT the same, but both a sub class of 'Number', then
	 *   cast both to 'Float' and use the compareTo method.
	 * - Else attempt to invoke the compareTo method on the lhs, parsing the rhs as parameter.
	 * 
	 * The method may very well fail. The lhs argument must contain a 'compareTo' method that
	 * can take the 'rhs' as an argument. When using complex classes, make therefore sure to
	 * implement a 'compareTo' method.
	 * 
	 * @param lhs The 'left hand side' of the logical expression.
	 * @param rhs The 'right hand side' of the logical expression.
	 * @return <0 if lhs < rhs, 0 if lhs == rhs, 1> if lhs > rhs. 
	 */
	public static int compare(Object lhs, Object rhs) {
		int returnValue = 0;

		if (lhs == null && rhs == null) {
			returnValue = 0;
		}
		else if (lhs == null) {
			returnValue = -1;
		}
		else if (rhs == null) {
			returnValue = 1;
		}
		else {
			/** Take the left hand side, and see if we can find a 'compareTo' function that
			 *  we can use to compare to the rhs. */
			try {
				for (Method candidateMethod : lhs.getClass().getMethods()) {
					if (candidateMethod.getName().equals("compareTo")) {

						Class<?> partypes[] = new Class[1];
						partypes[0] = lhs.getClass();

						/** If the rhs is not the same as the lhs, then try to cast it. */
						if (lhs.getClass() != rhs.getClass()) {

							/** If this is a Number, then we can convert. */
							if (lhs instanceof Number && lhs instanceof Number) {
								/** We use the Float comparison. */
								returnValue = (new Float(((Number) lhs).floatValue()).compareTo(((Number) rhs).floatValue()));
							}
						}
						else {
							Object arglist[] = {rhs};
							returnValue = (Integer) candidateMethod.invoke(lhs, arglist);
						}
						break;
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return returnValue;
	} 
}
