package org.mobicents.slee.annotations.examples.profile;

import org.mobicents.slee.annotations.ProfileCMPField;

public interface ExampleProfileCMPInterface {

	@ProfileCMPField
	public String getX();
	public void setX(String x);
}
