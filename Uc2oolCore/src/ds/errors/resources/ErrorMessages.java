/**
 * Copyright Daniel Semler 2015
 */
package ds.errors.resources;

import java.util.ListResourceBundle;

/**
 * This class provides messages of last resort primarily to indicate faults
 * in error message infrastructure or bad use of it by clients. These messages
 * will be placed in regular JDK exceptions. Thus they are not ErrorTexts.
 * 
 * @author	Daniel Semler
 * @version	%I%, %G%
 * @since	1.0
 */
public class ErrorMessages extends ListResourceBundle {

    static final Object[][] m_contents = {
            { "NULL_KEY", "Client specified a null message key" },
    };
    
    /* (non-Javadoc)
     * @see java.util.ListResourceBundle#getContents()
     */
    @Override
    protected Object[][] getContents() {

        return m_contents;
    }
}
