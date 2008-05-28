/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
package org.mobicents.slee.resource.rules.ratype;

/**
 * Used by Sbb to create new RulesSession
 * @author abhayani
 *
 */
public interface RulesProvider {
	public RulesSession getNewRulesSession(String drlFile);
}
