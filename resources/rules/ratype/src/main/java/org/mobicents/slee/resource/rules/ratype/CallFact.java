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
 * CallFact mantains the data that will be used by Rules.
 * Acts as Fact on which assertion is called
 * @author abhayani
 *
 */
public interface CallFact {

	public String getFromUri();

	public void setFromUri(String fromUri);

	public String getToUri();

	public void setToUri(String toUri);

	public String getSubMenu();

	public void setSubMenu(String subMenu);

	public String getDtmf();

	public void setDtmf(String dtmf);
}
