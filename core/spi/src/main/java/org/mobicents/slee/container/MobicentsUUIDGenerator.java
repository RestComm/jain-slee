package org.mobicents.slee.container;

import java.rmi.server.UID;
import java.util.UUID;

public class MobicentsUUIDGenerator {

	private final boolean localMode; 
	
	public MobicentsUUIDGenerator(boolean localMode) {
		this.localMode = localMode;
		// some id generators initialize on fist call
		createUUID();
	}
	    
    public String createUUID() {
    	if (localMode) {
    		return new UID().toString();
    	}
    	else {
    		return UUID.randomUUID().toString();
    	}
    }
}
