package org.hbird.business.timecorrelation;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Calendar;
import java.util.Date;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

public class ScriptEngineOverlordTest {

	ScriptEngineOverlord scriptOverlord;
	private static File helloWorldPythonScript;
	private static File helloWorldRubyScript;
	private static File rubyTimeCorrelator;
	private static Date testGroundDate;
	private static long testSpaceDate;

	// Setup -----------------------

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		helloWorldPythonScript = ResourceUtils.getFile(ScriptEngineOverlordTest.class.getResource("./helloWorld.py"));
		helloWorldRubyScript = ResourceUtils.getFile(ScriptEngineOverlordTest.class.getResource("./helloWorld.rb"));
		rubyTimeCorrelator = ResourceUtils.getFile(ScriptEngineOverlordTest.class.getResource("./RubyTimeCorrelator.rb"));

		// 2011, April, 4, 23, 50, 30
		Calendar cal = Calendar.getInstance();
		cal.set(2011, 3, 4, 23, 53, 30);
		testGroundDate = cal.getTime();

		testSpaceDate = testGroundDate.getTime();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		scriptOverlord = new ScriptEngineOverlord();
	}

	@After
	public void tearDown() throws Exception {
	}


	// Tests -----------------------

	// JPython not in Maven yet - ignoring for the time being.
	@Ignore
	@Test
	public void testLoadPythonScriptEngine() throws FileNotFoundException, ScriptException {
		ScriptEngine engine = scriptOverlord.loadScriptEngine("python");
		assertNotNull("engine is null", engine);

		Reader script = new FileReader(helloWorldPythonScript);

		engine.eval(script);
	}

	@Test
	public void testLoadRubyScriptEngine() throws FileNotFoundException, ScriptException {
		ScriptEngine engine = scriptOverlord.loadScriptEngine("ruby");
		assertNotNull("engine is null", engine);

		Reader script = new FileReader(helloWorldRubyScript);

		engine.eval(script);
	}

	@Test
	public void testTimeCorrelatorRuby() throws Exception {
		ScriptEngine engine = scriptOverlord.loadScriptEngine("ruby");
		assertNotNull("engine is null", engine);
		Reader script = new FileReader(rubyTimeCorrelator);
		engine.eval(script);

		TimeCorrelator tcor = (TimeCorrelator) engine.getContext().getAttribute("timeCorrelator");
		tcor.convertToSpacecraftTime(testGroundDate);
		tcor.convertToGroundTime(testSpaceDate);
	}

}
