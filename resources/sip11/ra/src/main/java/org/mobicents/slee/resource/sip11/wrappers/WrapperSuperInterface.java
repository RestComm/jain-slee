package org.mobicents.slee.resource.sip11.wrappers;

import org.mobicents.slee.resource.sip11.SipActivityHandle;

public interface WrapperSuperInterface {

	public SipActivityHandle getActivityHandle();
	
	public void cleanup();

}
