package org.mobicents.slee.resource.tts.ra;

import javax.slee.resource.ActivityHandle;

import org.apache.log4j.Logger;
import org.mobicents.slee.resource.tts.ratype.TTSSession;

public class TTSActivityHandle implements ActivityHandle {
    private static Logger logger = Logger.getLogger(TTSActivityHandle.class);
    private String handle = null;
    
    
    public TTSActivityHandle(String id) {
        logger.debug("TTSActivityHandle(" + id + ") called.");
        this.handle = id;
    }    
    
	public TTSActivityHandle(TTSSession ttsSession) {
		logger.debug("TTSActivityHandle(" + ttsSession + ") called.");
		this.handle = ttsSession.getSessionId();
	}    
    
    // ActivityHandle interface
    public boolean equals(Object o) {
    	if (o != null && o.getClass() == this.getClass()) {
			return ((TTSActivityHandle)o).handle.equals(this.handle);
		}
		else {
			return false;
		}
    }
       
    // ActivityHandle interface    
    public int hashCode() {
        return handle.hashCode();
    }        
}
