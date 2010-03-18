package org.mobicents.slee.container.component.validator.profile;

import java.io.Serializable;

public interface ProfileCMPInterfaceGetterThrows {

	
	public void setMasterTest(int v);
	public int getMasterTest() throws Error;
	
	public void setMasterTestArray(int[] v);
	public int[] getMasterTestArray();
	
	
	public void setMasterTestSerializable(Serializable[] v);
	public Serializable[] getMasterTestSerializable();
}
