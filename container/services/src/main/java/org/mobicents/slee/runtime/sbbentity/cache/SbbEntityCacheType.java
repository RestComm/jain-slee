package org.mobicents.slee.runtime.sbbentity.cache;

public enum SbbEntityCacheType 
{
	ATTACHED_CONTEXTS(1),MASKED_EVENTS(2),CMP_FIELDS(3),CHILD_RELATIONS(4),METADATA(5);
	
	private final int value;

	SbbEntityCacheType(final int newValue) {
        value = newValue;
    }

    public int getValue() { 
    	return value; 
    }
}
