import static org.junit.Assert.*;


import org.junit.*;
import org.junit.rules.ExpectedException;

import st.EntryMap;
import st.SimpleTemplateEngine;
import st.TemplateEngine;

public class Task1_Functional {
	
	private EntryMap map;
	private TemplateEngine engine;
	private SimpleTemplateEngine simpleEngine;

	@Before
	public void setUp() throws Exception {
		map = new EntryMap();
		engine = new TemplateEngine();
		simpleEngine = new SimpleTemplateEngine();
	}

	/*-------------------- EntryMap Class Tests --------------------*/

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	/*---------- EntryMap.store() ----------*/

	// Spec1: Template can't be null or Empty
	@Test
	public void entryStoreSpec1null() {
		thrown.expect(RuntimeException.class);
		map.store(null, "Adam");
	}

	@Test
	public void entryStoreSpec1empty() {
		thrown.expect(RuntimeException.class);
		map.store("", "Adam");
	}

	// Spec2: Replace value string can't be null
	@Test
	public void entryStoreSpec2null() {
		thrown.expect(RuntimeException.class);
		map.store("name", null);
	}


	// Spec3: Entries are ordered and follow order they appear in program
	@Test
	public void entryStoreSpec3sensitive() {

		map.store("name", "Bob");
		map.store("NAME", "Adam");

		// Patterns are not equal originally but are when case_insensititve
		// applied so Bob is added first

		String expected = "Order: Bob Bob";
		Integer matchingMode = TemplateEngine.CASE_INSENSITIVE;
		String result = engine.evaluate("Order: ${name} ${NAME}", map, matchingMode);
		assertEquals(expected, result);
	}
	
	@Test
	public void entryStoreSpec3insensitive() {

		map.store("name", "Bob");
		map.store("NAME", "Adam");

		// Patterns are not equal originally but are when case_insensititve
		// applied so Bob is added first

		String expected = "Order: Bob Adam";
		String result = engine.evaluate("Order: ${name} ${NAME}", map, TemplateEngine.CASE_SENSITIVE);
		assertEquals(expected, result);
	}

	// Spec4: Entries that exist cannot be stored again.
	@Test
	public void entryStoreSpec4overwrite() { 

		map.store("name", "Bob");
		map.store("name", "Adam");
		map.store("surname", "Smith");

		// Tests Adam doesn't overwrite Bob
		String expected = "Hello Bob Smith";
		Integer matchingMode = TemplateEngine.DEFAULT;
		String result = engine.evaluate("Hello ${name} ${SURNAME}", map, matchingMode);
		assertEquals(expected, result);

	}

	@Test
	public void entryStoreSpec4duplicateStored() {
		map.store("name", "Adam");
		map.store("name", "Adam");

		// Shows that duplicate is not stored
		int mapSize = map.getEntries().size();
		assertEquals(mapSize, 1);
	}

	/*---------- EntryMap.delete() ----------*/
	
	// Spec1: Template is not NUll or Empty
	@Test 
	public void entryDeleteSpec1empty() {
		thrown.expect(RuntimeException.class);
		map.delete("");
	}
	@Test 
	public void entryDeleteSpec1null() {
		thrown.expect(RuntimeException.class);
		map.delete(null);
	}
	
	// Spec2: After deleting a pair, other entries remain ordered 
	@Test
	public void entryDeleteSpec2_firstStaysOrdered() {
		
		map.store("name", "Bob");
		map.store("NAME", "Adam");
		map.store("Name", "John");
	
		// Bob will be first, Adam will be second
		// Shows Bob remains in first place after deleting Adam
		
		map.delete("NAME");
		String expected = "Bob"; 
		String result = engine.evaluate("${name}", map, TemplateEngine.CASE_INSENSITIVE); 
		assertEquals(result, expected);  
	}
	@Test
	public void entryDeleteSpec2_movesInOrder() {
		
		map.store("name", "Bob");
		map.store("NAME", "Adam");
		map.store("Name", "John");
		
		//Shows when Bob is removed, Adam takes first place position so remains ordered
		map.delete("name");
		String expected = "Adam"; 
		String result = engine.evaluate("${name}", map, TemplateEngine.CASE_INSENSITIVE); 
		assertEquals(result, expected);  
	}
	
