/**
 * Copyright Daniel Semler 2015
 */
package com.fortysevenrooks.errors;

/**
 * @author	Daniel Semler
 * @version	%I%, %G%
 * @since	1.0
 */
public class ErrorTexts {
    private final String m_error;
    private final String m_reason;
    private final String m_response;
    
    public ErrorTexts(String error, String reason, String response) {
        m_error = error;
        m_reason = reason;
        m_response = response;
    }

    /**
     * @return the m_error
     */
    public String getError() {
        return m_error;
    }

    /**
     * @return the m_reason
     */
    public String getReason() {
        return m_reason;
    }

    /**
     * @return the m_response
     */
    public String getResponse() {
        return m_response;
    }
}
