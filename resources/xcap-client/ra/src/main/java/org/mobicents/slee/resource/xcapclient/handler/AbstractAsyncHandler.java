package org.mobicents.slee.resource.xcapclient.handler;

import java.io.IOException;
import java.util.List;

import org.mobicents.slee.resource.xcapclient.ResponseEvent;
import org.mobicents.slee.resource.xcapclient.XCAPClientResourceAdaptor;
import org.mobicents.slee.resource.xcapclient.XCAPResourceAdaptorActivityHandle;
import org.openxdm.xcap.client.RequestHeader;
import org.openxdm.xcap.client.Response;
import org.openxdm.xcap.common.key.XcapUriKey;

/**
 * Asbtract class for a handler
 * @author emmartins
 *
 */
public abstract class AbstractAsyncHandler implements Runnable {

	protected XcapUriKey key;
	protected XCAPClientResourceAdaptor ra;
	protected XCAPResourceAdaptorActivityHandle handle;
	protected List<RequestHeader> additionalRequestHeaders;
	
	protected AbstractAsyncHandler(XCAPClientResourceAdaptor ra,XCAPResourceAdaptorActivityHandle handle,XcapUriKey key, List<RequestHeader> additionalRequestHeaders) {
		super();
		this.key = key;
		this.ra = ra;
		this.handle = handle;
		this.additionalRequestHeaders = additionalRequestHeaders;
	}
	
	/**
	 * To be implemented by concrete handler, sends the request to the XCAP and
	 * processes response.
	 * 
	 * @return
	 * @throws IOException 
	 * @throws Exception 
	 */
	protected abstract Response doRequest() throws Exception;
	
	public void run() {
		ResponseEvent event = null;
		try {
			// execute method and get response
			Response response = doRequest();
			// create event with response
			event = new ResponseEvent(response);
		}
		catch(Exception e) {
			// create event with exception
			event = new ResponseEvent(e);
		}    		
		// process event
		ra.processResponseEvent(event,handle);			
	}
}
