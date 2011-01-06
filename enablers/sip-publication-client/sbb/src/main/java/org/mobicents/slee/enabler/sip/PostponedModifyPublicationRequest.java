package org.mobicents.slee.enabler.sip;

/**
 * 
 * @author martins
 *
 */
public class PostponedModifyPublicationRequest implements PostponedRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String document;
	private final String contentType;
	private final String contentSubType;
	private final int expires;
	
	public PostponedModifyPublicationRequest(String document,
			String contentType, String contentSubType, int expires) {
		this.document = document;
		this.contentType = contentType;
		this.contentSubType = contentSubType;
		this.expires = expires;
	}

	@Override
	public void resume(PublicationClientChild publicationClientChild) {
		publicationClientChild.modifyPublication(document, contentType, contentSubType, expires);
	}

}