	// Spec3: Only existing value pair can be deleted otherwise nothing happens
	
	@Test
	public void entryDeleteSpec3() {
		map.store("name", "Bob");
		map.store("surname", "Smith");
		
		Integer matchingMode = TemplateEngine.DEFAULT; 
		String result1 = engine.evaluate("Hello ${name} ${surname}", map, matchingMode); 
		
		// Deleting template not in map results in same string
		
		map.delete("middlename"); 
		String result2 = engine.evaluate("Hello ${name} ${surname}", map, matchingMode);
		assertEquals(result1, result2); 
	}
	
	/*---------- EntryMap.update() ----------*/
	
	// Spec1: Template is not null or empty
	@Test (expected = RuntimeException.class)
	public void entryUpdateSpec1empty() {
		map.update("", "Bob");
	}
	@Test 
	public void entryUpdateSpec1null() {
		thrown.expect(RuntimeException.class);
		map.update(null, "Bob");
	}
	// Spec2: New replace value string is not NULL
	@Test 
	public void entryUpdateSpec2null() {
		thrown.expect(RuntimeException.class);
		map.update("name", null);
	}

	// Spec3: Updating does not change existing order
	@Test
	public void entryUpdateSpec3() {
		
		map.store("name", "Bob"); 
		map.store("NAME", "Adam");
		
		Integer matchingMode = TemplateEngine.CASE_INSENSITIVE;
		
		// Shows Bob remains in first place after it's template is updated
		map.update("name", "Robert");
		String result = engine.evaluate("Hello ${Name}", map, matchingMode);
		assertEquals("Hello Robert", result); 
		
		// Shows Bob remains in first place after Adam's template is updated
		map.update("NAME", "John");
		String result2 = engine.evaluate("Hello ${Name}", map, matchingMode);
		assertEquals("Hello Robert", result2); 
	}
	
	
// Spec4: Only existing value pair can be updated
	@Test
	public void entryUpdateSpec4_updateExisting() {
		
		map.store("name", "Bob");
		int len = map.getEntries().size();
		
		// Shows update does nothing to length of list when on nonexistent template
		
		map.update("age", "20");
		int len2 = map.getEntries().size(); 
		
		assertEquals(len, len2); 
	}
	
	@Test
	public void entryUpdateSpec4_updateEmpty() { 
		map.update("age", "24");
		
		// Shows update does nothing to empty list
		
		int len = map.getEntries().size(); 
		assertEquals(len, 0);
	}
	
	/*-------------------- TemplateEngine Class Tests --------------------*/
	
// Spec1: If template string is NULL or empty, then unchanged template string is returned
	
	@Test
	public void tEngineSpec1_nullEmpty() { 
		// Tests that no exception is thrown on null or empty string
	    try {
	    	map.store("name", "Bob");
	    	engine.evaluate("", map, TemplateEngine.DEFAULT); 
	    } catch (RuntimeException e){
	        assertNull(e);
	    }
	}
	@Test
	public void tEngineSpec1_nullTemplate() {
	    try {
	    	// Tests no runtime exception is returned when a null template passed in 
	    	map.store("name", "Bob");
	    	engine.evaluate(null, map, TemplateEngine.DEFAULT); 
	    } catch (RuntimeException e){
	        assertNull(e);
	    }
	}		
	@Test
	public void tEngineSpec1_emptyMap() {
		map.store("name", "Bob");
		String expected = ""; 
		String result = engine.evaluate("", map, TemplateEngine.DEFAULT); 
		
		assertEquals(result,expected); 
	}
	
