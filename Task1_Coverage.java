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
	
	// Tests for equals method (TODO doesn't add any coverage take out)
	
	@Test 
	public void entryMapEqualsObj() {
		EntryMap m1 = map; 
		Boolean result = map == m1; 
		Boolean expected = map.equals(m1); 
		assertEquals(result, expected); 
	}

	@Test
	public void entryMapEqualsNull() {
		EntryMap m2 = null;
		Boolean result = map.equals(m2); 
		Boolean expected = map.equals(null); 
		assertEquals(result, expected);
	}
	
	@Test
	public void entryMapEqualsNotNull() {
		
		map.store("name", "adam");
		Boolean result = map != null; 
		Boolean expected = map.equals(null); 
		assertEquals(result, true);
	}

	@Test 
	public void entryMapEqualsClass() {
		EntryMap m1 = new EntryMap(); 
		EntryMap m2 = new EntryMap(); 
		
		m1.store("name", "Bob");
		m1.store("name", "Adam");
		
		Boolean result = (m1.getClass() != m2.getClass()); 
		Boolean expected = m1.equals(m2); 
		
		assertEquals(result, expected); 
	}
	
	@Test 
	public void entryMapEqualsNotClass() {
		EntryMap m1 = map; 
		
		Boolean result = (m1.getClass() != map.getClass()); 
		Boolean expected = m1.equals(map); 
		assertNotEquals(result, expected); 
	}
	
	
	@Test
	public void entryUpdateSpec2emptysecondargument() {
		map.store("name", "Bob");
		map.update("name", "");
		String result = engine.evaluate("${name}", map, 0);
		assertEquals("", result);
	}

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

	@Test
	public void tEnginedoubledollar() {
		//tests results when there are multiple dollar signs in the template string
		map.store("name", "Adam");
		map.store("surname", "Dykes");
		map.store("age", "29");

		Integer matchingMode = TemplateEngine.DEFAULT;

		String input = "Hello, $$$${name}, is your age ${age}";
		String expected = "Hello, $$$Adam, is your age 29";
		String result = engine.evaluate(input, map, matchingMode);
		assertEquals(result, expected);
	}

	@Test
	public void tEngineDollarBracketBracket1() {
		//tests if 
		map.store("{name", "Adam");
		map.store("surname", "Dykes");
		map.store("age", "29");

		Integer matchingMode = TemplateEngine.DEFAULT;

		String input = "Hello, ${{name}";
		String expected = "Hello, Adam";
		String result = engine.evaluate(input, map, matchingMode);
		assertEquals(result, expected);
	}

	@Test
	public void tEngineDollarBracketBracket2() {
		// shows it doesn't match "name"
		map.store("name", "Adam");
		map.store("surname", "Dykes");
		map.store("age", "29");

		Integer matchingMode = TemplateEngine.DEFAULT;

		String input = "Hello, ${{name}";
		String expected = "Hello, ${{name}";
		String result = engine.evaluate(input, map, matchingMode);
		assertEquals(result, expected);
	}

	@Test
	public void tEngineRandom() {
		// may add no coverage
		map.store("greeting", "Hello");
		map.store("name", "Adam");
		map.store("HelloAdam", "Hello Adam");

		Integer matchingMode = TemplateEngine.DEFAULT;

		String input = "${${greeting}${name}}";
		String result = engine.evaluate(input, map, matchingMode);
		String expected = "Hello Adam";
		assertEquals(result, expected);
	}

	@Test
	public void tEngineRandom2() {
		map.store("greeting", "Hello");
		map.store("name", "Adam");
		map.store("Hello Adam", "Hello Adam");

		Integer matchingMode = TemplateEngine.DEFAULT;

		String input = "${${greeting} ${name}}";
		String result = engine.evaluate(input, map, matchingMode);
		String expected = "Hello Adam";
		assertEquals(result, expected);
	}

	@Test
	public void tEngineDollarCloseBracket() {
		// dissatisfies condition if (!templateCandidates.isEmpty())
		map.store("greeting", "Hello");
		map.store("name", "Adam");
		map.store("Hello Adam", "Hello Adam");

		Integer matchingMode = TemplateEngine.DEFAULT;

		String input = "$}greeting}";
		String result = engine.evaluate(input, map, matchingMode);
		String expected = "$}greeting}";
		assertEquals(result, expected);
		
	}

	@Test
	public void tEngineDollarEmptyTemplate() {
		// may add
		map.store(" ", "");
		map.store("surname", "Dykes");
		map.store("age", "29");

		Integer matchingMode = TemplateEngine.DEFAULT;

		String input = "Hello${}";
		String result = engine.evaluate(input, map, matchingMode);
		String expected = "Hello${}";
		assertEquals(result, expected);
	}

	@Test
	public void tEngineRandom3() {
		//A random test
		map.store("aaa", "Adam");
		map.store("a", "John");
		map.store("aa", "Dykes");

		Integer matchingMode = TemplateEngine.DEFAULT;

		String input = "${aaa}${a}${aa}${aaa}${a}";
		String result = engine.evaluate(input, map, matchingMode);
		String expected = "AdamJohnDykesAdamJohn";
		assertEquals(result, expected);
	}

	@Test
	public void tEngineReplaceTemplateAtVeryEnd() {
		map.store("name", "Bob");

		Integer matchingMode = TemplateEngine.DEFAULT;

		String input = "Hello ${name}";
		String result = engine.evaluate(input, map, matchingMode);
		String expected = "Hello Bob";
		assertEquals(result, expected);
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
		// David# does not match with David# in template string as # is dropped 
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
		// ## is treated as #
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
		// the pattern David#11 will remove the 11th David as expected and not the
		//first then drop the second "1"
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
		assertEquals(result, expected);
	}

	@Test
	public void sEngine_Random() {
		// Satisfies a branch of
		// if (charIndex + patternLength < originalText.length() &&
		// Character.isLetterOrDigit(originalText.charAt(charIndex +
		// patternLength)))
		String template = "Hello David";
		String pattern = "David";
		String value = "Peter";
		Integer matchingMode = SimpleTemplateEngine.WHOLE_WORLD_SEARCH;

		String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
		String expected = "Hello Peter";
		assertEquals(result, expected);
	}

	@Test
	public void sEngine_Random2() {
		String template = "HelloDavid";
		String pattern = "David";
		String value = "Peter";
		Integer matchingMode = SimpleTemplateEngine.WHOLE_WORLD_SEARCH;

		String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
		String expected = "HelloDavid";
		assertEquals(result, expected);
	}

	@Test
	public void sEngine_Random3() {
		// Satisfies a branch of charIndex!=0 &&
		// Character.isLetterOrDigit(originalText.charAt(charIndex-1))
		String template = "David";
		String pattern = "David";
		String value = "Peter";
		Integer matchingMode = SimpleTemplateEngine.WHOLE_WORLD_SEARCH;

		String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
		String expected = "Peter";
		assertEquals(result, expected);
	}
	
	@Test
	public void sEngine_Only_Hash() {
		// Satisfies branch if(!isTextValid(pattern))
		String template = "David";
		String pattern = "#";
		String value = "Peter";
		Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;

		String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
		String expected = "David";
		assertEquals(result, expected);
	}
}
