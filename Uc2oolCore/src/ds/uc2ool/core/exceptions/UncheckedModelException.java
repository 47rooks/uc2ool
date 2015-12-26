/**
 * Copyright Daniel Semler 2015
 */
package ds.uc2ool.core.exceptions;

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
     * @param msgKey the resource bundle message key for the message
     * @param args arguments required by the message substitution string
     */
    public UncheckedModelException(String msgKey, Object... args) {
        super(msgKey, args);
    }
}
