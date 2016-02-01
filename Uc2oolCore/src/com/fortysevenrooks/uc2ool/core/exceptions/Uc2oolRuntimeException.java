/**
 * Copyright Daniel Semler 2015
 */
package com.fortysevenrooks.uc2ool.core.exceptions;

import com.fortysevenrooks.errors.BaseError;

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
        return m_error.getLocalizedMessage();
    }
    
    /**
     * Get the error object from the exception.
     * 
     * @return the error object
     */
    public BaseError getError() {
        return m_error;
    }
}
