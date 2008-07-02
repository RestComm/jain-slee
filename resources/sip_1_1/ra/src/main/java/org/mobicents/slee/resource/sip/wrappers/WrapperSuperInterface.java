package org.mobicents.slee.resource.sip.wrappers;

import org.mobicents.slee.resource.sip.SipActivityHandle;

public interface WrapperSuperInterface {

	public SipActivityHandle getActivityHandle();
	public Object getWrappedObject();
	public void clearAssociations();
}
