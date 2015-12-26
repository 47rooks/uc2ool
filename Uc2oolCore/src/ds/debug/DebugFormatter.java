/**
 * Copyright Daniel Semler 2015
 */
package ds.debug;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * DebugFormatter provides a very simple log format where a single record is
 * logged to a single trace log line.
 * 
 * @author      Daniel Semler
 * @version     %I%, %G%
 * @since       1.0
 */
public class DebugFormatter extends Formatter {

    private final static SimpleDateFormat s_tmFormat =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    
    @Override
    public String format(LogRecord r) {
        StringBuilder sb = new StringBuilder();
        sb.append(s_tmFormat.format(new Date(r.getMillis()))).append(" ");
        sb.append(r.getSourceClassName()).append(".");
        sb.append(r.getSourceMethodName()).append(" ");
        sb.append(r.getLevel()).append(" ").append(r.getMessage());
        sb.append("\n");
        return sb.toString();
    }

}
