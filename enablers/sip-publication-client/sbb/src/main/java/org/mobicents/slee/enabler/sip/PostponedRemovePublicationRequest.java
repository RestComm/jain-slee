package org.mobicents.slee.enabler.sip;

/**
 * 
 * @author martins
 *
 */
public class PostponedRemovePublicationRequest implements PostponedRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public void resume(PublicationClientChild publicationClientChild) {
		publicationClientChild.removePublication();
	}

}
