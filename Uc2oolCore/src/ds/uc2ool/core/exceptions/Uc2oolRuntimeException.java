/**
 * Copyright Daniel Semler 2015
 */
package ds.uc2ool.core.exceptions;

import java.util.ArrayList;
import java.util.List;

import ds.errors.BaseError;

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
    
    private final BaseError m_error;
    
    /*
     * Constructor taking message key and arguments to be substituted
     * 
     * @param msgKey the resource bundle message key for the message
     * @param arguments required by the message substitution string
     */
    public Uc2oolRuntimeException(BaseError baseError) {
        m_error = baseError;
    }
    
    @Override
    public String getLocalizedMessage() {
        List<String> l = new ArrayList<String>();
        l.add(m_error.getError());
        l.add(m_error.getReason());
        l.add(m_error.getResponse());
        StringBuilder sb = new StringBuilder();
        l.forEach((elt)->{
            if (elt != null && elt.length() > 0) sb.append(elt).append("\n");
        });
        return sb.substring(0, sb.lastIndexOf("\n"));
    }
}
