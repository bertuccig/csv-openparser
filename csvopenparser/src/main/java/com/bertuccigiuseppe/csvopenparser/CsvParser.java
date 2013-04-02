/**
 * 
 */
package com.bertuccigiuseppe.csvopenparser;

import java.io.BufferedReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Giuseppe Bertucci <bertuccig@gmail.com>
 * @licence this software was released under the term of GPL (GNU PUBLIC
 *          LICENSE) v3. See the gpl-3.0.txt file for further information.
 */
public class CsvParser<T> {

    // private static final Logger log =
    // LoggerFactory.getLogger(CsvParser.class);

    private Map<Integer, String> fieldPositions = new HashMap<>();
    private static final String AUTO_GROUP_NAME_PREFIX = "FIELD_";

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

    private T parseRow(String line, Class<T> aClass) throws BadlyFormattedLineException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {

	Matcher m = rowPattern.matcher(line);
	T clientBean = null;

	if (m.matches()) {

	    clientBean = aClass.newInstance();

	    Method[] methods = aClass.getMethods();
	    for (Method method : methods) {
		if (method.isAnnotationPresent(CsvOpenparserMapping.class)) {
		    CsvOpenparserMapping mappingAnnotation = method.getAnnotation(CsvOpenparserMapping.class);

		    String CsvFieldName = "";
		    
		    //if is not been setted the field name, we use the field position
		    if (CsvParser.DEFAULT_FIELD_NAME.equals(mappingAnnotation.fieldName())) {
			
			String fieldname = fieldPositions.get(mappingAnnotation.fieldPosition());
			
			CsvFieldName = CsvParser.CleanUpFieldName(fieldname);
		    
		    } else {
			// if is not been setted the field position we throw an exception
			//FIXME check the filed position field
			CsvFieldName = CsvParser.CleanUpFieldName(mappingAnnotation.fieldName());
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
}
