package org.mobicents.slee.container.component.validator.profile;

public interface ProfileBaseCMPInterfaceLackBoolean extends
		ProfileSuperCMPInterface {
	public void setMasterTest(int v);

	public int getMasterTest();

	public void setMasterTestArray(int[] v);

	public void setWithCollator(String v);

	public String getWithCollator();

	// we lack boolean
}
