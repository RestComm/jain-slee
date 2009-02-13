package org.mobicents.slee.container.component.validator.profile;

public interface ProfileBaseCMPInterfaceCollatorOnNonString extends
		ProfileSuperCMPInterface {
	public void setMasterTest(int v);

	public int getMasterTest();

	public void setMasterTestArray(int[] v);

	public void setWithCollator(Boolean v);

	public Boolean getWithCollator();

	public void setWithoutCollatorBoolean(Boolean v);

	public Boolean getWithoutCollatorBoolean();
	// we lack boolean
	

	
	
	
}
