package org.mobicents.slee.services.sip.common;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sip.address.URI;

public interface LocationInterface {


	/**
	 * Adds new contact binding for particular user..
	 * @param sipAddress - user address of record sip:ala@ma.kota.w.domu.com
	 * @param contactAddress - contact address - sip:+381243256
	 * @param comment - possible comment note
	 * @param expiresDelta - long - miliseconds for which this contact is to remain valid
	 * @param q - q parameter
	 * @param id - call id
	 * @param seq - seq numbers
	 * @return - bidning created in this operation
	 * @throws LocationServiceException
	 */
	public RegistrationBinding addUserLocation(String sipAddress,String contactAddress, String comment,
			long expiresDelta, float q, String id, long seq) throws LocationServiceException;

	/**
	 * Returns set of user that have registered - set contains adress of record fro each user, something like sip:ala@kocia.domena.com
	 * @return
	 */
	public Set<String> getRegisteredUsers();
	/**
	 * Returns map which contians mapping contactAddress->registrationBinding for particular user - address of record sip:nie@ma.mnie.tu
	 * @param sipAddress
	 * @return
	 * @throws LocationServiceException
	 */
	public Map<String,RegistrationBinding> getUserBindings(String sipAddress) throws LocationServiceException;
	/**
	 * Removes contact address from user bindings.
	 * @param sipAddress - sip:ala@kocia.domena.au
	 * @param contactAddress - sip:+481234567890
	 * @throws LocationServiceException
	 */
	public void removeBinding(String sipAddress, String contactAddress)throws LocationServiceException;
	
	
}
