/**
 * 
 */
package com.bertuccigiuseppe.csvopenparser;

import java.io.BufferedReader;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Giuseppe Bertucci <bertuccig@gmail.com>
 * @licence this software was released under the term of GPL (GNU PUBLIC
 *          LICENSE) v3. See the gpl-3.0.txt file for further information.
 */
// TODO use an internal class for implementing ICsvAcquisitionListener for
// avoiding improper use
public class CsvParser<T> implements ICsvAcquisitionListener<T> {

    private Map<Integer, String> fieldPositions = new HashMap<>();

    // FIXME improve the constant definition
    public static final String NUMBER = "[0-9]+";
    // public static final String EMAIL = "[0-9]+";
    public static final String ALPHABETIC = "[a-zA-Z]+";
    public static final String ALPHANUMERIC = "[0-9a-zA-Z.,\\s]+";
    public static final String DECIMAL = "[0-9]+[.,].[0-9]+";
    // public static final String ANY = "";
    // public static final String CUSTOM = "";
    public static final String DEFAULT_FIELD_NAME = "CSVOPENPARSER-NOT-SET";
    public static final int DEFAULT_FIELD_POSITION = -1;

    private boolean skipFirstLine = false;
    private boolean autoCompile = true;
    private char fieldSeparator = ';';
    private String template = null;
    private static Pattern rowPattern = null;
    private Charset charset = Charset.forName("UTF-8");

    public void parse(Path entry, ICsvAcquisitionListener<T> eventListener, Class<T> aClass) throws LineParserNotAlreadyCompiledException {

	try (BufferedReader reader = Files.newBufferedReader(entry, charset)) {

	    String line = null;

	    if (autoCompile) {

		if (null == template)
		    template = reader.readLine();

		compileEreg(template, aClass);

	    } else if (this.skipFirstLine) {
		reader.readLine();
	    }

	    while ((line = reader.readLine()) != null) {
		try {

		    // fire the first event: row just read
		    eventListener.onLineRead(line);

		    T r = this.parseRow(line, aClass);

		    // fire the second event: line correctly parsed
		    eventListener.onLineParsed(r);

		} catch (BadlyFormattedLineException x) {
		    System.err.format("BadlyFormattedRankException: %s%n", x);

		    eventListener.onLineParsingError(line, "error during the row validation");
		}
	    }

	    // fire the last event: end of the file parsing
	    eventListener.onParsingFinished(entry);

	} catch (Exception x) {
	    System.err.format("IOException: %s%n", x);
	}

    }

    // TODO completare la generazione automatica di espressione regolare basata
    // su intestazione file / annotazioni
    private void compileEreg(String template, Class<T> aClass) {

	String[] fields = template.split("" + this.fieldSeparator);
	StringBuilder sb = new StringBuilder();

	for (int i = 0; i < fields.length; i++) {
	    String field = fields[i];

	    sb.append(String.format("(?<%s>%s)", CsvParser.CleanUpFieldName(field), ALPHANUMERIC));

	    if (i != fields.length - 1)
		sb.append(fieldSeparator);

	    fieldPositions.put(i + 1, field);
	}

	String lineEreg = sb.toString();
	rowPattern = Pattern.compile(lineEreg);

	// Method[] methods = aClass.getMethods();
	// for (Method method : methods) {
	// if (method.isAnnotationPresent(CsvOpenparserMapping.class)) {
	// CsvOpenparserMapping mappingAnnotation =
	// method.getAnnotation(CsvOpenparserMapping.class);
	//
	// // mappingAnnotation.
	// // put the annotation in the map
	//
	// }
	// }

    }

    private T parseRow(String line, Class<T> aClass) throws Exception {

	Matcher m = rowPattern.matcher(line);
	T clientBean = null;

	if (m.matches()) {

	    clientBean = aClass.newInstance();

	    Method[] methods = aClass.getMethods();
	    for (Method method : methods) {
		if (method.isAnnotationPresent(CsvOpenparserMapping.class)) {
		    CsvOpenparserMapping mappingAnnotation = method.getAnnotation(CsvOpenparserMapping.class);

		    String CsvFieldName = "";

		    // if is not been setted the field name, we use the field
		    // position
		    if (!CsvParser.DEFAULT_FIELD_NAME.equals(mappingAnnotation.fieldName())) {

			CsvFieldName = CsvParser.CleanUpFieldName(mappingAnnotation.fieldName());

		    } else {
			// if is not been setted the field position we throw an
			// exception
			if (CsvParser.DEFAULT_FIELD_POSITION != mappingAnnotation.fieldPosition()) {
			    String fieldname = fieldPositions.get(mappingAnnotation.fieldPosition());

			    CsvFieldName = CsvParser.CleanUpFieldName(fieldname);
			} else {

			    throw new Exception(String.format("missing fieldName or fieldPosition for the field %s", method.getName()));
			}
		    }

		    String val = m.group(CsvFieldName);

		    method.invoke(clientBean, val);
		}
	    }

	} else {

	    throw new BadlyFormattedLineException();
	}

	return clientBean;
    }

    public char getFieldSeparator() {
	return fieldSeparator;
    }

    public void setFieldSeparator(char fieldSeparator) {
	this.fieldSeparator = fieldSeparator;
    }

    public boolean isSkipFirstLine() {
	return skipFirstLine;
    }

    public void setSkipFirstLine(boolean skipFirstLine) {
	this.skipFirstLine = skipFirstLine;
    }

    public boolean isAutoCompile() {
	return autoCompile;
    }

    public void setAutoCompile(boolean autoCompile) {
	this.autoCompile = autoCompile;
    }

    public Charset getCharset() {
	return charset;
    }

    public void setCharset(Charset charset) {
	this.charset = charset;
    }

    // FIXME create ereg-group-accepted name for the field
    private static String CleanUpFieldName(String fieldName) {
	return fieldName;
    }

    List<T> entries = new ArrayList<>();

    public List<T> parseAll(Path entry, Class<T> aClass) throws LineParserNotAlreadyCompiledException {

	this.parse(entry, this, aClass);

	return entries;
    }

    @Override
    public void onLineRead(String line) {

	// TODO log occurrences
    }

    @Override
    public void onLineParsed(T bean) {

	// TODO log occurrences
	entries.add(bean);
    }

    @Override
    public void onLineParsingError(String errorLine, String errorDescription) {
	// TODO log occurrences
	// TODO create data structure for managing both the lists: ok and error
	// lines
    }

    @Override
    public void onParsingFinished(Path entry) {

	// TODO log occurrences
    }

    public void setTemplate(String string) {

	this.template = "AFIELD;BFIELD;CFIELD;DFIELD;EFIELD";
    }
}
