/**
 * Copyright Daniel Semler 2015
 */
package com.fortysevenrooks.utils;

/**
 * PlatformUtils provides platform specific utilities. The hope is to keep this
 * as small as possible. It will be enhanced on an as-needed basis. It is not
 * planned to support any and every conceivable port specific function.
 * 
 * @author	Daniel Semler
 * @version	%I%, %G%
 * @since	1.0
 */
public class PlatformUtils {
    public static String getOSName() {
        return System.getProperty("os.name");
    }
    
    public static boolean isOSX() {
        String osName = getOSName().toLowerCase();
        return osName.startsWith("mac") || osName.startsWith("darwin");
    }
}
