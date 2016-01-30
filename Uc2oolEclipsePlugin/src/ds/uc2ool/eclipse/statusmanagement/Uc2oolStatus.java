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
    
    public Uc2oolStatus(Uc2oolRuntimeException t) {
        super(Activator.PLUGIN_ID,
              0,
              t.getError().getError(),
              null);
        add(new Status(IStatus.ERROR,
                       Activator.PLUGIN_ID,
                       0,
                       t.getError().getReason(),
                       null));        
        add(new Status(IStatus.ERROR,
                Activator.PLUGIN_ID,
                0,
                t.getError().getResponse(),
                null));        
    }

}
