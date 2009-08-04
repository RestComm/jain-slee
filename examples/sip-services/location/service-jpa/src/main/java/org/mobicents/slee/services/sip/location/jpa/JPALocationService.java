package org.mobicents.slee.services.sip.location.jpa;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.TransactionManager;

import org.apache.log4j.Logger;
import org.mobicents.slee.services.sip.location.LocationService;
import org.mobicents.slee.services.sip.location.LocationServiceException;
import org.mobicents.slee.services.sip.location.RegistrationBinding;
import org.mobicents.slee.services.sip.location.jmx.LocationServiceManagement;


/**
 * 
 * Location Service implemented using JPA, which can be a HA data source.
 * 
 * @author martins
 */
public class JPALocationService implements LocationService {
	
	private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("mobicents-slee-examples-sipservices-location-pu");
	
	private static final Logger logger = Logger.getLogger(LocationService.class);
	
	public void init() {		
		// init JPA EM factory
		if (entityManagerFactory == null || !entityManagerFactory.isOpen()) {
			entityManagerFactory = Persistence.createEntityManagerFactory("mobicents-slee-examples-sipservices-location-pu");
		}
		// start MBean
		LocationServiceManagement.create(this);
		logger.info("JPA Location Service started.");
	}
	
	public void shutdown() {
		// stop MBean
		LocationServiceManagement.destroy();
		// close JPA EM factory
		entityManagerFactory.close();
		entityManagerFactory = null;
		logger.info("JPA Location Service shutdown.");
	}
	
	public RegistrationBinding addBinding(String sipAddress, String contactAddress, String comment, long expires, long registrationDate, float qValue, String callId, long cSeq)
			throws LocationServiceException {
		
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		JPARegistrationBinding registrationBinding = new JPARegistrationBinding(sipAddress,contactAddress,comment,expires,registrationDate,qValue,callId,cSeq);
		try {
			entityManager.persist(registrationBinding);
			return registrationBinding;
		} catch (Exception e) {
			throw new LocationServiceException(e.getLocalizedMessage());
		} finally {
			entityManager.close();
		}
	}
	
	public Map<String,RegistrationBinding> getBindings(String sipAddress) throws LocationServiceException {

		EntityManager entityManager = entityManagerFactory
				.createEntityManager();

		Map<String, RegistrationBinding> resultMap = new HashMap<String,RegistrationBinding>();
		try {
			List resultList = entityManager.createNamedQuery(
					"selectBindingsForSipAddress").setParameter("sipAddress",
					sipAddress).getResultList();
			for (Object object : resultList) {
				RegistrationBinding registrationBinding = (RegistrationBinding) object;
				resultMap.put(registrationBinding.getContactAddress(),
						registrationBinding);
			}
		} catch (Exception e) {
			throw new LocationServiceException(e.getLocalizedMessage());
		} finally {
			entityManager.close();
		}
		return resultMap;
	}
	
	public void updateBinding(RegistrationBinding registrationBinding)
			throws LocationServiceException {
		
		EntityManager entityManager = entityManagerFactory
		.createEntityManager();

		JPARegistrationBinding jPARegistrationBinding = (JPARegistrationBinding) registrationBinding;
		
		try {
			entityManager.createNamedQuery(
			"updateBindingByKey")
				.setParameter("sipAddress",jPARegistrationBinding.getKey().getSipAddress())
				.setParameter("contactAddress", jPARegistrationBinding.getKey().getContactAddress())
				.setParameter("callId", jPARegistrationBinding.getCallId())
				.setParameter("comment", jPARegistrationBinding.getComment())
				.setParameter("cSeq", jPARegistrationBinding.getCSeq())
				.setParameter("expires", jPARegistrationBinding.getExpires())
				.setParameter("registrationDate", jPARegistrationBinding.getRegistrationDate())
				.setParameter("qValue", jPARegistrationBinding.getQValue())
				.executeUpdate();
		} catch (Exception e) {
			throw new LocationServiceException(e.getLocalizedMessage());
		} finally {
			entityManager.close();
		}
	}

