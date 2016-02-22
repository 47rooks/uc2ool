package com.fortysevenrooks.utils;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;

/**
 * FontUtils offers generic font utilities such as checking correctly for
 * all platforms of interest whether a particular font has a glyph for a
 * particular code point.
 * 
 * @author	Daniel Semler
 * @version	%I%, %G%
 * @since	1.0
 */
public class FontUtils {
    
    /**
     * Check whether a particular font has a glyph for the specified codepoint.
     * This has special handling for Mac OSX where <code>canDisplay()</code> is
     * unreliable because it appears to include the fallback font in the check.
     * Strictly one could use the same method as used for OSX on all ports but
     * it is known to be expensive, so a port check is done.
     * 
     * @param fontName the name of the font to check
     * @param cp the codepoint to check for
     * @return true if the font has a glyph for the codepoint, false otherwise.
     */
    public static boolean hasGlyph(String fontName, int cp) {
        Font awtFont = 
                new java.awt.Font(fontName,
                        java.awt.Font.PLAIN,
                        16);
        if (PlatformUtils.isOSX()) {
            final FontRenderContext fontRenderContext =
                    new FontRenderContext(null, false, false);
            char[] array = new char[2];
            array[0] = (char) cp;
            GlyphVector glyphVector =
                    awtFont.createGlyphVector(fontRenderContext, array);
            int glyphCode = glyphVector.getGlyphCode(0);
            return (glyphCode > 0);
        }

        return !awtFont.canDisplay(cp);
    }
}
