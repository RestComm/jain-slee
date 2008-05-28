/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
package org.mobicents.slee.resource.media.ra;

import javax.slee.resource.ActivityHandle;

import org.apache.log4j.Logger;

public class MediaActivityHandle implements ActivityHandle {
    private static Logger logger = Logger.getLogger(MediaActivityHandle.class);
    private String handle = null;
    
    public MediaActivityHandle(String id) {
        this.handle = id;
    }
    
    // ActivityHandle interface
    public boolean equals(Object obj) {
        if (obj instanceof MediaActivityHandle) {
            return handle.equals(((MediaActivityHandle)obj).handle);
        } 
        else return false;
    }
       
    // ActivityHandle interface    
    public int hashCode() {
        return handle.hashCode();
    }    
}