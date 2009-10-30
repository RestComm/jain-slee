package org.mobicents.ha.javax.sip.cache;

import org.mobicents.cache.MobicentsCache;
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
		cache = SleeContainer.lookupFromJndi().getCluster().getMobicentsCache();
	}
	
	@Override
	public void init() throws SipCacheException {
		// nothing to do
	}

}
