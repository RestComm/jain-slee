package org.mobicents.slee.container;

public enum CacheType 
{
	ACTIVITIES("ac"),SBB_ENTITIES("sbbe"),ACI_NAMING("aci");
	
	private final String value;

	CacheType(final String newValue) {
        value = newValue;
    }

    public String getValue() { 
    	return value; 
    }
}
