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
 */
public interface PublicationClientParent {

	/**
	 * This method is called when publication enabler receives answer for
	 * publication creating request. If answer indicates success, it can be used
	 * to manage publication, otherwise it must be discarded.
	 * 
	 * @param result
	 *            - object holding information about operation
	 * @param child
	 *            - enabler, which handles publication
	 */
	public void afterNewPublication(Result result, PublicationClientChildLocalObject child);

	/**
	 * Callback indicating state of publication refresh. Its behaviour is
	 * similar to
	 * {@link #afterNewPublication(Result, PublicationClientChildLocalObject)}.
	 * 
	 * @param result
	 *            - object holding information about operation
	 * @param child
	 *            - enabler, which handles publication
	 */
	public void afterRefreshPublication(Result result, PublicationClientChildLocalObject child);

	/**
	 * Callback method indicating state of publication update. Its behaviour is
	 * similar to
	 * {@link #afterNewPublication(Result, PublicationClientChildLocalObject)}.
	 * 
	 * @param result
	 *            - object holding information about operation
	 * @param child
	 *            - enabler, which handles publication
	 */
	public void afterUpdatePublication(Result result, PublicationClientChildLocalObject child);

	/**
	 * Callback method indicating state of publication remove. Its behaviour is
	 * similar to
	 * {@link #afterNewPublication(Result, PublicationClientChildLocalObject)}.
	 * 
	 * @param result
	 *            - object holding information about operation
	 * @param child
	 *            - enabler, which handles publication
	 */
	public void afterRemovePublication(Result result, PublicationClientChildLocalObject child);

	/**
	 * Callback method indicating that communication failed. Enabler must be
	 * discarded
	 * 
	 * @param sbbLocalObject
	 */
	void newPublicationFailed(PublicationClientChildLocalObject sbbLocalObject);

	/**
	 * Callback method indicating that communication failed. Enabler must be
	 * discarded
	 * 
	 * @param sbbLocalObject
	 */
	void refreshPublicationFailed(PublicationClientChildLocalObject sbbLocalObject);

	/**
	 * Callback method indicating that communication failed. Enabler must be
	 * discarded
	 * 
	 * @param sbbLocalObject
	 */
	void removePublicationFailed(PublicationClientChildLocalObject sbbLocalObject);

}
