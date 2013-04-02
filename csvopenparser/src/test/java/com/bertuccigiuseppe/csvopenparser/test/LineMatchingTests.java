package com.bertuccigiuseppe.csvopenparser.test;

import static org.junit.Assert.fail;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.bertuccigiuseppe.csvopenparser.CsvParser;
import com.bertuccigiuseppe.csvopenparser.LineParserNotAlreadyCompiledException;

public class LineMatchingTests  {

    private static CsvParser<SampleBean> parser = null;
    private static Path entry = null;
    private static SampleBeanListener listener = new SampleBeanListener();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

	URL uri = LineMatchingTests.class.getResource("/CsvOpenparserSample.csv");
	entry = Paths.get(uri.getPath().substring(1));

	parser = new CsvParser<>();
    }

    @Test
    public void listenerTest() {

	System.out.println("-------------------------------------------");
	System.out.println("-             LISTENER TEST               -");
	System.out.println("-------------------------------------------");

	try {

	    parser.parse(entry, listener, SampleBean.class);
	    
	} catch (LineParserNotAlreadyCompiledException e) {

	    e.printStackTrace();

	    fail("an error occurred: ");
	} catch (Exception ex) {
	    ex.printStackTrace();

	    fail("an error occurred: ");
	}

    }

    @Test
    public void serializedTest() {

	// BE CAREFUL using this approach: with very big csv you can occupy huge memory and OutOfMemory may occur!!!

	System.out.println("-------------------------------------------");
	System.out.println("-             SERIALIZED TEST             -");
	System.out.println("-------------------------------------------");
	
	try {

	    List<SampleBean> parsedLines = parser.parseAll(entry, SampleBean.class);

	    for(SampleBean bean : parsedLines)
	    {
		System.out.println(String.format("acquired bean: %s - %s - %s - %s - %s", new Object[] { bean.getA(), bean.getB(), bean.getC(), bean.getD(), bean.getE() }));		
	    }
	    
	} catch (LineParserNotAlreadyCompiledException e) {

	    e.printStackTrace();

	    fail("an error occurred: ");
	} catch (Exception ex) {
	    ex.printStackTrace();

	    fail("an error occurred: ");
	}

    }

    @Test
    public void exernalTemplateTest() {

	// BE CAREFUL using this approach: with very big csv you can occupy huge memory and OutOfMemory may occur!!!
	System.out.println("-------------------------------------------");
	System.out.println("-             EXTERNAL TEMPLATE TEST      -");
	System.out.println("-------------------------------------------");
	
	try {

		URL uri = LineMatchingTests.class.getResource("/CsvOpenparserSampleNoHeader.csv");
		entry = Paths.get(uri.getPath().substring(1));

		CsvParser<SampleBean> parser2 = new CsvParser<>();

	    parser2.setTemplate("AFIELD;BFIELD;CFIELD;DFIELD;EFIELD");
	    List<SampleBean> parsedLines = parser2.parseAll(entry, SampleBean.class);

	    for(SampleBean bean : parsedLines)
	    {
		System.out.println(String.format("acquired bean: %s - %s - %s - %s - %s", new Object[] { bean.getA(), bean.getB(), bean.getC(), bean.getD(), bean.getE() }));		
	    }
	    
	} catch (LineParserNotAlreadyCompiledException e) {

	    e.printStackTrace();

	    fail("an error occurred: ");
	} catch (Exception ex) {
	    ex.printStackTrace();

	    fail("an error occurred: ");
	}

    }
}
