/*
 * ***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************
 */
package org.mobicents.slee.training.example4.second;
/**
 * The SBB Activity Context Interface 
 * @author amit bhayani
 *
 */
public interface SecondBounceSbbActivityContextInterface extends
		javax.slee.ActivityContextInterface {

	public boolean getFilteredByFirstBounceSbb();

	public void setFilteredByFirstBounceSbb(boolean val);

	public boolean getFilteredByMe();

	public void setFilteredByMe(boolean val);

}