import static org.junit.Assert.*;

import org.junit.*;
import org.junit.rules.ExpectedException;

import st.EntryMap;
import st.SimpleTemplateEngine;
import st.TemplateEngine;

public class Task1_Coverage {
	//
	private EntryMap map;
	private TemplateEngine engine;
	private SimpleTemplateEngine simpleEngine;

	@Before
	public void setUp() throws Exception {
		map = new EntryMap();
		engine = new TemplateEngine();
		simpleEngine = new SimpleTemplateEngine();
	}

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	/*-------------------- EntryMap Class Tests --------------------*/

	/*-------------------- TemplateEngine Class Tests --------------------*/

	@Test 
	public void tEngine_isMatchingModeValid() {
		map.store("name", "Bob");
		// if machingMode is invalid < 0 or > 7 it is assigned Integer.valueOf(O) (default)
		
		Integer expectedMatchingMode = Integer.valueOf(0); 
		Integer matchingMode1 = -2; 
		Integer matchingMode2 = 8; 
		
		String result1 = engine.evaluate("Hey ${name}", map, matchingMode1);
		String result2 = engine.evaluate("Hey ${name}", map, matchingMode2);
		String expected = engine.evaluate("Hey ${name}", map, expectedMatchingMode);
	
		assertEquals(result1, expected); 
		assertEquals(result2, expected); 
		
	}

	
	
	
	
	
	/*-------------------- SimpleTemplateEngine Class Tests --------------------*/
	@Test
	public void sEngine_InvalidMatchingMode1() {
		// When matching mode is null (an invalid matchingmode),
		// matching mode is set to default. and therefore should be
		// case insensitive and word separator insensitive.
		String template = "Hi, my name isDavid. David is my forename.";
		String pattern = "DAVID";
		String value = "Peter";
		Integer matchingMode = null;

		String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
		String expected = "Hi, my name isPeter. Peter is my forename.";

		assertEquals(result, expected);
	}

	@Test
	public void sEngine_InvalidMatchingMode2() {
		// When matching mode is less than 1 (an invalid matchingmode),
		// matching mode is set to default. and therefore should be
		// case insensitive and word separator insensitive.
		String template = "Hi, my name isDavid. David is my forename.";
		String pattern = "DAVID";
		String value = "Peter";
		Integer matchingMode = -1;
		// System.out.println(simpleEngine.caseSensative(SimpleTemplateEngine.));
		String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
		String expected = "Hi, my name isPeter. Peter is my forename.";

		assertEquals(result, expected);
	}

	@Test
	public void sEngine_InvalidMatchingMode3() {
		// When matching mode is more than 3 (an invalid matchingmode),
		// matching mode is set to default. and therefore should be
		// case insensitive and word separator insensitive.
		String template = "Hi, my name isDavid. David is my forename.";
		String pattern = "DAVID";
		String value = "Peter";
		Integer matchingMode = 4;

		String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
		String expected = "Hi, my name isPeter. Peter is my forename.";

		assertEquals(result, expected);
	}

	@Test
	public void sEngineSpec4_HashNumberOutOfBounds() {
		// Number after hash is larger than number of occurences of the pattern
		// in the input string.
		String template = "Hi, my name is David. David is my forename.";
		String pattern = "David#5";
		String value = "Peter";
		Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;

		String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
		String expected = "Hi, my name is David. David is my forename.";
		assertEquals(result, expected);
	}

	@Test
	public void sEngineSpec4_HashFollowedByLetter() {
		// Everything after the hash is dropped including second hash
		// This seems to just return the original string.
		String template = "Hi, my name is David#a. David is my forename.";
		String pattern = "David#a";
		String value = "Peter";
		Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;

		String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
		String expected = "Hi, my name is David#a. David is my forename.";
		assertEquals(result, expected);
	}

	@Test
	public void sEngineSpec4_HashFollowedByNonAlphaNum() {
		// Everything after the hash is dropped including second hash
		// This seems to just return the original string.
		String template = "Hi, my name is David#,. David# is my forename.";
		String pattern = "David#,";
		String value = "Peter";
		Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;

		String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
		String expected = "Hi, my name is David#,. David# is my forename.";
		assertEquals(result, expected);
	}

	@Test
	public void sEngineSpec4_HashFollowedByNothing() {
		// Everything after the hash is dropped including second hash
		// This seems to just return the original string.
		String template = "Hi, my name is David#. David# is my forename.";
		String pattern = "David#";
		String value = "Peter";
		Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;

		String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
		String expected = "Hi, my name is David#. David# is my forename.";
		assertEquals(result, expected);
	}

	@Test
	public void sEngineSpec4_HashFollowedByHash() {
		// Everything after the hash is dropped including second hash
		// This seems to just return the original string.
		String template = "Hi, my name is David#. David## is my forename.";
		String pattern = "David##";
		String value = "Peter";
		Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;

		String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
		String expected = "Hi, my name is Peter. Peter# is my forename.";
		assertEquals(result, expected);
	}

	@Test
	public void sEngineSpec4_HashFollowedByMultiDigit() {
		// Everything after the hash is dropped including second hash
		// This seems to just return the original string.
		String template = "DavidDavidDavidDavidDavidDavidDavidDavidDavidDavidDavid";
		String pattern = "David#11";
		String value = "Peter";
		Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;

		String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
		String expected = "DavidDavidDavidDavidDavidDavidDavidDavidDavidDavidPeter";
		assertEquals(result, expected);
	}

	@Test
	public void sEngineWholeWorldNoMatch1() {
		// Not needed
		String template = "Hi, my name isDavid. David is my forename.";
		String pattern = "David";
		String value = "Peter";
		Integer matchingMode = SimpleTemplateEngine.WHOLE_WORLD_SEARCH;

		String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
		String expected = "Hi, my name isDavid. Peter is my forename.";
		assertEquals(result, expected);
	}

	@Test
	public void sEngine_WholeWorldNoMatch2() {
		// additional letters are at the end instead of at the beginning.
		String template = "Hi, my name Davidis. David is my forename.";
		String pattern = "David";
		String value = "Peter";
		Integer matchingMode = SimpleTemplateEngine.WHOLE_WORLD_SEARCH;

		String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
		String expected = "Hi, my name Davidis. Peter is my forename.";
		// System.out.println(result);
		assertEquals(result, expected);
	}

}
