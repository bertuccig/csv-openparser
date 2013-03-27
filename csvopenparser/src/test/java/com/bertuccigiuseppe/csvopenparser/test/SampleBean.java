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

    @CsvOpenparserMapping(fieldName="AFIELD")
    public void setA(String a) {
	A = a;
    }

    public String getB() {
	return B;
    }

    @CsvOpenparserMapping(fieldName="BFIELD", fieldEreg = CsvParser.NUMBER)
    public void setB(String b) {
	B = b;
    }

    public String getC() {
	return C;
    }

    @CsvOpenparserMapping(fieldName="CFIELD")
    public void setC(String c) {
	C = c;
    }

    public String getD() {
	return D;
    }

    @CsvOpenparserMapping(fieldName="DFIELD")
    public void setD(String d) {
	D = d;
    }

    public String getE() {
	return E;
    }

    @CsvOpenparserMapping(fieldName="EFIELD")
    public void setE(String e) {
	E = e;
    }
}
