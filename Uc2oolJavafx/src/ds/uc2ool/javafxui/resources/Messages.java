package ds.uc2ool.javafxui.resources;

import java.util.ListResourceBundle;

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
