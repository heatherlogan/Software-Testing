import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import st.EntryMap;
import st.SimpleTemplateEngine;
import st.TemplateEngine;

public class Task2_TDD_2 {

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
	
	
	/*********** Tests that show ${year} is treated specially if value is "in 6 years" or "in X years" ***************/
	
	
	
	
	
	
	
}
