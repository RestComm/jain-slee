/**
 * 
 */
package org.mobicents.slee.enabler.xdmc;

import java.net.URI;

import org.mobicents.slee.enabler.xdmc.jaxb.xcapdiff.AttributeType;
import org.mobicents.slee.enabler.xdmc.jaxb.xcapdiff.DocumentType;
import org.mobicents.slee.enabler.xdmc.jaxb.xcapdiff.ElementType;
import org.mobicents.slee.enabler.xdmc.jaxb.xcapdiff.XcapDiff;

/**
 * @author martins
 * 
 */
public interface XDMClientParent {

	/**
	 * Provides the response for an XML resource GET request.
	 * 
	 * @param uri
	 * @param responseCode
	 * @param mimetype
	 * @param content
	 * @param eTag
	 */
	public void getResponse(URI uri, int responseCode, String mimetype,
			String content, String eTag);

	/**
	 * Provides the response for an XML resource PUT request.
	 * 
	 * @param uri
	 * @param responseCode
	 * @param eTag
	 * @param responseContent
	 */
	public void putResponse(URI uri, int responseCode, String responseContent,
			String eTag);

	/**
	 * Provides the response for an XML resource DELETE request.
	 * 
	 * @param uri
	 * @param responseCode
	 * @param eTag
	 * @param responseContent
	 */
	public void deleteResponse(URI uri, int responseCode,
			String responseContent, String eTag);

	
	// DIFF part
	
	//FIXME: or subscribeSucceed(only for initial), subscribeTerminated(for all fail reason + notify with subscribe-state = terminated) is enough?
	/**
	 * Callback method indicating failure of communication, enabler must be
	 * discarded.
	 * 
	 * @param sbbLocalObject
	 */
	public void subscribeFailed(int responseCode, XDMClientChildSbbLocalObject sbbLocalObject, URI notifier);

	/**
	 * Callback method indicating failure of communication, enabler must be
	 * discarded.
	 * 
	 * @param sbbLocalObject
	 */
	public void resubscribeFailed(int responseCode, XDMClientChildSbbLocalObject sbbLocalObject, URI notifier);

	/**
	 * Callback method indicating failure of communication, enabler must be
	 * discarded.
	 * 
	 * @param sbbLocalObject
	 */
	void unsubscribeFailed(int responseCode, XDMClientChildSbbLocalObject sbbLocalObject, URI notifier);
	
	/**
	 * Callback method indicating outcome of initial subscribe. 
	 * 
	 * @param responseCode
	 * @param enabler
	 * 
	 */
	public void subscribeSucceed(int responseCode,  XDMClientChildSbbLocalObject sbbLocalObject, URI notifier);

	/**
	 * Callback method indicating outcome of unsubscribe. 
	 * 
	 * @param responseCode
	 *            response code to remove request
	 * @param enabler
	 */
	public void unsubscribeSucceed(int responseCode,  XDMClientChildSbbLocalObject sbbLocalObject, URI notifier);
	
	/**
	 * Callback method indicating termination of subscription.
	 * @param sbbLocalObject
	 * @param uri
	 */
	public void subscriptionTerminated(XDMClientChildSbbLocalObject sbbLocalObject, URI notifier); //FIXME: add resourceList to callback?
	
	/**
	 * Notifies an update in XDMS resources subscribed by the enabler.
	 * This callback is used also to notify on resubscription.
	 * @param xcapDiff - the xcap diff notified
	 */
	public void subscriptionNotification(XcapDiff xcapDiff);
	
}
