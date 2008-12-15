package org.mobicents.sleetests.container.deployment.cmp;

import javax.slee.ActivityContextInterface;

import com.opencloud.sleetck.lib.resource.events.TCKResourceEventX;

public abstract class SbbAndSuperClassWithCMPsTestSbb extends SbbSuperClass {

	public void onTCKResourceEventX1(TCKResourceEventX event, ActivityContextInterface aci) {

	}
	
	public abstract void setCMP(String value);
	public abstract String getCMP();
	
}