	@Test
	public void tEngineSpec1d() {
		map.store("name", "Bob");
		String expected = null; 
		String result = engine.evaluate(null, map, TemplateEngine.DEFAULT); 
	
		assertEquals(result,expected); 
	}

// Spec2: If EntryMap is NULL or empty then unchanged template string is returned

	@Test 
	public void tEngineSpec2_noException() { 
	    try {
	    	EntryMap map2 = null; 
	    String template = "Hello ${name}";
	    	engine.evaluate(template, map2, TemplateEngine.DEFAULT); 
	    	
	    } catch (Exception e){
	        assertNull(e);
	    }
	}
	
	@Test 
	public void tEngineSpec2_nullMap() { 
		
		// tests template is returned unchanged with null map
		EntryMap map2 = null; 
		String expected = "Hello ${name}"; 
		String result = engine.evaluate("Hello ${name}", map2, TemplateEngine.DEFAULT); 
		
		assertEquals(result,expected); 
	}
	
	@Test
	public void tEngineSpec2_emptyMap() {
		
		String expected = "Hello ${name}"; 
		String result = engine.evaluate("Hello ${name}", map, TemplateEngine.DEFAULT); 
		
		assertEquals(result,expected); 
		
	}
	
// Spec3:
	// If matching mode = 0 or NULL or default, KEEP_UNMATCHED, CASE_INSENSITIVE, ACCURATE_SEARCH is used
	@Test
	public void tEngineSpec3_default() {
		
		map.store("t itle", "Mr.");
		map.store("name", "Bob");
		map.store("surname", "Smith");
		
		//String examples which shows keep unmatched, case insensitive and accurate search are used

		Integer matchingMode = TemplateEngine.DEFAULT; 
		String input = "Full Name: ${title} ${NAME} ${middlename} ${SURNAME}"; 
		String expected = "Full Name: ${title} Bob ${middlename} Smith"; 
		
		String result = engine.evaluate(input, map, matchingMode); 
		assertEquals(result, expected); 		
	}
	@Test
	public void tEngineSpec3_0() {
		
		map.store("t itle", "Mr.");
		map.store("name", "Bob");
		map.store("surname", "Smith");
		
		Integer matchingMode = 0;
		
		String input = "Full Name: ${title} ${NAME} ${middlename} ${SURNAME}"; 
		String expected = "Full Name: ${title} Bob ${middlename} Smith"; 
		
		String result = engine.evaluate(input, map, matchingMode); 
		
		assertEquals(result, expected); 		
	}
	@Test
	public void tEngineSpec3_null() {
		
		map.store("t itle", "Mr.");
		map.store("name", "Bob");
		map.store("surname", "Smith");
		
		Integer matchingMode = null; 
		
		String input = "Full Name: ${title} ${NAME} ${middlename} ${SURNAME}"; 
		String expected = "Full Name: ${title} Bob ${middlename} Smith"; 
		
		String result = engine.evaluate(input, map, matchingMode); 
		
		assertEquals(result, expected); 	
	}
	
	
	// if matching mode = DELETE_UNMACHED, then DELETE_UNMATCHED, CASE_INSENSITIVE, ACCURATE_SEARCH is used
	@Test
	public void tEngineSpec3_deleteUnmatched() {
		
		map.store("t itle", "Mr.");
		map.store("name", "Bob");
		map.store("surname", "Smith");
		
		Integer matchingMode = TemplateEngine.DELETE_UNMATCHED; 
		
		//String examples which shows delete unmatched, case insensitive and accurate search are used
		//Doesn't match "t title". Deletes ${title} and ${middle name}. Matches NAME with name etc. 
		
		String input = "Full Name: ${title} ${NAME} ${middlename} ${SURNAME}"; 
		String expected = "Full Name:  Bob  Smith"; 
		String result = engine.evaluate(input, map, matchingMode); 
	
		assertEquals(result, expected); 
	}
	
