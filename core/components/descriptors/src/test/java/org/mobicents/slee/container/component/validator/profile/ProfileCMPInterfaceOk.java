package org.mobicents.slee.container.component.validator.profile;

import java.io.Serializable;

public interface ProfileCMPInterfaceOk {

	
	public void setMasterTest(int v);
	public int getMasterTest();
	
	public void setMasterTestArray(int[] v);
	public int[] getMasterTestArray();
	
	
	public void setMasterTestSerializable(Serializable[] v);
	public Serializable[] getMasterTestSerializable();
}
