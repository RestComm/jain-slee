/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */ 
package net.java.slee.resources.smpp;

import java.util.Calendar;

import net.java.slee.resources.smpp.pdu.Address;
import net.java.slee.resources.smpp.pdu.SmppRequest;
import net.java.slee.resources.smpp.pdu.SmppResponse;
import net.java.slee.resources.smpp.util.AbsoluteSMPPDate;
import net.java.slee.resources.smpp.util.RelativeSMPPDate;

/**
 * In order to exchange short messages, a SMPP session must be established between the ESME and Message Centre or SMPP
 * Routing Entity where appropriate.
 * 
 * @author amit bhayani
 */
public interface SmppSession {

	/**
	 * Get the unique session ID.
	 * 
	 * @return
	 */
	public String getSessionId();

	public String getSMSCHost();

	public int getSMSPort();

        public SmppTransaction createTransaction(SmppRequest request);

	public void sendRequest(SmppRequest request) throws java.lang.IllegalStateException,
			java.lang.NullPointerException, java.io.IOException;

	public void sendResponse(SmppTransaction txn, SmppResponse response) throws java.lang.IllegalStateException,
			java.lang.NullPointerException, java.io.IOException;

	public boolean isAlive();

	/**
	 * @see SmppRequest
	 * @param commandId
	 * @return
	 */
	public SmppRequest createSmppRequest(int commandId);

	public Address createAddress(int addTon, int addNpi, String address);

	public AbsoluteSMPPDate createAbsoluteSMPPDate(Calendar calendar, boolean hasTz);

	public RelativeSMPPDate createRelativeSMPPDate(int years, int months, int days, int hours, int minutes, int seconds);

}