	// If matchingMode = CASE_SENSITIVE | BLUR_SEARCH, then KEEP_UNMATCHED, CASE_SENSITVE, BLUR_SEARCH is used
	
	@Test
	public void tEngineSpec3_caseSensitive() {
		
		map.store("t itle", "Mr.");
		map.store("name", "Bob");
		map.store("surname", "Smith");
		
		Integer matchingMode = TemplateEngine.CASE_SENSITIVE|TemplateEngine.BLUR_SEARCH; 
		
		//Blur search matches ${title} with "t itle". 
		//Case_sensitive means ${NAME} doesn't match with "name"
		//Keep unmatched keeps ${NAME} and ${middle name}. 
		
		String input = "Full Name: ${title} ${NAME} ${middlename} ${surname}"; 
		String expected = "Full Name: Mr. ${NAME} ${middlename} Smith"; 
		
		String result = engine.evaluate(input, map, matchingMode); 
	
		assertEquals(result, expected); 
	}
	
	// When contradictory matching mode is set, the non-default one is used. 
	
	// Test matching. DELETE_UNMATCHED is non-default
	@Test    
	public void tEngineSpec3_contradictoryMatch() {
		
		map.store("name", "Bob");
		
		Integer matchingMode = TemplateEngine.DELETE_UNMATCHED|TemplateEngine.KEEP_UNMATCHED; 
		
		String input = "Hi ${name}${surname}"; 
		String expected = "Hi Bob";
		
		String result = engine.evaluate(input, map, matchingMode); 
		
		assertEquals(result, expected); 
	}
	
	//Testing case sensitivity. case sensitive is non-default
	@Test
	public void tEngineSpec3_contradictoryCase() {
		
		map.store("Name", "Bob"); 
		map.store("name","Bobby"); 
		
		Integer matchingMode = TemplateEngine.CASE_INSENSITIVE|TemplateEngine.CASE_SENSITIVE; 
		
		// If case is insensitive, result is "Hi Bob". If case is sensitive, result is "Hi Bobby"
		String input = "Hi ${name}"; 
		String expected = "Hi Bobby"; 
		
		String result = engine.evaluate(input, map, matchingMode); 
		assertEquals(result, expected);
	}
	//Testing search. Blur search is non default
	@Test
	public void tEngineSpec3_contradictorySearch() {
		
		map.store("na me", "Bob"); 
		map.store("name","Bobby"); 
		
		Integer matchingMode = TemplateEngine.BLUR_SEARCH|TemplateEngine.ACCURATE_SEARCH; 
		
		// If accurate_search is used, output is "Hi Bobby". If blur search, output is "Hi Bob"
		
		String input = "Hi ${name}"; 
		String expected = "Hi Bob"; 
		
		String result = engine.evaluate(input, map, matchingMode); 
		assertEquals(result, expected);
	}	
	
// Spec4: In a template, everything between its boundaries ${} is treated as normal text when 
// matched against an entry
	
	@Test 
	public void tEngineSpec4() {
		
		//  ${${text}} to show the nested ${} are treated as normal text as they are inside a boundary
		 
		map.store("${name}", "Bob");
		
		String result = engine.evaluate("Hey ${${name}}", map, TemplateEngine.DEFAULT); 
		String expected = "Hey Bob"; 
		
		assertEquals(expected, result);
	}

// Spec5: WHen a template is matched against an entry key & BLUR_SEARCH is enabled, any non visible
// character does not affect the result
	
	@Test
	public void tEngineSpec5_space() {	
			
		map.store("middle name", "Peter");
			
		Integer matchingMode = engine.BLUR_SEARCH;		
	
		String input = "${middlename}, ${middle name}"; //no space, 2 spaces, 1 tab
		String expectedBlur = "Peter, Peter"; 
			
		String result1 = engine.evaluate(input, map, matchingMode);
		assertEquals(expectedBlur, result1); 
	}
	@Test
	public void tEngineSpec5_tab() {	
			
		map.store("middle name", "Peter");
			
		Integer matchingMode = engine.BLUR_SEARCH; 
		String input =  "${middle		name}"; //tab
		String expectedBlur = "Peter"; 
		String result1 = engine.evaluate(input, map, matchingMode);
			
		assertEquals(expectedBlur, result1); 
	}
	
