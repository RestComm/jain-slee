package org.mobicents.slee.container.component.validator.profile;

import java.io.Serializable;

public interface ProfileCMPInterfaceWrongFieldType {

	
	public void setMasterTest(Object v);
	public Object getMasterTest();
	
	public void setMasterTestArray(int[] v);
	public int[] getMasterTestArray();
	
	
	public void setMasterTestSerializable(Serializable[] v);
	public Serializable[] getMasterTestSerializable();
}
