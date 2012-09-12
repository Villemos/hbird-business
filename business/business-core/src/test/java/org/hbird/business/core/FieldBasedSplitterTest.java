package org.hbird.business.core;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class FieldBasedSplitterTest {

	class TestType {
		public boolean test1 = true;
		protected List<String> test2 = new ArrayList<String>();
	}
	
	@Test
	public void testSplit() {
		TestType data = new TestType();
		data.test2.add("test1");
		data.test2.add("test2");
		data.test2.add("test3");
		
		FieldBasedSplitter processor = new FieldBasedSplitter();
		
		try {
			assertTrue(processor.split(data).size() == 0);
			
			processor.setFieldName("test2");
			assertTrue(processor.getFieldName().equals("test2"));
			assertTrue(processor.split(data).size() == 3);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
