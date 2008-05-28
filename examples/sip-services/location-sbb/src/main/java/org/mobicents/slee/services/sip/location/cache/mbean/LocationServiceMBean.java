package org.mobicents.slee.services.sip.location.cache.mbean;

import java.util.Set;

public interface LocationServiceMBean {

	public static final String MBEAN_NAME_PREFIX="slee:siplocationservice=";
	
	/**
	 * 
	 * @return Set with registered users. It contains entries like "sip:316471@kocia.domena.au" or "sip:mobicents@gmail.com"
	 */
	public Set<String> getRegisteredUsers();
	/**
	 * 
	 * @param record - address of record, value like "sip:mobicents@gmail.com" this is passed in from and to header of REGISTER reqeust
	 * @return
	 */
	public Set<String> getContacts(String record);
	
	/**
	 * Returns time in miliseconds left till certain contact expires, if there is some error it return Long.MIN_VALUE.
	 * 
	 * @param record
	 * @param contact - must be exact value put into register. See return values of getContacts
	 * @return
	 */
	public long getExpirationTime(String record, String contact);
	
	/**
	 * Number of registered users.
	 * @return
	 */
	public int getRegisteredUserCount();
	
}
