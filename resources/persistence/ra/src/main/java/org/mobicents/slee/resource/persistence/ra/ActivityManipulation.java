package org.mobicents.slee.resource.persistence.ra;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.mobicents.slee.resource.persistence.ratype.SbbEntityManager;

public interface ActivityManipulation {

	public void addActivity(SbbEntityManagerImpl em);
	public void removeActivity(SbbEntityManagerImpl em);
	/**
	 * This method notifies that someone is interesetd in using EMF for particular PU. Call of this method Either returns 
	 * previosuly present EMF or creates new one using passed emfProperties.
	 * @param puName - name of persistence unit for which Entity\ManagerFactory will be created.
	 * @param emfProperties - map of propererties used to create EMF if none is present. If EMF is present it is ignored. This always can be set to <b>null</b>
	 * @return EMF
	 */
	public EntityManagerFactory subscribeForEntityManagerFactoryForPU(String puName, Map emfProperties);
	/**
	 * Notifies that soemone is no longer interesetd in using EMF for certain PU.
	 * @param puName - name of persistence unit.
	 */
	public void unsubscribeForEntityManagerFactoryForPU(String puName);
	/**
	 * Checks wheather EMF is present, and if it is, it checks if its open.
	 * @param puName - name of persistence unit.
	 * @return
	 */
	public boolean isEMFOpen(String puName);
	//public void storeEntityManagerFactory(String puName, EntityManagerFactory emf);
	
}
