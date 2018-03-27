import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import st.EntryMap;
import st.SimpleTemplateEngine;
import st.TemplateEngine;

public class Task2_TDD_1 {

	private EntryMap map;
	private TemplateEngine engine;
	private SimpleTemplateEngine simpleEngine;
	private int year;

	@Before
	public void setUp() throws Exception {
		map = new EntryMap();
		engine = new TemplateEngine();
		simpleEngine = new SimpleTemplateEngine();
		year = Calendar.getInstance().get(Calendar.YEAR);
	}

	@Rule
	public ExpectedException thrown = ExpectedException.none();


	/*-------------------- Tests special treatment when X is a valid number --------------------*/
	
	
	@Test 
	public void works_YearsAgo() {
		
		map.store("year", "2 years ago");
		int yearVal = year - 2; 
		String template = "I was born in ${year}";
		Integer matchingMode = TemplateEngine.DEFAULT;
		
		String expected = "I was born in 2016"; 
		String result = engine.evaluate(template, map, matchingMode); 
		
		// System.out.println(expected + "\n" + result); 
		
		assertEquals(expected, result); 
		
	}
	
	@Test 
	public void works_InXYears() {
		
		map.store("year", "in 2 years");
		int yearVal = year + 2; 
		String template = "I was born in ${year}";
		
		Integer matchingMode = TemplateEngine.DEFAULT;
		
		String expected = "I was born in 2020";
		String result = engine.evaluate(template, map, matchingMode); 
		
		// System.out.println(expected + "\n" + result); 
		
		assertEquals(expected, result); 
	}
	
	@Test 
	public void works_defaultMatching() {
		map.store("year", "");
	}
	
	@Test 
	public void works_accurateSarchMatching() {
		map.store("year", "");
		
	}
	
	@Test 
	public void works_blurSearchMatching() {
		map.store("year", "");
		
	}
	@Test 
	public void works_caseSensitiveMatching() {
		map.store("year", "");
		
	}
	@Test 
	public void works_caseInsensitiveMatching() {
		map.store("year", "");
		
	}

	
	/*-------------------- Tests when X is negative or not a number then no special treatment --------------------*/
	
	
	@Test
	public void x_isNegative_inXyears() {
		
		map.store("year", "in -2 years");

		String template = "I was born in ${year}";
		Integer matchingMode = TemplateEngine.DEFAULT;
		
		String expected = "I was born in in -2 years";
		String result = engine.evaluate(template, map, matchingMode); 
		
		// System.out.println(expected + "\n" + result); 
		
		assertEquals(result, expected); 
	}
	
	@Test
	public void x_isNegative_yearsAgo() {
		
		map.store("year", "-2 years ago");
		
		String template = "I was born in ${year}";
		Integer matchingMode = TemplateEngine.DEFAULT;
		
		String expected = "I was born in -2 years ago";
		String result = engine.evaluate(template, map, matchingMode); 
		
		// System.out.println(expected + "\n" + result); 
		
		assertEquals(result, expected); 
		
	}
	
	@Test 
	public void x_isNotNumber_inXyears() {
		
		map.store("year", "in two years");
		
		String template = "I was born in ${year}";
		Integer matchingMode = TemplateEngine.DEFAULT;
		
		String expected = "I was born in in two years";
		String result = engine.evaluate(template, map, matchingMode); 
		
		// System.out.println(expected + "\n" + result); 
		
		assertEquals(result, expected); 
		
	}
	@Test 
	public void x_isNotNumber_yearsAgo() {
		
		map.store("year", "two years ago");
		String template = "I was born in ${year}";
		Integer matchingMode = TemplateEngine.DEFAULT;
		
		String expected = "I was born in two years ago";
		String result = engine.evaluate(template, map, matchingMode); 
		
		System.out.println(expected + "\n" + result); 
		
		assertEquals(result, expected); 
	}	
	@Test
	public void x_isSpace_inXyears() {
		
		map.store("year", "in   years");
		String template = "I was born in ${year}";
		Integer matchingMode = TemplateEngine.DEFAULT;
		
		String expected = "I was born in in   years";
		String result = engine.evaluate(template, map, matchingMode); 
		
		// System.out.println(expected + "\n" + result); 
		
		assertEquals(result, expected); 
		
	}
	
	@Test 
	public void x_isSpace_yearsAgo() {
		
		map.store("year", "  years ago");
		String template = "I was born in ${year}";
		Integer matchingMode = TemplateEngine.DEFAULT;
		
		String expected = "I was born in   years ago";
		String result = engine.evaluate(template, map, matchingMode); 
		
		// System.out.println(expected + "\n" + result); 
		
		assertEquals(result, expected); 
		
	}

	@Test
	public void two_values_forX_inXyears() {
		
		map.store("year", "in 2 3 years");
		String template = "I was born in ${year}";
		Integer matchingMode = TemplateEngine.DEFAULT;
		
		String expected = "I was born in in 2 3 years";
		String result = engine.evaluate(template, map, matchingMode); 
		
		// System.out.println(expected + "\n" + result); 
		
		assertEquals(result, expected); 
	}
	
