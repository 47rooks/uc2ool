/**
 * Copyright Daniel Semler 2015
 */
package ds.uc2ool.core.exceptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Uc2oolRuntimeException is the base class of all RuntimeExceptions
 * used in the calculator. The reason runtime exceptions are used is so that
 * one does not have to declare thrown exceptions all over the code. At the
 * top level in the MVC controller all methods with use a common exception
 * handling mechanism which will catch this exception and process it as
 * required.
 * 
 * @author  Daniel Semler
 * @version %I%, %G%
 * @since   1.0
 */
public class Uc2oolRuntimeException extends RuntimeException {
    
    /**
     * First version
     */
    private static final long serialVersionUID = 1L;
    
    private final String m_msgKey;
    private final List<Object> m_args;
    private final static String RESOURCE_BUNDLE_NAME =
            "ds.uc2ool.core.resources.Messages";
    
    /*
     * Constructor taking message key and arguments to be substituted
     * 
     * @param msgKey the resource bundle message key for the message
     * @param arguments required by the message substitution string
     */
    public Uc2oolRuntimeException(String msgKey, Object... args) {
        m_msgKey = msgKey;
        m_args = new ArrayList<Object>(Arrays.asList(args));
    }
    
    @Override
    public String getLocalizedMessage() {
        ResourceBundle mb = ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME);
        String msg = mb.getString(m_msgKey);
        return new StringBuilder(
                String.format(msg, m_args.toArray())).toString();
    }
}
