/**
 * Copyright Daniel Semler 2015
 */
package ds.uc2ool.eclipse.errors;

import java.util.ResourceBundle;

import ds.errors.BaseError;

/**
 * @author	Daniel Semler
 * @version	%I%, %G%
 * @since	1.0
 */
public class EclipseError extends BaseError {

    private final static String RESOURCE_BUNDLE_NAME =
            "ds.uc2ool.eclipse.errors.EclipseMessages";

    protected EclipseError(String key,
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
