/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.container.component;

import javax.slee.SbbID;

/** 
 * SBB Component Identifier. This the key to located the loaded
 * Sbb class in the service container. Note that an SBB may 
 * be referenced by multiple services. We cannot remove an
 * Sbb until all services no longer reference the sbb.
 * 
 * @author Francesco Moggia
 */
public class SbbIDImpl extends ComponentIDImpl implements SbbID  {
   
    private static final long serialVersionUID = 5469482667062459925L;
    
    public SbbIDImpl(ComponentKey key) {
        super(key);
    }
   
}
