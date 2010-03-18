package org.mobicents.slee.container.component.validator.profile;

import java.io.Serializable;

public interface ProfileCMPInterfaceTypeMissMatch {

	
	public void setMasterTest(int v);
	public long getMasterTest();
	
	public void setMasterTestArray(int[] v);
	public int[] getMasterTestArray();
	
	
	public void setMasterTestSerializable(Serializable[] v);
	public Serializable[] getMasterTestSerializable();
}
