/**
 * Copyright Daniel Semler 2015
 */
package com.fortysevenrooks.uc2ool.core.exceptions;

import com.fortysevenrooks.errors.BaseError;

/**
 * Objects of this class represent an exception in the (model)
 * and will contain translatable parameterised messages.
 * 
 * @author  Daniel Semler
 * @version %I%, %G%
 * @since   1.0
 */
public class UncheckedModelException extends Uc2oolRuntimeException {

    /**
     * First version
     */
    private static final long serialVersionUID = 1L;

    /*
     * Constructor taking message key and arguments to be substituted
     * 
     * @param error the BaseError object. See com.fortysevenrooks.errors.BaseError
     */
    public UncheckedModelException(BaseError error) {
        super(error);
    }
}
