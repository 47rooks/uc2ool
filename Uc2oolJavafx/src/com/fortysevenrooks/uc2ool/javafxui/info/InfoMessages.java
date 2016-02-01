/**
 * Copyright Daniel Semler 2015
 */
package com.fortysevenrooks.uc2ool.javafxui.info;

import java.util.ListResourceBundle;

/**
 * @author	Daniel Semler
 * @version	%I%, %G%
 * @since	1.0
 */
public class InfoMessages extends ListResourceBundle {

    static final Object[][] m_contents = {

            // Status messages
            { "NO_GLYPH", "This font has no glyph for this codepoint."}
    };
    
    /* (non-Javadoc)
     * @see java.util.ListResourceBundle#getContents()
     */
    @Override
    protected Object[][] getContents() {
        return m_contents;
    }

}