	public void removeBinding(String sipAddress, String sipContact)
			throws LocationServiceException {

		EntityManager entityManager = entityManagerFactory
		.createEntityManager();

		try {
			entityManager.createNamedQuery(
			"deleteBindingsByKey")
				.setParameter("sipAddress",sipAddress)
				.setParameter("contactAddress", sipContact)
				.executeUpdate();
		} catch (Exception e) {
			throw new LocationServiceException(e.getLocalizedMessage());
		} finally {
			entityManager.close();
		}
	}	
	
	public Set<String> getRegisteredUsers() throws LocationServiceException {
		
		TransactionManager transactionManager = null;
		EntityManager entityManager = null;
		
		boolean startedTx = false;
				try {
			transactionManager = (TransactionManager) new InitialContext().lookup("java:/TransactionManager");
			if (transactionManager.getTransaction() == null) {
				transactionManager.begin();
				startedTx = true;
			}
			entityManager = entityManagerFactory
			.createEntityManager();
			HashSet<String> resultSet = new HashSet<String>();
			List resultList = entityManager.createNamedQuery(
			"selectAllBindings").getResultList();
			for (Object object:resultList) {
				JPARegistrationBinding registrationBinding = (JPARegistrationBinding) object;
				resultSet.add(registrationBinding.getKey().getSipAddress());
			}
			return resultSet;
		} catch (Exception e) {
			throw new LocationServiceException(e.getLocalizedMessage());
		}
		finally {
			if (entityManager != null) {
				try {
					entityManager.close();
				}
				catch (Exception f) {
					throw new LocationServiceException(f.getLocalizedMessage());
				}
			}
			if (startedTx && transactionManager != null) {
				try {
					transactionManager.commit();
				}
				catch (Exception f) {
					throw new LocationServiceException(f.getLocalizedMessage());
				}
			}
		}
	}

	// MBEAN RELATED METHODS
	
	public Set<String> getContacts(String sipAddress) throws LocationServiceException {
		
		TransactionManager transactionManager = null;
		try {
			transactionManager = (TransactionManager) new InitialContext().lookup("java:/TransactionManager");
			transactionManager.begin();
			return getBindings(sipAddress).keySet();		
		} catch (Exception e) {
			throw new LocationServiceException(e.getLocalizedMessage());
		}
		finally {
			if (transactionManager != null) {
				try {
					transactionManager.commit();
				}
				catch (Exception f) {
					throw new LocationServiceException(f.getLocalizedMessage());
				}
			}
		}
	}

	public long getExpirationTime(String sipAddress, String contactAddress) throws LocationServiceException {
		
		TransactionManager transactionManager = null;
		EntityManager entityManager = null;
		try {
			transactionManager = (TransactionManager) new InitialContext().lookup("java:/TransactionManager");
			transactionManager.begin();
			entityManager = entityManagerFactory.createEntityManager();
			Map<String,RegistrationBinding> bindings = getBindings(sipAddress);			
			for (String address : bindings.keySet()) {
				if (address.equals(contactAddress)) {
					return bindings.get(address).getExpiresDelta();
				}
			}
			return -1;
		} catch (Exception e) {
			throw new LocationServiceException(e.getLocalizedMessage());
		}
		finally {
			if (entityManager != null) {
				try {
					entityManager.close();
				}
				catch (Exception f) {
					throw new LocationServiceException(f.getLocalizedMessage());
				}
			}
			if (transactionManager != null) {
				try {
					transactionManager.commit();
				}
				catch (Exception f) {
					throw new LocationServiceException(f.getLocalizedMessage());
				}
			}
		}
	}

	public int getRegisteredUserCount() throws LocationServiceException {
		return getRegisteredUsers().size();
	}
	
}
