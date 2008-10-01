package org.mobicents.slee.services.sip.location.jmx;

import java.util.Set;

import org.mobicents.slee.services.sip.location.LocationServiceException;


public interface LocationServiceManagementMBean {

	public static final String MBEAN_NAME="slee:sipservice=Location";
	
	/**
	 * 
	 * @return Set with registered users. It contains entries like "sip:316471@kocia.domena.au" or "sip:mobicents@gmail.com"
	 */
	public Set<String> getRegisteredUsers() throws LocationServiceException;
	/**
	 * 
	 * @param sipAddress - address of record, value like "sip:mobicents@gmail.com" this is passed in from and to header of REGISTER reqeust
	 * @return
	 * @throws LocationServiceException 
	 */
	public Set<String> getContacts(String sipAddress) throws LocationServiceException;
	
	/**
	 * Returns time in miliseconds left till certain contact expires, if there is some error it return Long.MIN_VALUE.
	 * 
	 * @param sipAddress
	 * @param contactAddress - must be exact value put into register. See return values of getContacts
	 * @return
	 * @throws LocationServiceException 
	 */
	public long getExpirationTime(String sipAddress, String contactAddress) throws LocationServiceException;
	
	/**
	 * Number of registered users.
	 * @return
	 */
	public int getRegisteredUserCount() throws LocationServiceException;
	
}
