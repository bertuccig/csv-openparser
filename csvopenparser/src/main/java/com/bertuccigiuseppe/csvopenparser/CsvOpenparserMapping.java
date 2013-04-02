package com.bertuccigiuseppe.csvopenparser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Giuseppe Bertucci <bertuccig@gmail.com>
 * @licence this software was released under the term of GPL (GNU PUBLIC LICENSE) v3. See the gpl-3.0.txt file for further information.
 */
@Target( ElementType.METHOD )
@Retention(RetentionPolicy.RUNTIME)
public @interface CsvOpenparserMapping {

    String fieldName() default CsvParser.DEFAULT_FIELD_NAME;
    int fieldPosition() default CsvParser.DEFAULT_FIELD_POSITION; 
    String fieldEreg() default CsvParser.ALPHANUMERIC;
}
 