package org.mobicents.csapi.jr.slee;

import java.io.Serializable;

/**
 *  This is the parent class of all messages used by the Causeway resource
 *  adapter.
 *  <BR>
 *  @revision $Revision: 1.2 $<BR>
 *  <BR>
 */

public class ResourceEvent implements Serializable {
    
    public ResourceEvent( TpServiceIdentifier tpServiceIdentifier ) {
        this.tpServiceIdentifier = tpServiceIdentifier;
        
    }

	/**
	 * Returns the id of the service manager instance that this message
     * is associated with.
	 * @return int
	 */
    public TpServiceIdentifier getService() {
        return tpServiceIdentifier;
    }

    private TpServiceIdentifier tpServiceIdentifier = null;
    
    // Constants for JMSType headers
   
}
