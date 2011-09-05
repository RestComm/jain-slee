/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * 
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free 
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.slee.resources.smpp;

/**
 * @author amit bhayani
 * 
 */
public interface SmppResourceAdaptorUsageParameters {

	// counter-type usage parameter names - REQUEST
	public void incrementReqBindTransceiver(long value);

	public void incrementReqBindTransmitter(long value);

	public void incrementReqBindReceiver(long value);

	public void incrementReqUnbind(long value);

	public void incrementReqEnquireLink(long value);

	public void incrementReqAlertNotification(long value);

	public void incrementReqSubmitSm(long value);

	public void incrementReqDataSm(long value);

	public void incrementReqSubmitMulti(long value);

	public void incrementReqDeliverSm(long value);

	public void incrementReqBroadcastSm(long value);

	public void incrementReqCancelSm(long value);

	public void incrementReqQuerySm(long value);

	public void incrementReqReplaceSm(long value);

	public void incrementReqQueryBroadcastSm(long value);

	public void incrementReqCancelBroadcastSm(long value);

	// This is V3.3
	public void incrementReqParamRetrieve(long value);

	// This is V3.3
	public void incrementReqQueryMessageDetails(long value);

	// This is V3.3
	public void incrementReqQueryLastMessages(long value);

	// counter-type usage parameter names - RESPONSE
	public void incrementResBindTransceiver(long value);

	public void incrementResBindTransmitter(long value);

	public void incrementResBindReceiver(long value);

	public void incrementResUnbind(long value);

	public void incrementResEnquireLink(long value);

	public void incrementResSubmitSm(long value);

	public void incrementResDataSm(long value);

	public void incrementResSubmitMulti(long value);

	public void incrementResDeliverSm(long value);

	public void incrementResBroadcastSm(long value);

	public void incrementResCancelSm(long value);

	public void incrementResQuerySm(long value);

	public void incrementResReplaceSm(long value);

	public void incrementResQueryBroadcastSm(long value);

	public void incrementResCancelBroadcastSm(long value);

	// This is V3.3
	public void incrementResParamRetrieve(long value);

	// This is V3.3
	public void incrementResQueryMessageDetails(long value);

	// This is V3.3
	public void incrementResQueryLastMessages(long value);

	public void incrementGenericNack(long value);

	public void incrementMessagesExchanged(long value);

	public long getMessagesExchanged();

	/**
	 * Number of messages exchanged per second. The messages could be any
	 * message request/response going out or coming in
	 * 
	 * @param value
	 */
	public void sampleMessagesExchangedPerSec(long value);

}
