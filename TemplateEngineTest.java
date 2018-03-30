import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import st.EntryMap;
import st.TemplateEngine;
import st.SimpleTemplateEngine;

public class TemplateEngineTest {

    private EntryMap map;

    private TemplateEngine engine;
    
    private SimpleTemplateEngine simpleEngine;
    
    @Before
    public void setUp() throws Exception {
        map = new EntryMap();
        engine = new TemplateEngine();
        simpleEngine = new SimpleTemplateEngine();
    }
    
    @Test
    public void Test1() {
    	
    	map.store("year", "2 years ago");
    	map.store("base_year", "1990");
    	
    	String template = "${YEAR} ${base _ year}"; 
    	
    	String result = engine. evaluate(template, map, TemplateEngine.BLUR_SEARCH); 
    	
    	System.out.println(result); 
    	
    	
    }
    

    
}