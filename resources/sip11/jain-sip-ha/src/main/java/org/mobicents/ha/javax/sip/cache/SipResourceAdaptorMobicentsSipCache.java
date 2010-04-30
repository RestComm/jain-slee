package org.mobicents.ha.javax.sip.cache;

import org.mobicents.slee.container.SleeContainer;

/**
 * This {@link SipCache} relies on the SLEE container cluster {@link MobicentsCache}.
 * 
 * @author martins
 * 
 */
public class SipResourceAdaptorMobicentsSipCache extends MobicentsSipCache {
	
	/**
	 * 
	 */
	public SipResourceAdaptorMobicentsSipCache() {
		super();
		cluster = SleeContainer.lookupFromJndi().getCluster();
	}
	
	@Override
	public void init() throws SipCacheException {
		// nothing to do
	}

}
