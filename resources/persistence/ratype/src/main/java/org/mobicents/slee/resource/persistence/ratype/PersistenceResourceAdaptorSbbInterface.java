package org.mobicents.slee.resource.persistence.ratype;

import java.util.Map;

import javax.persistence.EntityManager;


public interface PersistenceResourceAdaptorSbbInterface {

	/**
	 * Informs RA that this SbbInterfaceImpl Object will no longer be used, This has to be called explicitly.
	 *
	 */
	public void close();

	
	public boolean hasBeenClosed();
	/**
	 * Requests EntityMnager for certain PU
	 * @param emfProperties - properties that are to be passed when EMF is created - discarded if EMF exists, can be set to <b>null</b>
	 * @param puName - name of persistence unit
	 * @return Object implementing EntityManager interface
	 */
	public EntityManager createEntityManager(Map emfProperties, String puName);
	/**
	 * Similar method to {@link #createEntityManager(Map, String)} , however it alows to pass properties to EM
	 * @param emfProperties - properties that are to be passed when EMF is created - discarded if EMF exists, can be set to <b>null</b>
	 * @param emProps - properties that will be passed to EM when it is created, it can be set to <b>null</b>
	 * @param puName - name of persistence unit
	 * @return
	 */
	public EntityManager createEntityManager(Map emfProperties,Map emProps, String puName);
	/**
	 * Checks if there is present EMF for PU, and if it is, it returns its EMF.isOpen();
	 * @param puName - name of persistence unit
	 * @return
	 */
	public boolean isOpen(String puName);

}