	@Test
	public void tEngineSpec5_newline() {	
		// Newline is ignored and middle name matched
		map.store("middle name", "Peter");
		Integer matchingMode = engine.BLUR_SEARCH; 
		String input = "${middle"
					+ "name}"; 
		String result = engine.evaluate(input, map, matchingMode);
			
		assertEquals("Peter", result); 
	}
	
// Spec6: When CASE_INSENSITIVE is enabled, letter case is not taken in consideration when 
// matching against entries

	@Test 
	public void tEngineSpec6_caseInsesitive() {
		
		map.store("Name", "Adam"); 
		map.store("NAME", "Bob");
		
		// as case is insensitive ${NAME} the first variation of ${name} which is Adam
		
		String input = "Hey ${NAME}";
		String expected = "Hey Adam"; 
		String result = engine.evaluate(input, map, TemplateEngine.DEFAULT); 
		
		assertEquals(result, expected) ; 
	}

// Spec7: In a template string every ${} acts as a boundary of at MOST one template
	
	@Test 
	public void tEngineSpec7() {
		// Shows that ${name surname} does not act as boundary for the two templates name & surname
		map.store("name", "Bob");
		map.store("surname", "Smith");
		
		String input = "Hey ${name surname}"; 
		String compare = "Hey Bob Smith"; 
		String result = engine.evaluate(input, map, TemplateEngine.DEFAULT); 
		
		assertNotEquals(result, compare);  
		
		}
	
// Spec8: In a template string, different templates are ordered according to their length, 
// the shorter template precedes
	
	@Test
	public void tEngineSpec8() {
		
		map.store("name","Adam");
		map.store("age :)", "25");
		map.store("symbol", ":)");
		
		// As symbol is smaller than ${age {symbol}} it is evaluated first so 
		// {symbol} evaluated to :), giving ${age ?} 
		// ${age :)} matched to 25
		
		String input = "Hello ${name}, is your age ${age ${symbol}}";
		String expected = "Hello Adam, is your age 25"; 
		String result = engine.evaluate(input, map, TemplateEngine.DEFAULT); 
		
		assertEquals(expected, result) ; 
	}
	
// Spec9: The engine processes one template at a time and attempts to match it against the keys of the
// EntryMap entries until there is a match or the entry list is exhausted. 
	
	
	
	/*-------------------- SimpleTemplateEngine Class Tests --------------------*/
	
//Spec1: If template string null or empty, unchanged template string is returned
	@Test
	public void sEngineSpec1Empty() {
		String template = "";
		String pattern = "David";
		String value = "Peter";
		Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;

		String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
		String expected = "";

		assertEquals(result, expected);
	}

	@Test
	public void sEngineSpec1Null() {
		String template = null;
		String pattern = "David";
		String value = "Peter";
		Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;

		String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
		String expected = null;

		assertEquals(result, expected);
	}

	// Spec2: If formatted pattern is string null or empty, unchanged template
	// string is returned
	@Test
	public void sEngineSpec2Empty() {
		String template = "Hi, my name is David. David is my forename.";
		String pattern = "";
		String value = "Peter";
		Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;

		String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
		String expected = "Hi, my name is David. David is my forename.";

		assertEquals(result, expected);
	}

	@Test
	public void sEngineSpec2Null() {
		String template = "Hi, my name is David. David is my forename.";
		String pattern = null;
		String value = "Peter";
		Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;

		String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
		String expected = "Hi, my name is David. David is my forename.";

		assertEquals(result, expected);
	}

