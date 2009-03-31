/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.util;

/**
 * Convenience utility for sleeping
 */
public class SleepUtil {

    /**
     * Blocks the calling thread for at least the given time period
     */
    public static void sleepFor(long millis) {
        long target = System.currentTimeMillis() + millis;
        long toSleep = millis;

        for(;;) {
            if(toSleep <= 0) break;
            try {
                Thread.currentThread().sleep(toSleep);
            } catch(InterruptedException ie) {
                // ignore
            }
            toSleep = target - System.currentTimeMillis();
        }
    }
    
    
}