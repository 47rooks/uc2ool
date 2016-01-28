package ds.uc2ool.javafxui.errors;

import java.util.ListResourceBundle;

import ds.errors.ErrorTexts;

public class JavafxMessages extends ListResourceBundle {
    
    @Override
    protected Object[][] getContents() {
        return m_contents;
    }

    static final Object[][] m_contents = {
            // FIXME add reason and response messages

            // Fatal errors requiring development, or at least code,
            // investigation
            { "INIT_FAILED", new ErrorTexts(
                    "Field %1$s did not initialize from %2$s.",
                    null,
                    "Contact developer - program will exit.")
            }

    };

}
