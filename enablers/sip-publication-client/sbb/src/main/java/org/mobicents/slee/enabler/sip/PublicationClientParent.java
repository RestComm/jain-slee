/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.slee.enabler.sip;

/**
 * @author baranowb
 * @author martins
 */
public interface PublicationClientParent {

	/**
	 * Callback method indicating that new publication request succeed.
	 * 
	 * @param child
	 *            the enabler, which handles publication
	 */
	public void newPublicationSucceed(PublicationClientChildSbbLocalObject child);

	/**
	 * Callback method indicating that modify publication request succeed.
	 * 
	 * @param child
	 *            the enabler, which handles publication
	 */
	public void modifyPublicationSucceed(PublicationClientChildSbbLocalObject child);

	/**
	 * Callback method indicating that remove publication request succeed.
	 * 
	 * @param child
	 *            the enabler, which handles publication
	 */
	public void removePublicationSucceed(PublicationClientChildSbbLocalObject child);

	/**
	 * Callback method indicating that new publication request failed. Enabler
	 * must be discarded.
	 * 
	 * @param errorCode
	 * @param sbbLocalObject
	 */
	public void newPublicationFailed(int errorCode,
			PublicationClientChildSbbLocalObject sbbLocalObject);

	/**
	 * Callback method indicating that modify publication request failed.
	 * Enabler must be discarded.
	 * 
	 * @param errorCode
	 * @param sbbLocalObject
	 */
	public void modifyPublicationFailed(int errorCode,
			PublicationClientChildSbbLocalObject sbbLocalObject);

	/**
	 * Callback method indicating that refresh publication request failed.
	 * Enabler must be discarded.
	 * 
	 * @param errorCode
	 * @param sbbLocalObject
	 */
	public void refreshPublicationFailed(int errorCode,
			PublicationClientChildSbbLocalObject sbbLocalObject);

	/**
	 * Callback method indicating that remove publication request failed.
	 * Enabler must be discarded.
	 * 
	 * @param errorCode
	 * @param sbbLocalObject
	 */
	public void removePublicationFailed(int errorCode,
			PublicationClientChildSbbLocalObject sbbLocalObject);

}
