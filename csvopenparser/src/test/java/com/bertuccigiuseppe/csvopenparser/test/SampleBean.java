package com.bertuccigiuseppe.csvopenparser.test;

import com.bertuccigiuseppe.csvopenparser.CsvOpenparserMapping;
import com.bertuccigiuseppe.csvopenparser.CsvParser;

public class SampleBean {

    private String A;
    private String B;
    private String C;
    private String D;
    private String E;

    public String getA() {
	return A;
    }

    @CsvOpenparserMapping(fieldName="A-FIELD")
    public void setA(String a) {
	A = a;
    }

    public String getB() {
	return B;
    }

    @CsvOpenparserMapping(fieldName="B-FIELD", fieldEreg = CsvParser.NUMBER)
    public void setB(String b) {
	B = b;
    }

    public String getC() {
	return C;
    }

    @CsvOpenparserMapping(fieldName="C-FIELD")
    public void setC(String c) {
	C = c;
    }

    public String getD() {
	return D;
    }

    @CsvOpenparserMapping(fieldName="D-FIELD")
    public void setD(String d) {
	D = d;
    }

    public String getE() {
	return E;
    }

    @CsvOpenparserMapping(fieldName="E-FIELD")
    public void setE(String e) {
	E = e;
    }
}
