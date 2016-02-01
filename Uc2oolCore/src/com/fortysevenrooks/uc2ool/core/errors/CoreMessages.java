/**
 * Copyright Daniel Semler 2015
 */
package com.fortysevenrooks.uc2ool.core.errors;

import java.util.ListResourceBundle;

import com.fortysevenrooks.errors.ErrorTexts;

/**
 * @author	Daniel Semler
 * @version	%I%, %G%
 * @since	1.0
 */
public class CoreMessages extends ListResourceBundle {

    /* (non-Javadoc)
     * @see java.util.ListResourceBundle#getContents()
     */
    @Override
    protected Object[][] getContents() {
        return m_contents;
    }
    
    static final Object[][] m_contents = {
        { "INV_DEC_CP", new ErrorTexts(
                "Invalid decimal code point.",
                "The input \'%1$s\' cannot be interpreted as a valid code point.",
                "Please enter a valid decimal number, in the valid code point range.")
        },
        { "INV_HEX_CP", new ErrorTexts(
                "Invalid hexadecimal code point.",
                "The input \'%1$s\' cannot be interpreted as a code point.",
                "Please enter a valid hexdecimal number, in the valid code point range.")
        },
        { "INV_UTF8_ENCODING", new ErrorTexts(
                "Invalid UTF-8 input sequence.",
                "The input \'%1$s\' cannot be interpreted as a code point.",
                "Please enter a valid UTF-8 encoded character.")
        },
    };
}
