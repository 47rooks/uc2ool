/**
 * Copyright Daniel Semler 2015
 */
package ds.debug;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A DebugLogger is a simple logger which may be created with a
 * <code>java.util.logging.Logger</code> with a FileHandler or a with a
 * handler provided by the caller.
 * 
 * The FileHandler is configured for log rotation. Currently barring the
 * destination and logging level filename all parameters are hardwired,
 * The logging must be passed as a system property on the VM command line.
 * The property name is ds.debug.logging.level and the levels are regular 
 * <code>java.util.logging.Level</code> levels.
 * 
 * @author  Daniel Semler
 * @version %I%, %G%
 * @since   1.0
 */
public class DebugLogger extends Logger {

    private static final int MAX_FILE_SIZE = 1024*1024;
    private static final int FILE_COUNT = 10;
    private static final boolean APPEND = true;
    private static final String DEBUG_LOG_LEVEL = "ds.debug.logging.level";
    private static final String DEFAULT_LOG_LEVEL = "INFO";
    
    /**
     * Create a logger with a FileHandler logging to the specified file.
     * 
     * @param name the logger name
     * @param destFile the destination file specification for a
     * <code>java.util.logging.FileHandler</code>.
     * @throws IOException if there is a difficulty creating the FileHandler.
     */
    public DebugLogger(String name, String destFile)
    throws IOException
    {
        super(name, null);

        setLevel(getLevelFromSystemProperty());
        FileHandler fh = new FileHandler(destFile,
                                   MAX_FILE_SIZE,
                                   FILE_COUNT,
                                   APPEND);

        fh.setFormatter(new DebugFormatter());
        addHandler(fh);
    }
    
    /**
     * Create a debug logging with the specified handler, logging at the
     * specified level.
     * 
     * @param name the logger name
     * @param hdl the <code>java.util.logging.Handler</code> to use
     */
    public DebugLogger(String name, Handler hdl) {
        super(name, null);
        setLevel(getLevelFromSystemProperty());
        addHandler(hdl);
    }
    
    private Level getLevelFromSystemProperty() {
        Level level;
        try {
            level = Level.parse(System.getProperty(DEBUG_LOG_LEVEL,
                                                   DEFAULT_LOG_LEVEL));
        } catch (IllegalArgumentException iae) {
            level = Level.parse(DEFAULT_LOG_LEVEL);
        }
        return level;
    }
}