	// Spec3: If value string is null or empty, unchanged template string is
	// returned
	@Test
	public void sEngineSpec3Empty() {
		String template = "Hi, my name is David. David is my forename.";
		String pattern = "David";
		String value = "";
		Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;

		String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
		String expected = "Hi, my name is David. David is my forename.";

		assertEquals(result, expected);
	}

	@Test
	public void sEngineSpec3Null() {
		String template = "Hi, my name is David. David is my forename.";
		String pattern = "David";
		String value = null;
		Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;

		String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
		String expected = "Hi, my name is David. David is my forename.";

		assertEquals(result, expected);
	}
	// Spec4: #n means nth occurence of pattern is replaced

	// Spec4-1 When pattern (2nd argument) is a string with no #, all occurrences
	// are replaced.
	@Test
	public void sEngineSpec4_1() {
		String template = "Hi, my name is David. David is my forename.";
		String pattern = "David";
		String value = "Peter";
		Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;

		String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
		String expected = "Hi, my name is Peter. Peter is my forename.";

		assertEquals(result, expected);
	}

	// Spec4-2 Replacing specific occurences 
	
	@Test
	public void sEngineSpec4_2a() {
		// The 3rd David Exists.
		String template = "Hi, my name is David. David is my forename. David.";
		String pattern = "David#3";
		String value = "Peter";
		Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;

		String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
		String expected = "Hi, my name is David. David is my forename. Peter.";

		assertEquals(result, expected);
	}

	@Test
	public void sEngineSpec4_2b() {
		// The 3rd David does not exist.
		String template = "Hi, my name is David. David is my forename.";
		String pattern = "David#3";
		String value = "Peter";
		Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;

		String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
		String expected = "Hi, my name is David. David is my forename.";

		assertEquals(result, expected);
	}

	@Test
	public void sEngineSpec4_2c() {
		// No David exists.
		String template = "This is a sentence.";
		String pattern = "David#3";
		String value = "Peter";
		Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;

		String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
		String expected = "This is a sentence.";

		assertEquals(result, expected);
	}

	// Spec4-3 doubles of hashes are treated as single hash
	@Test
	public void sEngineSpec4_3() {
		String template = "Hi, my name is David#. David is my forename.";
		String pattern = "David##";
		String value = "Peter";
		Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;

		String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
		String expected = "Hi, my name is Peter. David is my forename.";

		assertEquals(result, expected);
	}

	// Spec4-4 Multiple hashes on specific replacements work correctly
	@Test
	public void sEngineSpec4_4() {
		String template = "Hi, my name is David#. David# is my forename.";
		String pattern = "David###2";
		String value = "Peter";
		Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;

		String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
		String expected = "Hi, my name is David#. Peter is my forename.";

		assertEquals(result, expected);
	}

	// Spec4-5 Hash placed within a word means only preceding characters are used as replacement
	@Test
	public void sEngineSpec4_5() {
		String template = "Hi, David, my name is David. David is my forename.";
		String pattern = "David#3d";
		String value = "Peter";
		Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;

		String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
		String expected = "Hi, David, my name is David. Peter is my forename.";

		assertEquals(result, expected);
	}
	
	// Spec5: the default matching mode is not case sensitive and the pattern can be either a word or part of the word. 
	@Test
	public void sEngineSpec5DefaultCaseA() {
		// Tests that this matchingmode is not case sensitive
		String template = "Hi, my name is David. David is my forename.";
		String pattern = "DAVID";
		String value = "Peter";
		Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;

		String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
		String expected = "Hi, my name is Peter. Peter is my forename.";

		assertEquals(result, expected);
	}

	@Test
	public void sEngineSpec5DefaultCaseB() {
		// Tests that this matchingmode is not case sensitive.
		// Possibly not needed
		String template = "Hi, my name is David. David is my forename.";
		String pattern = "David#2";
		String value = "Peter";
		Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;

		String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
		String expected = "Hi, my name is David. Peter is my forename.";

		assertEquals(result, expected);
	}

