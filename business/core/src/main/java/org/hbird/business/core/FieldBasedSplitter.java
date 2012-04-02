package org.hbird.business.core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Body;
import org.hbird.exchange.core.Named;

public class FieldBasedSplitter extends AllFields {

	/**
	 * The name of the field in the body object to be accessed as a time. 
	 */
	protected String fieldName = "tasks";

	public List<Object> split(@Body Named body) {

		try {
			/** Get all fields of this class, including all superclass fields. */
			Map<String, Field> fields = new HashMap<String, Field>();
			recursiveGet(body.getClass(), fields);

			fields.get(fieldName).setAccessible(true);

			if (fields.get(fieldName).get(body) instanceof List) {
				return (List) fields.get(fieldName).get(body);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ArrayList<Object>();
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
}
