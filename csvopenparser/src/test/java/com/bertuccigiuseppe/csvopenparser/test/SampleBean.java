package com.bertuccigiuseppe.csvopenparser.test;

import com.bertuccigiuseppe.csvopenparser.CsvOpenparserMapping;

public class SampleBean {

    private String A = new String();
    private String B = new String();
    private String C = new String();
    private String D = new String();
    private String E = new String();

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

    @CsvOpenparserMapping(fieldPosition=2)
    public void setB(String b) {
	B = b;
    }

    public String getC() {
	return C;
    }

    @CsvOpenparserMapping(fieldPosition=2)
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
