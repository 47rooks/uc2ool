/**
 * Copyright Daniel Semler 2015
 */
package ds.uc2ool.eclipse.errors;

import java.util.ListResourceBundle;

import ds.errors.ErrorTexts;

/**
 * Eclipse GUI messages
 * 
 * @author	Daniel Semler
 * @version	%I%, %G%
 * @since	1.0
 */
public class EclipseMessages extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return m_contents;
    }

    static final Object[][] m_contents = {

            // Fatal exception message for IStatus
            { "UC2-0001", new ErrorTexts(
                    "Got a fatal exception. Details below",
                    null,
                    null)
            },
    };

}
