/*
 * Created on Jan 18, 2005
 * 
 * The Open SLEE Project
 * 
 * A SLEE for the People
 * 
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 */
package org.mobicents.slee.services.sip.location.cache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.slee.transaction.SleeTransactionManager;
import javax.transaction.SystemException;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.cache.CacheableMap;
import org.mobicents.slee.runtime.cache.CacheableSet;
import org.mobicents.slee.runtime.transaction.TransactionManagerImpl;
import org.mobicents.slee.services.sip.common.LocationServiceException;
import org.mobicents.slee.services.sip.common.RegistrationBinding;

/**
 * 
 * Keeps track of mappings between User Agent SIP addresses and location
 * bindings such as IP address This class acts as backend storage for this
 * implementation of registrar service. Constructor has package access, so noone
 * can ceaete instance of this class and mess up bindings, only Registrar class
 * should do this.
 * 
 * @author F.Moggia
 * @author baranowb
 */
public class LocationService {
	String tcache = TransactionManagerImpl.RUNTIME_CACHE;

	// private static transient SleeTransactionManager tm =
	// SleeContainer.getTransactionManager();

	private Map bindingsMap = null;
	private static transient Logger logger = Logger
			.getLogger(LocationService.class.getName());
	private Set registered = null;
	private static String ALL_REGISTRATIONS = "ALL_REGISTRATIONS";
	private TransactionManagerImpl txm = null;

	public LocationService() {
		super();
		this.txm = (TransactionManagerImpl) SleeContainer
				.getTransactionManager();
	}

	private Map getMap(String name) {

		CacheableMap map = new CacheableMap("location:" + name);
		return map;

	}

	private Set getSet(String name) {
		Set s = new CacheableSet("location:" + name);
		return s;
	}

	/**
	 * Return copy of map containing registration bindings
	 * 
	 * @param sipAddress -
	 *            address for which we want to retrieve bindings
	 * @return map(String,RegistrationBinding)
	 * @throws LocationServiceException
	 */
	public Map getBindings(String sipAddress) throws LocationServiceException {

		boolean createdTX = false;

		Map toReturn = null;
		try {
			createdTX = this.txm.requireTransaction();
			if (bindingsMap == null)
				bindingsMap = getMap(sipAddress);

			if (bindingsMap.isEmpty()) {
				toReturn = new HashMap();
			} else {
				toReturn = new HashMap(bindingsMap);
			}
		} catch (Exception e) {
			throw new LocationServiceException(e.getLocalizedMessage());
		} finally {
			if (createdTX)
				try {
					this.txm.commit();
				} catch (SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

		return toReturn;

	}

	/**
	 * Use add and remove.
	 * 
	 * @param sipAddress
	 * @param bindings
	 * @throws LocationServiceException
	 * @author baranowb
	 * @deprecated
	 */
	public void setBindings(String sipAddress, Map bindings)
			throws LocationServiceException {
		// try {
		// tm.putObject(tcache, "/location:" + sipAddress, "bindings",
		// bindings);
		// } catch (SystemException e) {
		// throw new LocationServiceException(e.getMessage());
		// }
		logger
				.info("^^^ public void setBindings(String sipAddress, Map bindings) throws LocationServiceException  { ^^^");

	}

	// TWO Methods depend on contactAddress form!!!
	/**
	 * 
	 * @param sipAddress -
	 *            sip address for which binding will be added
	 * @param binding -
	 *            bindings to be added for ueser
	 * @throws LocationServiceException -
	 *             thrown when backend storage fails in some manner
	 */
	public void addBinding(String sipAddress, RegistrationBinding binding)
			throws LocationServiceException {
		logger.info("ADD BINDING[" + sipAddress + "][" + binding + "]");
		boolean createdTX = false;
		try {
			createdTX = this.txm.requireTransaction();
			if (bindingsMap == null)
				bindingsMap = getMap(sipAddress);

			bindingsMap.put(binding.getContactAddress(), binding);
			getSet(ALL_REGISTRATIONS).add(sipAddress);
		} catch (Exception e) {
			throw new LocationServiceException(e.getLocalizedMessage());
		} finally {
			if (createdTX)
				try {
					this.txm.commit();
				} catch (SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	/**
	 * 
	 * @param sipAddress -
	 *            user for which we will perform remove.
	 * @param sipContact -
	 *            contact address that will be removed for this user
	 * @throws LocationServiceException -
	 *             thrown when backend storage fails in some manner
	 */
	public void removeBinding(String sipAddress, String sipContact)
			throws LocationServiceException {

		boolean createdTX = false;
		try {
			createdTX = this.txm.requireTransaction();
			if (bindingsMap == null)
				bindingsMap = getMap(sipAddress);

			bindingsMap.remove(sipContact);
			if (bindingsMap.size() == 0) {
				getSet(ALL_REGISTRATIONS).remove(sipAddress);
				((CacheableMap) bindingsMap).remove();
			}
		} catch (Exception e) {
			throw new LocationServiceException(e.getLocalizedMessage());
		} finally {
			if (createdTX)
				try {
					this.txm.commit();
				} catch (SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

	}

	// TODO
	/**
	 * Default constructor with defualt access level, so noone except this
	 * package can create it Noone except this package should be able to modify
	 * bindings. Thus access to bindings can be obtained via static method -
	 * which returns copy of bindings
	 */
	// LocationService()
	// {}
	/**
	 * Returns set of sip addresses of registered users.
	 */
	public Set getRegistered() {
		boolean createdTX = false;
		try {
			createdTX = this.txm.requireTransaction();
			return new HashSet(getSet(ALL_REGISTRATIONS));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (createdTX)
				try {
					this.txm.commit();
				} catch (SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return new HashSet();
	}
}
