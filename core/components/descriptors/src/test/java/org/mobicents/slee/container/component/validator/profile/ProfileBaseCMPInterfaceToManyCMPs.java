package org.mobicents.slee.container.component.validator.profile;

public interface ProfileBaseCMPInterfaceToManyCMPs extends
		ProfileSuperCMPInterface {
	public void setMasterTest(int v);

	public int getMasterTest();

	public void setMasterTestArray(int[] v);

	public void setWithCollator(String v);

	public String getWithCollator();

	public void setWithoutCollatorBoolean(Boolean v);

	public Boolean getWithoutCollatorBoolean();
	// we lack boolean
	
	public void setWithoutCollatorBoolean2(Boolean v);

	public Boolean getWithoutCollatorBoolean2();
	
	
	
}
