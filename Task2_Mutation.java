import static org.junit.Assert.*;

<<<<<<< HEAD
import org.junit.*;
import org.junit.rules.ExpectedException;

import st.EntryMap;
import st.SimpleTemplateEngine;
import st.TemplateEngine;

public class Task2_Mutation {

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
	
	
	
	
	/* ------------------- MUTATION 1 TESTS ----------------------- */ 
	
	@Test
	public void tEngineSpec3_null() {
		
		map.store("t itle", "Mr.");
		map.store("name", "Bob");
		map.store("surname", "Smith");
		
		Integer matchingMode = null; 
		
		String input = "Full Name: ${title} ${NAME} ${middlename} ${SURNAME}"; 
		String expected = "Full Name: ${title} Bob ${middlename} Smith"; 
		
		String result = engine.evaluate(input, map, matchingMode); 
		
		// real expected = Full Name: ${title} Bob ${middlename} Smith; 
		// mutant gives = Full Name: Mr. Bob ${middlename} Smith
				
		// So mutant is case insensitive when matchingmode is null 

		assertEquals(result, expected); 	
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
			
			/* Expected gives: Full Name: ${title} Bob ${middlename} Smith
			   Mutant givesFull Name: Mr. Bob ${middlename} Smith */
			
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
			
			/* Expected: Full Name: ${title} Bob ${middlename} Smith
			   Mutant gives: Full Name: Mr. Bob ${middlename} Smith */
			
			assertEquals(result, expected); 		
		}
		
		@Test
		public void tEngineSpec3_deleteUnmatched() {
			
			map.store("t itle", "Mr.");
			map.store("name", "Bob");
			map.store("surname", "Smith");
			
			Integer matchingMode = TemplateEngine.DELETE_UNMATCHED; 
			
			String input = "Full Name: ${title} ${NAME} ${middlename} ${SURNAME}"; 
			String expected = "Full Name:  Bob  Smith"; 
			String result = engine.evaluate(input, map, matchingMode); 
		
			/* Expected: Full Name:  Bob  Smith
			   Mutant: Full Name: Mr. Bob  Smith */
			
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
			
			/* Expected: Hello Adam 
			 * Mutant: ${Hello Adam}*/
			
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
			
			/* Expected: Hello${}
			   Mutant: Hello */
			
			assertEquals(result, expected);
		}
		
		@Test
		public void tEngineSpec8() {
			
			map.store("name","Adam");
			map.store("age :)", "25");
			map.store("symbol", ":)");
			
			String input = "Hello ${name}, is your age ${age ${symbol}}";
			String expected = "Hello Adam, is your age 25"; 
			String result = engine.evaluate(input, map, TemplateEngine.DEFAULT); 
			
			/* Expected: Hello Adam, is your age 25
				Mutant: Hello Adam, is your age ${age :)} */
			
			assertEquals(expected, result) ; 
		}
		
	
	/* ------------------- MUTATION 2 TESTS ----------------------- */ 
	
		@Test
		public void cant_readd_after_delete() {
			
			Integer matchingMode = TemplateEngine.DEFAULT; 
			String template = "Hi ${name}"; 
			
			map.store("name", "John");
			map.delete("name");
			map.store("name", "John");
			String result3 = engine.evaluate(template, map, matchingMode); 
		
			assertEquals("Hi John", result3); 
			
		}
		
		@Test
		public void cant_readd_after_delete_2() {
			
			Integer matchingMode = TemplateEngine.DEFAULT; 
			String template = "Hi ${name}"; 
			
			map.store("name", "John");
			String firstResult = engine.evaluate(template, map, matchingMode); 
			
			map.delete("name");
		
			map.store("name", "John");
			String secondResult = engine.evaluate(template, map, matchingMode); 
		
			assertEquals(firstResult, secondResult); 
			
		}
	
	
	
	
	
	/* ------------------- MUTATION 3 TESTS ----------------------- */ 
	
		
		@Test
		public void tEngineRandom3() {
			// A random test
			map.store("aaa", "Adam");
			map.store("a", "John");
			map.store("aa", "Dykes");

			Integer matchingMode = TemplateEngine.DEFAULT;

			String input = "${aaa}${a}${aa}${aaa}${a}";
			String result = engine.evaluate(input, map, matchingMode);
			String expected = "AdamJohnDykesAdamJohn";	
			
			/*Expected: AdamJohnDykesAdamJohn
			    Mutant:  AdamJohnDykeAdam}John */


			assertEquals(result, expected);
		}
		
		// Spec5: WHen a template is matched against an entry key & BLUR_SEARCH is enabled, any non visible
		// character does not affect the result
			
			
			public void tEngineSpec5_tab() {	
					
				map.store("middle name", "Peter");
					
				Integer matchingMode = engine.BLUR_SEARCH; 
				String input =  "${middle		name}"; //tab
				String expected = "Peter"; 
				String result = engine.evaluate(input, map, matchingMode);
				
				/*	Expected: Peter
					  Mutant:  Peter */
					
				assertEquals(expected, result); 
			}

			
		@Test
		public void tEngineSpec5_space() {	
				
			map.store("middle name", "Peter");
				
			Integer matchingMode = engine.BLUR_SEARCH;		
		
			String input = "${middlename}, ${middle name}"; //no space, 2 spaces, 1 tab
			String expected = "Peter, Peter"; 
				
			String result = engine.evaluate(input, map, matchingMode);

			
			/*	Expected: Peter, Peter
				  Mutant:  Peter,Peter}  */
			
			assertEquals(expected, result); 
		}
		
