/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
package org.mobicents.slee.resource.rules.ratype;

import java.util.List;

/**
 * This is bridge between the Sbb and WorkingMemory. Maintains the instance of
 * WorkingMemory.
 * 
 * @author abhayani
 * 
 */

public interface RulesSession {

	public String getId();

	public List executeRules(final List objects);
	
	public void dispose();

}
