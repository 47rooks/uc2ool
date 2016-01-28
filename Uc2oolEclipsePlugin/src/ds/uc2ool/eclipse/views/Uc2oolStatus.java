/**
 * Copyright Daniel Semler 2015
 */
package ds.uc2ool.eclipse.views;

import org.eclipse.core.runtime.Status;

import uc2ooleclipseplugin.Activator;

/**
 * @author	Daniel Semler
 * @version	%I%, %G%
 * @since	1.0
 */
public class Uc2oolStatus extends Status {
    
    public enum Severity {
        SEVERE(1),
        WARNING(2),
        INFO(3);
        
        private int m_severity;
        
        private Severity(int sev) {
            m_severity = sev;
        }
        
        public int getSeverity() {
            return m_severity;
        }
    }
    
    public Uc2oolStatus(int severity,
                        int code,
                        String message,
                        Throwable t) {
        super(severity, Activator.PLUGIN_ID, code, message, t);
    }
}
