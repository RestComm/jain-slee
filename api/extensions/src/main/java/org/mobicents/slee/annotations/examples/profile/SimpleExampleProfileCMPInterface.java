package org.mobicents.slee.annotations.examples.profile;

import org.mobicents.slee.annotations.ProfileCMPField;
import org.mobicents.slee.annotations.ProfileSpec;

@ProfileSpec(name="SimpleAnnotatedProfileSpec",vendor="javax.slee",version="1.0",
		cmpInterface=SimpleExampleProfileCMPInterface.class)
public interface SimpleExampleProfileCMPInterface {

	@ProfileCMPField
	public String getX();
	public void setX(String x);
}
