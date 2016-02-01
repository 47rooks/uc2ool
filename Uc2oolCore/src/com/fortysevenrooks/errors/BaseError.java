package com.fortysevenrooks.errors;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Instances of BaseError provide three translatable text strings for
 * providing error information to users. The three pieces of information are
 * the error itself, the proximate reason for the error, and the response the
 * user should make to the error. The response may range from simply entering
 * a more reasonable value to calling for assistance from the developer.
 * <p>
 * All strings are expected to be in a form compliant with the
 * <code>java.util.Formatter</code> as used by the <code>String.format()</code>
 * method. Thus they permit parameter substitution.
 * 
 * Subclasses are to be created for each set of messages defined in a
 * ListResourceBundle. Subclasses must implement getMessageBundle().
 * 
 * @author	Daniel Semler
 * @version	%I%, %G%
 * @since	1.0
 */
public abstract class BaseError {
    
    private final static String LAST_RESORT_ERROR_MSGS = 
            "com.fortysevenrooks.errors.resources.ErrorMessages";
    
    private final String m_key;
    private final Object[] m_errorArgs;
    private final Object[] m_reasonArgs;
    private final Object[] m_responseArgs;
    private ResourceBundle m_msgBundle;
    
    protected BaseError(String key,
                    Object[] errorArgs,
                    Object[] reasonArgs,
                    Object[] responseArgs) {
        
        if (key == null || key.length() == 0)
        {
            throw new IllegalArgumentException(
                    ResourceBundle.getBundle(LAST_RESORT_ERROR_MSGS)
                        .getString("NULL_KEY"));
        }
        
        m_key = key;
        m_errorArgs = errorArgs;
        m_reasonArgs = reasonArgs;
        m_responseArgs = responseArgs;
    }
    
    /**
     * This method loads the message bundle if it is not already loaded.
     * 
     * @return the message bundle.
     */
    private ResourceBundle getMessageBundle() {
        if (m_msgBundle == null) {
            m_msgBundle = loadBundle();
        }
        return m_msgBundle;
    }
    
    /**
     * Get the message bundle for this BaseError class.
     * @return the ListResourceBundle to obtain message text from.
     */
    protected abstract ResourceBundle loadBundle();
    
    /**
     * Get the error message text.
     * 
     * @return the error message string.
     */
    public String getError() {
        String msg = 
                ((ErrorTexts)getMessageBundle().getObject(m_key)).getError();
        return msg == null ? "" :
            msg.length() == 0 ? "" : String.format(msg, m_errorArgs);
    }
    
    /**
     * Get the reason for the error. This message will tell the user what
     * caused the error.
     * 
     * @return the reason text.
     */
    public String getReason() {
        String msg =
                ((ErrorTexts)getMessageBundle().getObject(m_key)).getReason();
        return msg == null ? "" :
            msg.length() == 0 ? "" :  String.format(msg, m_reasonArgs);
    }
    
    /**
     * Get the user response text. This string describes how the user should
     * best respond to the error they received.
     * 
     * @return the response text.
     */
    public String getResponse() {
        String msg =
                ((ErrorTexts)getMessageBundle().getObject(m_key)).getResponse();
        return msg == null ? "" :
            msg.length() == 0 ? "" :  String.format(msg, m_responseArgs);
    }
    
    /**
     * Get a translated message containing all the parts and their arguments.
     * 
     * @return a fully translated error, reason and response message
     */
    public String getLocalizedMessage() {
        List<String> l = new ArrayList<String>();
        l.add(getError());
        l.add(getReason());
        l.add(getResponse());
        StringBuilder sb = new StringBuilder();
        l.forEach((elt)->{
            if (elt != null && elt.length() > 0) sb.append(elt).append("\n");
        });
        return sb.substring(0, sb.lastIndexOf("\n"));
    }
}