		@Test
		public void entryUpdateSpec2emptysecondargument() {
			map.store("name", "Bob");
			map.update("name", "");
			String result = engine.evaluate("${name}", map, 0);
			
			/*	Expected: 
				  Mutant:  */ //2 spaces vs 1
			
			assertEquals("", result);
		}


		@Test
		public void tEngineRandom2x() {
			map.store("greeting", "Hello");
			map.store("name", "Adam");
			map.store("Hello Adam", "Hello Adam");

			Integer matchingMode = TemplateEngine.DEFAULT;

			String input = "${${greeting} ${name}}";
			String result = engine.evaluate(input, map, matchingMode);
			String expected = "Hello Adam";

			/*	Expected: Hello Adam
				  Mutant:  Hello Adam
			*/
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

			/*	Expected: Hello Adam
				  Mutant:  Hello Adam */
						
			assertEquals(result, expected);
		}
		
		@Test
		public void entryDeleteSpec2_movesInOrder() {
			
			map.store("name", "Bob");
			map.store("NAME", "Adam");
			map.store("Name", "John");
					
			map.delete("name");
			String expected = "Adam"; 
			String result = engine.evaluate("${name}", map, TemplateEngine.CASE_INSENSITIVE); 
			
			assertEquals(result, expected);  
		}
		@Test
		public void tEngineSpec5_newline() {	
			// Newline is ignored and middle name matched
			map.store("middle name", "Peter");
			Integer matchingMode = engine.BLUR_SEARCH; 
			String input = "${middle"
						+ "name}"; 
			String result = engine.evaluate(input, map, matchingMode);	

			/*	Expected: Peter
	  			  Result:  Peter		*/
			
			assertEquals("Peter", result); 
		}
		
		// Spec2: After deleting a pair, other entries remain ordered 
			@Test
			public void entryDeleteSpec2_firstStaysOrdered() {
				
				map.store("name", "Bob");
				map.store("NAME", "Adam");
				map.store("Name", "John");

				map.delete("NAME");
				String expected = "Bob"; 
				String result = engine.evaluate("${name}", map, TemplateEngine.CASE_INSENSITIVE); 
				
				/* 	Expected: Bob
	  				  Result:  Bob 	*/
				
				assertEquals(result, expected);  
			}
			
			
			
			// More ^.
			@Test 
			public void firstReplacement() {
				map.store("name", "Adam");
				
				String expected = "Adam";
				String result = engine.evaluate("${name}", map, TemplateEngine.DEFAULT);
			
				assertEquals(result, expected); 
			}
			
	
	
	
	
	/* ------------------- MUTATION 4 TESTS ----------------------- */ 
			
			@Test
			public void longString_withSpaces() {
				
				String template = "This string is ${31 chars longgggggggggggggggggg}"; 
				
				map.store("31 chars longgggggggggggggggggg", "long" ); 
				
				Integer matchingMode = TemplateEngine.DEFAULT ;
				  
				String result = engine.evaluate(template, map, matchingMode);
				
				assertEquals("This string is long", result);
			
			}
			
			@Test 
			public void longString_withoutSpaces() {
				
				String template = "This string is ${31charslonggggggggggggggggggggg}"; 
				
				map.store("31charslonggggggggggggggggggggg", "long" ); 
				
				Integer matchingMode = TemplateEngine.DEFAULT ;
				  
				String result = engine.evaluate(template, map, matchingMode);
				
				assertEquals("This string is long", result);
			}

	
	/* ------------------- MUTATION 5 TESTS ----------------------- */ 
	
	

			// Spec2: After deleting a pair, other entries remain ordered 
				@Test
				public void entryDeleteSpec2_firstStaysOrderedx() {
					
					map.store("name", "Bob");
					map.store("NAME", "Adam");
					map.store("Name", "John");
				
					// Bob will be first, Adam will be second
					// Shows Bob remains in first place after deleting Adam
					
					map.delete("NAME");
					String expected = "Bob"; 
					String result = engine.evaluate("${name}", map, TemplateEngine.CASE_INSENSITIVE); 
					
					/*
					 * Expected: Bob
					 * Result: John
					*/
					
					assertEquals(result, expected);  
				}
				
