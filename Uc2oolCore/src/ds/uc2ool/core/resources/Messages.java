package ds.uc2ool.core.resources;

import java.util.ListResourceBundle;

/**
 * The UI and error messages. The messages use the <code>String.format</code>
 * format string syntax.
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
            { "INV_DEC_CP", "Invalid decimal code point %1$s." },
            { "INV_HEX_CP", "Invalid hexadecimal code point %1$s." },
            
            // Fatal errors requiring development, or at least code,
            // investigation
            { "INIT_FAILED", "Field %1$s did not initialize from %2%s." +
                             " Contact developer - program will exit."}
    };
}
