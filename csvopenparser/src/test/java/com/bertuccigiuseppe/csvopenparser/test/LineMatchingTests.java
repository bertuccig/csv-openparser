package com.bertuccigiuseppe.csvopenparser.test;

import static org.junit.Assert.fail;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.BeforeClass;
import org.junit.Test;

import com.bertuccigiuseppe.csvopenparser.CsvParser;
import com.bertuccigiuseppe.csvopenparser.ICsvAcquisitionListener;
import com.bertuccigiuseppe.csvopenparser.LineParserNotAlreadyCompiledException;

public class LineMatchingTests implements ICsvAcquisitionListener<SampleBean> {

    private static CsvParser<SampleBean> parser = null;
    private static Path entry = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

	URL uri = LineMatchingTests.class.getResource("/CsvOpenparserSample.csv");
	entry = Paths.get(uri.getPath().substring(1));
	
	parser = new CsvParser<>();
    }

    @Test
    public void test() {

	try {
	    
	    parser.parse(entry, this, SampleBean.class);
	    
	} catch (LineParserNotAlreadyCompiledException e) {
	    
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    
	    fail("an error occurred: ");
	}
	
    }

    @Override
    public void onLineRead(String line) {

	System.out.println("acquiring line: " + line);

    }

    @Override
    public void onLineParsed(SampleBean bean) {

	System.out.println(String.format("acquired bean: %s - %s - %s - %s - %s", new Object[] { bean.getA(), bean.getB(), bean.getC(), bean.getD(), bean.getE() }));
    }

    @Override
    public void onLineParsingError(String errorLine, String errorDescription) {

	fail("An error occurred during the parsing");
    }

    @Override
    public void onParsingFinished(Path entry) {

	System.out.println("parsing finished");
    }

}
