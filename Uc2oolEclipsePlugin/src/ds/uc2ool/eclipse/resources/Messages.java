/**
 * Copyright Daniel Semler 2015
 */
package ds.uc2ool.eclipse.resources;

import java.util.ListResourceBundle;

/**
 * Eclipse GUI messages
 * 
 * @author	Daniel Semler
 * @version	%I%, %G%
 * @since	1.0
 */
public class Messages extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return m_contents;
    }

    static final Object[][] m_contents = {
            
            // Status messages
            { "NO_GLYPH", "This font has no glyph for this codepoint." },

    };

}
