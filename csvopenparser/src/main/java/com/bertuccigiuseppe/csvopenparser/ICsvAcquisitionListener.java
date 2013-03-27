package com.bertuccigiuseppe.csvopenparser;

import java.nio.file.Path;

/**
 * @author Giuseppe Bertucci <bertuccig@gmail.com>
 * @licence this software was released under the term of GPL (GNU PUBLIC LICENSE) v3. See the LICENSE.txt for further information.
 */
public interface ICsvAcquisitionListener<T> {

    void onLineRead(String line);
    void onLineParsed(T bean);
    void onLineParsingError(String errorLine, String errorDescription);
    void onParsingFinished(Path entry);

}
