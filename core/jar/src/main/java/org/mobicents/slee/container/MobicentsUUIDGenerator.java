package org.mobicents.slee.container;

import java.io.ObjectStreamException;
import java.util.UUID;

public class MobicentsUUIDGenerator {

private static final MobicentsUUIDGenerator INSTANCE = new MobicentsUUIDGenerator();
	
	private MobicentsUUIDGenerator() {
		// some id generators initialize on fist call
		createUUID();
	}
	
	public static MobicentsUUIDGenerator getInstance() {		
		return INSTANCE;
	}
	
	// solves serialization of singleton
	private Object readResolve() throws ObjectStreamException {
        return INSTANCE;
    }

    // solves cloning of singleton
    protected Object clone() throws CloneNotSupportedException {
            throw new CloneNotSupportedException();
    }
    
    public String createUUID() {
    	return UUID.randomUUID().toString(); 
    }
}
