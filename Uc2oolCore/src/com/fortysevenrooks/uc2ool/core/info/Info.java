/**
 * Copyright Daniel Semler 2015
 */
package com.fortysevenrooks.uc2ool.core.info;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Instances of the Info class provide support for storing one or more
 * status messages and their corresponding arguments. They are stored in
 * the order they are added. These messages are assumed to use messages
 * Ids which are known to the client. The client is expected to handle
 * looked of the message in an appropriate bundle and to apply the
 * arguments to it. Info objects merely provide storage. 
 * 
 * There is no multi-threading support.
 * 
 * @author  Daniel Semler
 * @version %I%, %G%
 * @since   1.0
 */
public class Info {
    List<String> m_msgIds;
    List<List<Object>> m_args;
    
    public Info() {
        m_msgIds = new ArrayList<String>();
        m_args = new ArrayList<List<Object>>();
    };
    
    /**
     * Add a status message and arguments to this Info
     * 
     * @param msgId the message Id for this status message
     * @param args a list of arguments required by the message
     */
    public void add(String msgId, Object... args) {
        m_msgIds.add(msgId);
        m_args.add(Arrays.asList(args));
    }
    
    /**
     * Clear down the status object
     */
    public void clear() {
        m_msgIds = new ArrayList<String>();
        m_args = new ArrayList<List<Object>>();
    }
    
    /**
     * Get the message Id
     * 
     * @param index the index of the message Id to return
     */
    public String getId(int index) {
        return m_msgIds.get(index);
    }
    
    /**
     * Get the message arguments as an Object[].
     * 
     * @param index the specific message argument List object
     */
    public Object[] getArgs(int index) {
        return m_args.get(index).toArray();
    }
    
    /**
     * Determine whether there are any status messages to display.
     * 
     * @return true if there are no status messages, false otherwise
     */
    public boolean isEmpty() {
        return m_msgIds.isEmpty();
    }
}