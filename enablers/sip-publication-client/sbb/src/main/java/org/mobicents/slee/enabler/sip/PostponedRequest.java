package org.mobicents.slee.enabler.sip;

import java.io.Serializable;

/**
 * A request that is postponed due to enabler doing a refresh.
 * @author martins
 *
 */
public interface PostponedRequest extends Serializable {

	void resume(PublicationClientChild publicationClientChild);
	
}
