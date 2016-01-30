/**
 * Copyright Daniel Semler 2015
 */
package ds.uc2ool.eclipse.statusmanagement;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;

import ds.uc2ool.core.exceptions.Uc2oolRuntimeException;
import uc2ooleclipseplugin.Activator;

/**
 * @author	Daniel Semler
 * @version	%I%, %G%
 * @since	1.0
 */
public class Uc2oolStatus extends MultiStatus {
    
    private Uc2oolStatus(Uc2oolRuntimeException ure) {
        super(Activator.PLUGIN_ID,
              0,
              ure.getError().getError(),
              null);
        add(new Status(IStatus.ERROR,
                Activator.PLUGIN_ID,
                0,
                ure.getError().getReason(),
                null));        
    }

    /**
     * Add the response status to the MultiStatus
     * 
     * @param ure the exception to get the response status from.
     */
    private void addResponse(Uc2oolRuntimeException ure) {
        add(new Status(IStatus.ERROR,
                Activator.PLUGIN_ID,
                0,
                ure.getError().getResponse(),
                null));        
    }
    
    /**
     * Add the response status with the exception to the MultiStatus
     * 
     * @param ure the exception to add
     */
    private void addResponseWithException(Uc2oolRuntimeException ure) {
        add(new Status(IStatus.ERROR,
                Activator.PLUGIN_ID,
                0,
                ure.getError().getResponse(),
                ure));        
    }

    /**
     * Get a Uc2ool object for display to users. It will not contain any
     * stack trace information, but will contain all three parts of the 
     * error text.
     * 
     * @param ure the expectation for which to obtain the status
     * @return the status object
     */
    public static Uc2oolStatus getStatus(Uc2oolRuntimeException ure) {
        Uc2oolStatus us = new Uc2oolStatus(ure);
        us.addResponse(ure);
        return us;
    }
    
    /**
     * Get a status suitable for logging in the platform logging facility.
     * This will contain all message data plus the exception information.
     * 
     * @param ure the expectation for which to obtain the status
     * @return the status object
     */
    public static Uc2oolStatus getStatusWithException(Uc2oolRuntimeException ure) {
        Uc2oolStatus us = new Uc2oolStatus(ure);
        us.addResponseWithException(ure);
        return us;
    }
}