				@Test
				public void entryDeleteSpec2_movesInOrderx() {
					
					map.store("name", "Bob");
					map.store("NAME", "Adam");
					map.store("Name", "John");
					
					//Shows when Bob is removed, Adam takes first place position so remains ordered
					map.delete("name");
					String expected = "Adam"; 
					String result = engine.evaluate("${name}", map, TemplateEngine.CASE_INSENSITIVE); 
					
					/*
					 * Expected: Adam
					   Result: John
					*/
								
					assertEquals(result, expected);  
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
					
					/* Expected: Order: Bob Bob
					   Result: Order: Adam Adam */ 

					assertEquals(expected, result);
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
					
					/*Expected: Hi Bob
					  Result: Hi Bobby
					*/
					
					assertEquals(result, expected);
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
						
						/* Expected: Hey Adam
						   Result: Hey Bob
						*/ 
						
						assertEquals(result, expected) ; 
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
						
						/*
						Expected: Hello Robert
						Result: Hello Adam
						*/ 

						assertEquals("Hello Robert", result); 
					
					}
					
					@Test 
					public void entryUpdateSpec3a() {
						
						map.store("name", "Bob"); 
						map.store("NAME", "Adam");
						
						Integer matchingMode = TemplateEngine.CASE_INSENSITIVE;
						
						// Shows Bob remains in first place after Adam's template is updated
						map.update("NAME", "John");
						String result2 = engine.evaluate("Hello ${Name}", map, matchingMode);
						
						/*
						Expected: Hello Robert
						Result2: Hello John
						*/
						System.out.println(result2); 
					
						assertEquals("Hello Bob", result2); 
					}
					
					@Test
					public void testStoreOverwriting() {
						
						map.store("name", "Adam");
						map.store("NAME", "Bob");
						map.store("Name", "John");
						
						Integer matchingMode = TemplateEngine.CASE_INSENSITIVE; 
						
						// John overwrites Adam
					
						String template = "${name}"; 
						String expected = "Adam"; 
						
						String result = engine.evaluate(template, map, matchingMode);
						
						/*
						 * Expected = "Adam"
						 * Result = "John"
						 */
						assertEquals(expected, result); 
					}

		
	
	/* ------------------- MUTATION 6 TESTS ----------------------- */ 
	
	
					@Test
					public void test_should_be_empty_is_a() {

						map.store("a", "MISTAKE");
						map.store("name", "Bob");
						map.store("surname", "Smith");

						Integer matchingMode = 0;

						String input = "${}";
						String expected = "${}";

						String result = engine.evaluate(input, map, matchingMode);

						// Expected: "${}"
						// Mutant gives: "MISTAKE"

						assertEquals(result, expected);
					}

	
	
	
	/* ------------------- MUTATION 7 TESTS ----------------------- */ 
	
	
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
					

	
	
	/* ------------------- MUTATION 8 TESTS ----------------------- */ 
	
	

					@Test
					public void sEngineSpec5CaseSensitive_CaseTest() { 
						
						String template = "Hi, my name is DAVID. David is my forename.";
						String pattern = "DAVID";
						String value = "Peter";
						Integer matchingMode = SimpleTemplateEngine.CASE_SENSITIVE;

						String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
						String expected = "Hi, my name is Peter. David is my forename.";

						// Expected: Hi, my name is Peter. David is my forename.
						// Result: Hi, my name is Peter. Peter is my forename.

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
						
						//Expected: Hi, my name is DAVID. Peter is my forename.
						//  Result: Hi, my name is Peter. Peter is my forename.

						assertEquals(result, expected);
					}
	

	
	
	
	/* ------------------- MUTATION 9 TESTS ----------------------- */ 
					
					
					
		
					
					
   /* ------------------- MUTATION 10 TESTS ----------------------- */ 
					
					
					@Test
					public void sEngine_Random3() {
						
						String template = "David";
						String pattern = "David";
						String value = "Peter";
						Integer matchingMode = SimpleTemplateEngine.WHOLE_WORLD_SEARCH;

						String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
						String expected = "Peter";
						
						// result = Petera
						
						assertEquals(result, expected);
					}
					
					@Test
					public void sEngine_Random() {

						String template = "Hello David";
						String pattern = "David";
						String value = "Peter";
						Integer matchingMode = SimpleTemplateEngine.WHOLE_WORLD_SEARCH;

						String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
						String expected = "Hello Peter";
						
						// result = Hello Petera
						
						assertEquals(result, expected);
					}
					
					@Test
					public void sEngineSpec4_HashFollowedByMultiDigit() {

						String template = "DavidDavidDavidDavidDavidDavidDavidDavidDavidDavidDavid";
						String pattern = "David#11";
						String value = "Peter";
						Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;

						String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
						String expected = "DavidDavidDavidDavidDavidDavidDavidDavidDavidDavidPeter";
						
						// result = DavidDavidDavidDavidDavidDavidDavidDavidDavidDavidPetera
						
						assertEquals(result, expected);
					}
					
					
					@Test 
					public void sEngine_testOneA() { 
						
						// An a is added only if replacement is the last item to be replaced 
						
						String template = "b";
						String pattern = "b";
						String value = "a";
						Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;

						String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
												
						assertEquals(result, "a");
					}

} // final
=======
import org.junit.Test;

public class Task2_Mutation {
	
	// Need to add all tests together here from other repo 

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
>>>>>>> 1d43c98029fa94ab3003c41fec31889b03c78e6b
