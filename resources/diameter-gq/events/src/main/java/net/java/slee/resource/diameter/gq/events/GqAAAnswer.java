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
import net.java.slee.resource.diameter.base.events.avp.ExperimentalResultAvp;
import net.java.slee.resource.diameter.base.events.avp.FailedAvp;
import net.java.slee.resource.diameter.base.events.avp.ProxyInfoAvp;
import net.java.slee.resource.diameter.gq.events.avp.BindingInformation;
import net.java.slee.resource.diameter.gq.events.avp.ReservationPriority;

/**
 * <pre>
 * <b>7.1.2 AA-Answer(AAA) command</b>
 * The AAA command, indicated by the Command-Code field set to 265 and the "R" bit cleared in the Command Flags
 * field, is sent by the SPDF to the AF in response to the AAR command.
 * Message Format:
 * &lt;AA-Answer&gt; ::= < Diameter Header: 265, PXY >
 *                 < Session-Id >
 *                 { Auth-Application-Id }
 *                 { Origin-Host }
 *                 { Origin-Realm }
 *                 [ Result-Code ]
 *                 [ Experimental-Result ]
 *                 [ Binding-Information ]
 *                 [ Reservation-Priority ]
 *                 [ Error-Message ]
 *                 [ Error-Reporting-Host ]
 *                 [ Authorization-Lifetime ]
 *                 [ Auth-Grace-Period ]
 *                 [ Failed-AVP ]
 *                 [ Proxy-Info ]
 *                 [ AVP ]
 * </pre>
 *
 * @author <a href="mailto:webdev@web-ukraine.info"> Yulian Oifa </a>
 */
public interface GqAAAnswer extends DiameterMessage
{
	public static final int COMMAND_CODE = 265;

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
    * Returns true if the Result-Code AVP is present in the message.
    */
    boolean hasResultCode();

	/**
	* Returns the value of the Result-Code AVP, of type Unsigned32. Use
	* {@link #hasResultCode()} to check the existence of this AVP.
	* 
	* @return the value of the Result-Code AVP
	* @throws IllegalStateException
	*             if the Result-Code AVP has not been set on this message
	*/
	long getResultCode();

	/**
	* Sets the value of the Result-Code AVP, of type Unsigned32.
	* 
	* @throws IllegalStateException
	*             if setResultCode has already been called
	*/
	void setResultCode(long resultCode);
	
	/**
	* Returns the set of Failed-AVP AVPs.
	* 
	* @return
	*/
	FailedAvp[] getFailedAvps();

	/**
	* Sets a single Failed-AVP AVP in the message, of type Grouped.
	* 
	* @param failedAvp
	* @throws IllegalStateException
	*/
	void setFailedAvp(FailedAvp failedAvp) throws IllegalStateException;

	/**
	* Sets the set of Failed-AVP AVPs, with all the values in the given array.
	* 
	* @param failedAvps
	* @throws IllegalStateException
	*/
	void setFailedAvps(FailedAvp[] failedAvps) throws IllegalStateException;
	
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
	* Returns the value of the Authorization-Lifetime AVP, of type Unsigned32.
	* 
	* @return
	*/
	long getAuthorizationLifetime();

	/**
	* Sets the value of the Authorization-Lifetime AVP, of type Unsigned32.
	* 
	* @param authApplicationId
	* @throws IllegalStateException
	*/
	void setAuthorizationLifetime(long authorizationLifetime) throws IllegalStateException;

	/**
	* Returns true if the Authorization-Lifetime AVP is present in the message.
	* 
	* @return
	*/
	boolean hasAuthorizationLifetime();
	
	/**
	* Returns true if the Auth-Grace-Period AVP is present in the message.
	*/
	boolean hasAuthGracePeriod();
	    
	/**
	* Returns the value of the Auth-Grace-Period AVP, of type Unsigned32. Use
	* {@link #hasAuthGracePeriod()} to check the existence of this AVP.
	* 
	* @return the value of the Auth-Grace-Period AVP
	* @throws IllegalStateException
	*             if the Auth-Grace-Period AVP has not been set on this message
	*/
	long getAuthGracePeriod();

	/**
	* Sets the value of the Auth-Grace-Period AVP, of type Unsigned32.
	* 
	* @throws IllegalStateException
	*             if setAuthGracePeriod has already been called
	*/
	void setAuthGracePeriod(long authGracePeriod);
	
	/**
	* Returns true if the Error-Message AVP is present in the message.
	*/
	boolean hasErrorMessage();

	/**
	* Returns the value of the Error-Message AVP, of type UTF8String.
	* @return the value of the Error-Message AVP or null if it has not been set on this message
	*/
	String getErrorMessage();

	/**
	* Sets the value of the Error-Message AVP, of type UTF8String.
	* @throws IllegalStateException if setErrorMessage has already been called
	*/
	void setErrorMessage(String errorMessage) throws IllegalStateException;
	
	/**
	* Returns true if the Error-Reporting-Host AVP is present in the message.
	*/
	boolean hasErrorReportingHost();

	/**
	* Returns the value of the Error-Reporting-Host AVP, of type DiameterIdentity.
	* @return the value of the Error-Reporting-Host AVP or null if it has not been set on this message
	*/
	DiameterIdentity getErrorReportingHost();

	/**
	* Sets the value of the Error-Reporting-Host AVP, of type DiameterIdentity.
	* @throws IllegalStateException if setErrorReportingHost has already been called
	*/
	void setErrorReportingHost(DiameterIdentity errorReportingHost) throws IllegalStateException;
	
	/**
	* Returns true if the Experimental-Result AVP is present in the message.
	*/
	boolean hasExperimentalResult();

	/**
	* Returns the value of the Experimental-Result AVP, of type ExperimentalResultAvp.
	* @return the value of the Experimental-Result AVP or null if it has not been set on this message
	*/
	ExperimentalResultAvp getExperimentalResult();

	/**
	* Sets the value of the Experimental-Result AVP, of type ExperimentalResultAvp.
	* @throws IllegalStateException if setErrorReportingHost has already been called
	*/
	void setExperimentalResult(ExperimentalResultAvp experimentalResult) throws IllegalStateException;
	
	/**
	* Returns true if the Binding-Information AVP is present in the message.
	*/
	boolean hasBindingInformation();

	/**
	* Returns the value of the Binding-Information AVP, of type BindingInformation.
	* @return the value of the Binding-Information AVP or null if it has not been set on this message
	*/
	BindingInformation getBindingInformation();

	/**
	* Sets the value of the Binding-Information AVP, of type BindingInformation.
	* @throws IllegalStateException if setBindingInformation has already been called
	*/
	void setBindingInformation(BindingInformation bindingInformation) throws IllegalStateException;
	
	/**
	* Returns true if the Reservation-Priority AVP is present in the message.
	*/
	boolean hasReservationPriority();

	/**
	* Returns the value of the Reservation-Priority AVP, of type ReservationPriority.
	* @return the value of the Reservation-Priority AVP or null if it has not been set on this message
	*/
	ReservationPriority getReservationPriority();

	/**
	* Sets the value of the Reservation-Priority AVP, of type ReservationPriority.
	* @throws IllegalStateException if setReservationPriority has already been called
	*/
	void setReservationPriority(ReservationPriority reservationPriority) throws IllegalStateException;
}
