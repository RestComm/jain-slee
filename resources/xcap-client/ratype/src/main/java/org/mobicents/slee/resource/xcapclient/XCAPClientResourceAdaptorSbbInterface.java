package org.mobicents.slee.resource.xcapclient;

import javax.slee.resource.ActivityAlreadyExistsException;
import javax.slee.resource.CouldNotStartActivityException;

import org.openxdm.xcap.client.XCAPClient;

/**
 * This is the XCAP Client Resource Adaptor's Interface that Sbbs can use.
 * 
 * @author Eduardo Martins
 * @version 1.0
 * 
 */

public interface XCAPClientResourceAdaptorSbbInterface extends XCAPClient {
	
	public AsyncActivity createActivity() throws ActivityAlreadyExistsException, CouldNotStartActivityException;
	
}
