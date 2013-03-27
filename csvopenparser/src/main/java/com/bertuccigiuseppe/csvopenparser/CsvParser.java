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
 *          LICENSE) v3
 */
public class CsvParser<T> {

    // private static final Logger log =
    // LoggerFactory.getLogger(CsvParser.class);

    private Map<String, Integer> fieldPositions = new HashMap<>();
    private static final String AUTO_GROUP_NAME_PREFIX = "FIELD_";

    // FIXME improve the constant definition
    public static final String NUMBER = "[0-9]+";
//    public static final String EMAIL = "[0-9]+";
    public static final String ALPHABETIC = "[a-zA-Z]+";
    public static final String ALPHANUMERIC = "[0-9a-zA-Z.,\\s]+";
    public static final String DECIMAL = "[0-9]+[.].[0-9]+";
//    public static final String ANY = "";
//    public static final String CUSTOM = "";

    private boolean skipFirstLine = false;
    private boolean autoCompile = true;
    private char fieldSeparator = ';';
    private Charset charset = Charset.forName("UTF-8");

    public void parse(Path entry, ICsvAcquisitionListener<T> eventListener, Class<T> aClass) throws LineParserNotAlreadyCompiledException {

	if (rowPattern == null)
	    throw new LineParserNotAlreadyCompiledException();

	try (BufferedReader reader = Files.newBufferedReader(entry, charset)) {

	    String line = null;

	    // skip the header, if requested
	    if (autoCompile)
		compileEreg(reader.readLine(), aClass);
	    else if (this.skipFirstLine)
		reader.readLine();

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
    private void compileEreg(String readLine, Class<T> aClass) {

	Method[] methods = aClass.getMethods();
	for (Method method : methods) {
	    if (method.isAnnotationPresent(CsvOpenparserMapping.class)) {
		CsvOpenparserMapping mappingAnnotation = method.getAnnotation(CsvOpenparserMapping.class);

//		mappingAnnotation.
		// put the annotation in the map

	    }
	}

	String[] fields = readLine.split("" + this.fieldSeparator);
	StringBuilder sb = new StringBuilder();

	for (int i = 0; i < fields.length; i++) {
	    String field = fields[i];

	    fieldPositions.put(field, i);
	}

    }

    private static Pattern rowPattern = null;

    public void compile() {
	rowPattern = Pattern.compile("");
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

		    String val = m.group(mappingAnnotation.fieldName());

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
}
