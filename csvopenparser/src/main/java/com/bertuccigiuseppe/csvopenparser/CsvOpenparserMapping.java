package com.bertuccigiuseppe.csvopenparser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Giuseppe Bertucci <bertuccig@gmail.com>
 * @licence this software was released under the term of GPL (GNU PUBLIC LICENSE) v3
 */

@Target( ElementType.METHOD )
@Retention(RetentionPolicy.RUNTIME)
public @interface CsvOpenparserMapping {

    String fieldName() default "CSVOPENPARSER-NOT-SET"; //FIXME create static constant field in CsvParser
    int fieldPosition() default -1; //FIXME create static constant field in CsvParser
    String fieldEreg() default CsvParser.ALPHANUMERIC;
}
 