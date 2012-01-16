/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package net.java.slee.resource.diameter.gq.events;


import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.base.events.avp.ProxyInfoAvp;
import net.java.slee.resource.diameter.gq.events.avp.AbortCause;
import net.java.slee.resource.diameter.gq.events.avp.Flows;
import net.java.slee.resource.diameter.gq.events.avp.SpecificAction;

/**
 * <pre>
 * <b>7.1.3  Re-Auth-Request (RAR) command</b>
 * The RAR command, indicated by the Command-Code field set to 258 and the 'R' bit set in
 * the Command Flags field, is sent by the SPDF to the AF in order to indicate a specific
 * action.
 * 
 * However, application-specific authentication and/or authorization messages are not 
 * mandated for the Gq application in response to an RAR command.
 * 
 * The values INDICATION_OF_RELEASE_OF_BEARER, INDICATION_OF_SUBSCRIBER_DETACHMENT, 
 * INDICATION_OF_RESERVATION_EXPIRATION and INDICATION_OF_LOSS_OF_BEARER, 
 * INDICATION_OF_RECOVERY_OF_BEARER and INDICATION_OF_RELEASE_OF_BEARER of the 
 * Specific-Action AVP shall not be combined with each other in an Re-Auth-Request.
 * 
 * Message Format:
 * 
 * &lt;RA-Request&gt; ::= < Diameter Header: 258, REQ, PXY > 
 *                  < Session-Id >
 *                  { Origin-Host } 
 *                  { Origin-Realm } 
 *                  { Destination-Realm } 
 *                  { Destination-Host } 
 *                  { Auth-Application-Id }
 *                 *{ Specific-Action } 
 *                 *[ Flow-Description ]
 *                  [ Globally-Unique-Address ]
 *                  [ Logical-Access-Id ] 
 *                 *[ Flows ]
 *                  [ Abort-Cause ]
 *                  [ Origin-State-Id ] 
 *                 *[ Proxy-Info ] 
 *                 *[ Route-Record ] 
 *                 *[ AVP ]
 * </pre>
 *
 * @author <a href="mailto:webdev@web-ukraine.info"> Yulian Oifa </a>
 */
public interface GqReAuthRequest extends DiameterMessage 
{
	public static final int COMMAND_CODE = 258;

	/**
	* Returns the value of the Auth-Application-Id AVP, of type Unsigned32.
	* 
	* @return
	*/
	long getAuthApplicationId();

	/**
	* Sets the value of the Auth-Application-Id AVP, of type Unsigned32.
	* 
	* @param authApplicationId
	* @throws IllegalStateException
	*/
	void setAuthApplicationId(long authApplicationId) throws IllegalStateException;

	/**
	* Returns true if the Auth-Application-Id AVP is present in the message.
	* 
	* @return
	*/
	boolean hasAuthApplicationId();
	
	/**
	* Returns the set of Proxy-Info AVPs.
	* 
	* @return
	*/
	ProxyInfoAvp[] getProxyInfos();

	/**
	* Sets a single Proxy-Info AVP in the message, of type Grouped.
	* 
	* @param proxyInfo
	* @throws IllegalStateException
	*/
	void setProxyInfo(ProxyInfoAvp proxyInfo) throws IllegalStateException;

	/**
	* Sets the set of Proxy-Info AVPs, with all the values in the given array.
	* 
	* @param proxyInfos
	* @throws IllegalStateException
	*/
	void setProxyInfos(ProxyInfoAvp[] proxyInfos) throws IllegalStateException;
	
	/**
	* Returns the set of Route-Record AVPs.
	* 
	* @return
	*/
	DiameterIdentity[] getRouteRecords();

	/**
	* Sets a single Route-Record AVP in the message, of type DiameterIdentity.
	* 
	* @param routeRecord
	* @throws IllegalStateException
	*/
	void setRouteRecord(DiameterIdentity routeRecord) throws IllegalStateException;

	/**
	* Sets the set of Route-Record AVPs, with all the values in the given
	* array.
	* 
	* @param routeRecords
	* @throws IllegalStateException
	*/
	void setRouteRecords(DiameterIdentity[] routeRecords) throws IllegalStateException;
	
	/**
	* Returns true if the Origin-State-Id AVP is present in the message.
	*/
	boolean hasOriginStateId();
	    
	/**
	* Returns the value of the Origin-State-Id AVP, of type Unsigned32. Use
	* {@link #hasOriginStateId()} to check the existence of this AVP.
	* 
	* @return the value of the Origin-State-Id AVP
	* @throws IllegalStateException
	*             if the Origin-State-Id AVP has not been set on this message
	*/
	long getOriginStateId();

	/**
	* Sets the value of the Origin-State-Id AVP, of type Unsigned32.
	* 
	* @throws IllegalStateException
	*             if setOriginStateId has already been called
	*/
	void setOriginStateId(long originStateId);
	
	/**
	* Returns the set of Specific-Action AVPs.
	* 
	* @return
	*/
	SpecificAction[] getSpecificActions();

	/**
	* Sets a single Specific-Action AVP in the message, of type SpecificAction.
	* 
	* @param specificAction
	* @throws IllegalStateException
	*/
	void setSpecificAction(SpecificAction specificAction) throws IllegalStateException;

	/**
	* Sets the set of Specific-Action AVPs, with all the values in the given
	* array.
	* 
	* @param specificActions
	* @throws IllegalStateException
	*/
	void setSpecificActions(SpecificAction[] specificActions) throws IllegalStateException;
	
	/**
	* Returns the set of Flows AVPs.
	* 
	* @return
	*/
	Flows[] getFlows();

	/**
	* Sets a single Flows AVP in the message, of type Flows.
	* 
	* @param flow
	* @throws IllegalStateException
	*/
	void setFlows(Flows flow) throws IllegalStateException;

	/**
	* Sets the set of Flows AVPs, with all the values in the given
	* array.
	* 
	* @param flows
	* @throws IllegalStateException
	*/
	void setFlows(Flows[] flows) throws IllegalStateException;
	
	/**
	* Returns true if the Abort-Cause AVP is present in the message.
	*/
	boolean hasAbortCause();

	/**
	* Returns the value of the Abort-Cause AVP, of type AbortCause.
	* @return the value of the Abort-Cause AVP or null if it has not been set on this message
	*/
	AbortCause getAbortCause();

	/**
	* Sets the value of the Abort-Cause AVP, of type AbortCause.
	* @throws IllegalStateException if setAbortCause has already been called
	*/
	void setAbortCause(AbortCause abortCause) throws IllegalStateException;
	
	/**
	* Returns true if the Logical-Access-Id AVP is present in the message.
	*/
	boolean hasLogicalAccessId();

	/**
	* Returns the value of the Logical-Access-Id AVP, of type OctetString.
	* @return the value of the Logical-Access-Id AVP or null if it has not been set on this message
	*/
	byte[] getLogicalAccessId();

	/**
	* Sets the value of the Logical-Access-Id AVP, of type OctetString.
	* @throws IllegalStateException if setLogicalAccessId has already been called
	*/
	void setLogicalAccessId(byte[] logicalAccessId) throws IllegalStateException;
}