	@Test
	public void sEngineSpec5DefaultSeparationA() {
		// Tests that this matchingmode is not seperation character-sensitive
		// Separate tests for each non alphanumeric character? *delete_later*
		String template = "Hi, my name isDavid. David is my forename.";
		String pattern = "David";
		String value = "Peter";
		Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;

		String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
		String expected = "Hi, my name isPeter. Peter is my forename.";

		assertEquals(result, expected);
	}

	@Test
	public void sEngineSpec5DefaultSeparationB() {
		// Tests that this matchingmode is not seperation character-sensitive
		// Separate tests for each non alphanumeric character? *delete_later*
		String template = "Hi, my name is David. David is my forename.";
		String pattern = "David";
		String value = "Peter";
		Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;

		String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
		String expected = "Hi, my name is Peter. Peter is my forename.";

		assertEquals(result, expected);
	}

	@Test
	public void sEngineSpec5WholeWorld_SeparationTest() {
		//Situation where word isn't separated
		String template = "Hi, my name isDavid. David is my forename.";
		String pattern = "David";
		String value = "Peter";
		Integer matchingMode = SimpleTemplateEngine.WHOLE_WORLD_SEARCH;

		String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
		String expected = "Hi, my name isDavid. Peter is my forename.";

		assertEquals(result, expected);
	}

	@Test
	public void sEngineSpec5WholeWorld_CaseTest() { //
		String template = "Hi, my name is DAVID. David is my forename.";
		String pattern = "DAVID";
		String value = "Peter";
		Integer matchingMode = SimpleTemplateEngine.WHOLE_WORLD_SEARCH;

		String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
		String expected = "Hi, my name is Peter. Peter is my forename.";

		assertEquals(result, expected);
	}

	@Test
	public void sEngineSpec5CaseSensitive_CaseTest() { //
		String template = "Hi, my name is DAVID. David is my forename.";
		String pattern = "DAVID";
		String value = "Peter";
		Integer matchingMode = SimpleTemplateEngine.CASE_SENSITIVE;

		String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
		String expected = "Hi, my name is Peter. David is my forename.";

		assertEquals(result, expected);
	}

	@Test
	public void sEngineSpec5CaseSensitive_SeparationTest() { //
		String template = "Hi, my name isDavid. David is my forename.";
		String pattern = "David";
		String value = "Peter";
		Integer matchingMode = SimpleTemplateEngine.CASE_SENSITIVE;

		String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
		String expected = "Hi, my name isPeter. Peter is my forename.";

		assertEquals(result, expected);
	}

	@Test
	public void sEngineSpec5CaseAndWholeWorld_SeparationTest() { //
		String template = "Hi, my name isDavid. David is my forename.";
		String pattern = "David";
		String value = "Peter";
		Integer matchingMode = SimpleTemplateEngine.CASE_SENSITIVE | SimpleTemplateEngine.WHOLE_WORLD_SEARCH;

		String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
		String expected = "Hi, my name isDavid. Peter is my forename.";

		assertEquals(result, expected);
	}

	@Test
	public void sEngineSpec5CaseAndWholeWorld_CaseTest() { //
		String template = "Hi, my name is DAVID. David is my forename.";
		String pattern = "David";
		String value = "Peter";
		Integer matchingMode = SimpleTemplateEngine.CASE_SENSITIVE | SimpleTemplateEngine.WHOLE_WORLD_SEARCH;

		String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
		String expected = "Hi, my name is DAVID. Peter is my forename.";

		assertEquals(result, expected);
	}

	// Spec6 - after a pattern string is replaces the program continues to match pattern from the end of the replaced value
	@Test
	public void sEngineSpec6() { 
		String template = "Hi, my name is David. David is my forename.";
		String pattern = "David";
		String value = "DavidDavid";
		Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;

		String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
		String expected = "Hi, my name is DavidDavid. DavidDavid is my forename.";

		assertEquals(result, expected);
	}
}

// final version
