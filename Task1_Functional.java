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
	public void entryStoreSpec1a() {
		thrown.expect(RuntimeException.class);
		map.store(null, "Adam");
	}
	@Test 
	public void entryStoreSpec1b() {
		thrown.expect(RuntimeException.class);
		map.store("", "Adam");
	}
	
// Spec2: Replace value string can't be null
	@Test
	public void entryStoreSpec2() {
		thrown.expect(RuntimeException.class);
		map.store("name", null);
	}

// Spec3: Entries are ordered and follow order they appear in program
	@Test 
	public void entryStoreSpec3() {
		
        map.store("name", "Bob");
        map.store("NAME", "Adam");
        
        // Patterns are not equal originally but are when case_insensititve applied
        // So Bob is added first 
         
        String expected = "Order: Bob Bob";
        Integer matchingMode = TemplateEngine.CASE_INSENSITIVE;
        String result = engine.evaluate("Order: ${name} ${NAME}", map, matchingMode);
        assertEquals(expected, result); 
        
        String expected2 = "Order: Bob Adam";
        String result2 = engine.evaluate("Order: ${name} ${NAME}", map, TemplateEngine.CASE_SENSITIVE);
        assertEquals(expected2, result2); 
	}
	
	// TODO: might be repeating order tests
// Spec4: Entries that exist cannot be stored again.
	@Test 
	public void entryStoreSpec4a() {  // Tests Adam doesn't overwrite Bob
		
        map.store("name", "Bob");   
        map.store("name", "Adam");
        map.store("surname", "Smith");  
        
        String expected = "Hello Bob Smith"; 		
        Integer matchingMode = TemplateEngine.DEFAULT;
        String result = engine.evaluate("Hello ${name} ${SURNAME}", map, matchingMode); 
        assertEquals(expected, result); 
        
	}
	@Test 
	public void entryStoreSpec4b() {   // Tests second entry is not stored.
        map.store("name", "Adam");   
        map.store("name", "Adam");
        
        int mapSize = map.getEntries().size();
        assertEquals(mapSize, 1); 
	}

	
	/*---------- EntryMap.delete() ----------*/
	
// Spec1: Template is not NUll or Empty
	@Test 
	public void entryDeleteSpec1a() {
		thrown.expect(RuntimeException.class);
		map.delete("");
	}
	@Test 
	public void entryDeleteSpec1b() {
		thrown.expect(RuntimeException.class);
		map.delete(null);
	}
	
	// TODO: not sure about this 
// Spec2: After deleting a pair, other entries remain ordered 
	@Test
	public void entryDeleteSpec2() {
		
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
	public void entryDeleteSpec2b() {
		
		map.store("name", "Bob");
		map.store("NAME", "Adam");
		map.store("Name", "John");
		
		map.delete("name");
		String expected = "Adam"; 
		String result = engine.evaluate("${name}", map, TemplateEngine.CASE_INSENSITIVE); 
		
		//Shows when Bob is removed, Adam takes first place position so remains ordered
		assertEquals(result, expected);  
	}
	
// Spec3: Only existing value pair can be deleted otherwise nothing happens
	
	@Test
	public void entryDeleteSpec3() {
		map.store("name", "Bob");
		map.store("surname", "Smith");
		
		Integer matchingMode = TemplateEngine.DEFAULT; 
		String result1 = engine.evaluate("Hello ${name} ${surname}", map, matchingMode); 
		
		map.delete("middlename"); // Deleting template not in map
		
		String result2 = engine.evaluate("Hello ${name} ${surname}", map, matchingMode); 
		
		assertEquals(result1, result2); // Tests return string remains the same
	}
	
	/*---------- EntryMap.update() ----------*/
	
// Spec1: Template is not null or empty
	@Test 
	public void entryUpdateSpec1a() {
		thrown.expect(RuntimeException.class);
		map.update("", "Bob");
	}
	@Test 
	public void entryUpdateSpec1b() {
		thrown.expect(RuntimeException.class);
		map.update(null, "Bob");
	}
// Spec2: New replace value string is not NULL
	@Test 
	public void entryUpdateSpec2b() {
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
	public void entryUpdateSpec4a() {// Shows update does nothing to length of list when on nonexistent template
		map.store("name", "Bob");
		map.update("age", "20");
		int len = map.getEntries().size(); 

		map.update("age", "20");
		int len2 = map.getEntries().size(); 
		assertEquals(len, len2); 
	}
	
	@Test
	public void entryUpdateSpec4b() { // Shows update does nothing to empty list
		map.update("age", "24");
		int len = map.getEntries().size(); 
		assertEquals(len, 0);
	}
	
	/*-------------------- TemplateEngine Class Tests --------------------*/
	
// Spec1: If template string is NULL or empty, then unchanged template string is returned
	
	@Test
	public void tEngineSpec1a() { // Tests that no exception is thrown on null or empty string
	    try {
	    	map.store("name", "Bob");
	    	engine.evaluate("", map, TemplateEngine.DEFAULT); 
	    } catch (Exception e){
	        assertNull(e);
	    }
	}
	@Test
	public void tEngineSpec1b() {
	    try {
	    	map.store("name", "Bob");
	    	engine.evaluate(null, map, TemplateEngine.DEFAULT); 
	    } catch (Exception e){
	        assertNull(e);
	    }
	}		
	@Test
	public void tEngineSpec1c() {
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

	@Test   // Tests that no exception is thrown on null map
	public void tEngineSpec2a() { 
	    try {
	    	EntryMap map2 = null; 
	    String template = "Hello ${name}";
	    	engine.evaluate(template, map2, TemplateEngine.DEFAULT); 
	    } catch (Exception e){
	        assertNull(e);
	    }
	}
	
	@Test   // tests template is returned unchanged with null map
	public void tEngineSpec2b() { 
		EntryMap map2 = null; 
		String expected = "Hello ${name}"; 
		String result = engine.evaluate("Hello ${name}", map2, TemplateEngine.DEFAULT); 
		
		assertEquals(result,expected); 
	}
	
// Spec3:
	// If matchingmode = 0 or NULL or default, KEEP_UNMATCHED, CASE_INSENSITIVE, ACCURATE_SEARCH is used
	@Test
	public void tEngineSpec3a() {
		
		
		
		
	}
	
	
	
	// if matchingmode = DELETE_UNMACHED, then DELETE_UNMATCHED, CASE_INSENSITIVE, ACCURATE_SEARCH is used
	
	
	// If matchingmode = CASE_SENSITIVE | BLUR_SEARCH, then KEEP_UNMATCHED, CASE_SENSITVE, BLUR_SEARCH is used
	
	
	// When contradictory matching mode is set, the non-default one is used. 
	
		
	// If matchingmode = DELETE_UNMATCHED|KEEP_UNMATCHED|CASE_SENSITIVE, then ACCURATE_SEARCH
	// DELETE_UNMATCHED, CASE_SENSITIVE will be used.
	
	
	
	
	
	
// Spec4: In a template, everything between its boundaries ${} is treated as normal text when matched against an entry
	
	
// Spec5: WHen a template is matched against an entry key & BLUR_SEARCH is enabled, any non visible
// character dpes mpt affect the result
	
	
// Spec6: When CASE_INSENSITIVE is enabled, letter case is not taken in consideration when 
// matching against entries
	
	
// Spec7: In a template string every ${} acts as a boundary of at MOST one template
	
	
// Spec8: In a template string, different templates are ordered according to their length, 
// the shorter template precedes
	
	
// Spec9: The engine processes one template at a time and attempts to match it against the keys of the
// EntryMap entries until there is a match or the entry list is exhausted. 
	
}
