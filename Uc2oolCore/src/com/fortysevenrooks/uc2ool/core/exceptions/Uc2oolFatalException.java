/**
 * Copyright Daniel Semler 2015
 */
package com.fortysevenrooks.uc2ool.core.exceptions;

import com.fortysevenrooks.errors.BaseError;

/**
 * Objects of this class represent a fatal exception in Uc2ool. This usually
 * means that it is a bug or at the very least development assistance will be
 * required to debug the problem. It should be used rarely and with care.
 * 
 * @author  Daniel Semler
 * @version %I%, %G%
 * @since   1.0
 */
public class Uc2oolFatalException extends Uc2oolRuntimeException {

    /**
     * First version
     */
    private static final long serialVersionUID = 1L;

    /*
     * Constructor taking message key and arguments to be substituted
     * 
     * @param error the BaseError object. See com.fortysevenrooks.errors.BaseError
     */
    public Uc2oolFatalException(BaseError error) {
        super(error);
    }
}
