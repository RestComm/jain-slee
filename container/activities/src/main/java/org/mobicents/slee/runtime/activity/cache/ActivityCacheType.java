package org.mobicents.slee.runtime.activity.cache;

public enum ActivityCacheType 
{
	ATTACHED_SBBS(1),ATTACHED_TIMERS(2),NAMES_BOUND(3),CMP_ATTRIBUTES(4),METADATA(5);
	
	private final int value;

	ActivityCacheType(final int newValue) {
        value = newValue;
    }

    public int getValue() { 
    	return value; 
    }
}
