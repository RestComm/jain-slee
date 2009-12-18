package org.mobicents.slee.resource.xcapclient;

import javax.slee.resource.ActivityAlreadyExistsException;
import javax.slee.resource.StartActivityException;

import org.mobicents.xcap.client.XcapClient;

/**
 * This is the XCAP Client Resource Adaptor's Interface that Sbbs can use.
 * 
 * @author Eduardo Martins
 * @author aayush.bhatnagar
 * @version 1.0
 * 
 */

public interface XCAPClientResourceAdaptorSbbInterface extends XcapClient {
	
	public AsyncActivity createActivity() throws ActivityAlreadyExistsException, StartActivityException;
	
}
