/**
 * Copyright Daniel Semler 2015
 */
package ds.uc2ool.core.errors;

import java.util.ResourceBundle;

import ds.errors.BaseError;

/**
 * Instances of this class carry Uc2ool core error data.
 * 
 * @author	Daniel Semler
 * @version	%I%, %G%
 * @since	1.0
 */
public class CoreError extends BaseError {

    private final static String RESOURCE_BUNDLE_NAME =
            "ds.uc2ool.core.errors.CoreMessages";

    public CoreError(String key,
            Object[] errorArgs,
            Object[] reasonArgs,
            Object[] responseArgs)
    {
        super(key, errorArgs, reasonArgs, responseArgs);
    }
    
    /* (non-Javadoc)
     * @see ds.errors.BaseError#loadBundle()
     */
    @Override
    protected ResourceBundle loadBundle() {
        return ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME);
    }

}