	@Test 
	public void two_values_forX_yearsAgo() {
		
		map.store("year", "2 3 years ago");
		String template = "I was born in ${year}";
		Integer matchingMode = TemplateEngine.DEFAULT;
		
		String expected = "I was born in 2 3 years ago";
		String result = engine.evaluate(template, map, matchingMode); 
		
		// System.out.println(expected + "\n" + result); 
		
		assertEquals(result, expected); 
		
	}
	
	
	/*-------------------- Tests when X is zero then return current year --------------------*/


	@Test
	public void x_isZero_yearsAgo() {
		
		map.store("year", "0 years ago");
		String template = "I was born in ${year}";
		Integer matchingMode = TemplateEngine.DEFAULT;
		
		String expected = "I was born in 2018";
		String result = engine.evaluate(template, map, matchingMode); 
		
		// System.out.println(expected + "\n" + result); 
		
		assertEquals(result, expected); 
	
	
	}
	
	@Test
	public void x_isZero_inXYears() {
		
		map.store("year", "in 0 years");
		String template = "I was born in ${year}";
		Integer matchingMode = TemplateEngine.DEFAULT;
		
		String expected = "I was born in 2018";
		String result = engine.evaluate(template, map, matchingMode); 
		
	    // System.out.println(expected + "\n" + result); 
		
		assertEquals(result, expected); 
	}

	@Test
	public void x_hasMultipleZeros() {
		
		map.store("year", "in 000 years");
		String template = "I was born in ${year}";
		Integer matchingMode = TemplateEngine.DEFAULT;
		
		String expected = "I was born in 2018";
		String result = engine.evaluate(template, map, matchingMode); 
		
		// System.out.println(expected + "\n" + result); 
		
		assertEquals(result, expected); 
		
	}
	
	@Test 
	public void x_hasLeadingZeros_inXyears() {
		
		map.store("year", "in 001 years");
		String template = "I was born in ${year}";
		Integer matchingMode = TemplateEngine.DEFAULT;
		
		String expected = "I was born in 2019";
		String result = engine.evaluate(template, map, matchingMode); 
		
	    // System.out.println(expected + "\n" + result); 
		
		assertEquals(result, expected); 
		
	}
	
	@Test 
	public void x_hasLeadingZeros_yearsAgo() {
		
		map.store("year", "001 years ago");
		String template = "I was born in ${year}";
		Integer matchingMode = TemplateEngine.DEFAULT;
		
		String expected = "I was born in 2017";
		String result = engine.evaluate(template, map, matchingMode); 
		
	    // System.out.println(expected + "\n" + result); 
		
		assertEquals(result, expected); 
		
	}
	
	/*----------------------- Tests when entryMap class contains base_year  ----------------------*/

	
	@Test 
	public void baseyear_valid_YearsAgo() {
		
		map.store("base_year", "1990");
		map.store("year", "2 years ago"); 
		
		String template = "I was born in ${year} ${base_year}";
		Integer matchingMode = TemplateEngine.DEFAULT;
		
		String expected = "I was born in 1988 1990";
		String result = engine.evaluate(template, map, matchingMode); 
		
	    // System.out.println(expected + "\n" + result); 
		
		assertEquals(result, expected); 
		
	}
	
	@Test
	public void baseyear_valid_inXyears() {
		
		map.store("base_year", "1990"); 
		map.store("year", "in 6 years"); 
		
		String template = "I was born in ${year} ${base_year}";
		Integer matchingMode = TemplateEngine.DEFAULT;
		
		String expected = "I was born in 1996 1990";
		String result = engine.evaluate(template, map, matchingMode); 
		
	    // System.out.println(expected + "\n" + result); 
		
		assertEquals(result, expected); 
		
		
	}
	@Test (expected = RuntimeException.class)
	public void baseyear_invalid_null() {
		
		map.store("base_year", null);
		map.store("year", "in 6 years"); 
		
		String template = "I was born in ${year} ${base_year}";
		Integer matchingMode = TemplateEngine.DEFAULT;
		
		String expected = "I was born in 1996 1990";
		String result = engine.evaluate(template, map, matchingMode); 
		
	    System.out.println(expected + "\n" + result); 
		
		assertEquals(result, expected); 
	}
	
	@Test 
	public void baseyear_invalid_empty() {
		
		map.store("base_year", "");
	}
	
	@Test 
	public void baseyear_invalid_negative() {
		
		map.store("base_year", "-2");
		
	}
	
	@Test 
	public void baseyear_invalid_notNumber() {
		
		map.store("base_year", "two thousand");
	}
	
	@Test 
	public void baseyear_invalid_numberandLetters() {
		
		map.store("base_year", "2 thousand");
	}
	
	
	

	/*----------------------- Tests base year is there but X is invalid ----------------------*/

}