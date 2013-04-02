package com.bertuccigiuseppe.csvopenparser.test;

import static org.junit.Assert.fail;

import java.nio.file.Path;

import com.bertuccigiuseppe.csvopenparser.ICsvAcquisitionListener;

public class SampleBeanListener implements ICsvAcquisitionListener<SampleBean> {

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
