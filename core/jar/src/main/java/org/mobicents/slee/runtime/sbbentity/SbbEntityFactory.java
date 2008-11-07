/*
 * Created on Feb 3, 2005
 * 
 * The Mobicents Open SLEE Project
 * 
 * A SLEE for the People
 * 
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 */
package org.mobicents.slee.runtime.sbbentity;

import java.rmi.dgc.VMID;

import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.transaction.SystemException;
import javax.transaction.TransactionRequiredException;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

/**
 * 
 * SbbEntityFactory -- implements a map from SBB Entity Id to SBB Entity instances.
 * This is the sole place where Sbb Entities are created and destroyed.
 * We keep a map of SBB Entity ID to SBB Entity here and return Sbb Entities that
 * are stored in this map.
 * 
 * @author F.Moggia
 * @author M. Ranganathan
 * @author Tim Fox  ( streamlining )
 * @author eduardomartins
 */
public class SbbEntityFactory {

	private static Logger log = Logger.getLogger(SbbEntityFactory.class);

	private static String genId() {
		//Use a GUID for the sbb entity id - a counter won't work in a cluster
		VMID vmid = new VMID();
		return vmid.toString();
	}

	/**
	 * Creates a new non root sbb entity.
	 * @param sbbId
	 * @param svcId
	 * @param parentSbbEntityId
	 * @param parentChildRelation
	 * @param rootSbbEntityId
	 * @param convergenceName
	 * @return
	 */
	public static SbbEntity createSbbEntity(SbbID sbbId, ServiceID svcId,
			String parentSbbEntityId, String parentChildRelation,
			String rootSbbEntityId, String convergenceName) {
		try {
			// generate sbb entity id
			String sbbeId = svcId.toString() + ":nonroot:" + genId();
			if (log.isDebugEnabled())
				log.debug("Creating non root sbb entity with id:" + sbbeId);
			// create sbb entity
			SbbEntity sbbe = new SbbEntity(sbbeId, parentSbbEntityId,
					parentChildRelation, rootSbbEntityId, sbbId,
					convergenceName, svcId);
			// store it in the tx
			SleeContainer.getTransactionManager().putTxLocalData(sbbeId, sbbe);
			return sbbe;
		} catch (Exception ex) {
			String s = "Exception in creating non root sbb entity!";
			log.error(s, ex);
			throw new RuntimeException(s, ex);
		}
	}

	/**
	 * Creates a new root sbb entity
	 * @param sbbId
	 * @param svcId
	 * @param convergenceName
	 * @return
	 */
	public static SbbEntity createRootSbbEntity(SbbID sbbId, ServiceID svcId,
			String convergenceName) {
		try {
			// generate sbb entity id
			String sbbeId = svcId.toString() + ":root:" + genId();
			if (log.isDebugEnabled())
				log.debug("Creating root sbb entity with id:" + sbbeId);
			// create sbb entity
			SbbEntity sbbe = new SbbEntity(sbbeId, null, null, sbbeId, sbbId,
					convergenceName, svcId);
			// store it in the tx
			SleeContainer.getTransactionManager().putTxLocalData(sbbeId, sbbe);
			return sbbe;
		} catch (Exception ex) {
			String s = "exception in creating root sbb entity!";
			log.error(s, ex);
			throw new RuntimeException(s, ex);
		}
	}

	/**
	 * Call this function when you want to get an instantiated SbbEntity from the
	 * cache.
	 * 
	 * @param sbbeId
	 * @return
	 */
	public static SbbEntity getSbbEntity(String sbbeId) {

		if (sbbeId == null)
			throw new NullPointerException("Null Sbbeid");

		/* NB!! We must not use a map to cache sbb entities.
		 * There can be multiple active transactions each of which accesses the same sbb entity
		 * at any one time in the SLEE.
		 * Therefore, by using a map to store the sbb entities one tx can see the transactional
		 * state of the other tx before it has committed.
		 * I.e. we would have no transaction isolation.
		 * Instead, if we want to cache the sbb entity for the lifetime of the tx for
		 * performance reasons, we need to store in a *per transaction* cache
		 */

		//Look for it in the per transaction cache
		SleeTransactionManager txMgr = SleeContainer.getTransactionManager();
		SbbEntity sbb = (SbbEntity) txMgr.getTxLocalData(sbbeId);
		if (sbb == null) {
			if (log.isDebugEnabled())
				log.debug("Loading sbb entity " + sbbeId + " from cache");
			// not found, recreate it from cache
			sbb = new SbbEntity(sbbeId);
			// and store in local tx data
			txMgr.putTxLocalData(sbbeId, sbb);
		}

		return sbb;
	}

	/**
	 * Removes the specified sbb entity.
	 * @param sbbEntity the sbb entity to remove
	 * @param removeFromParent indicates if the entity should be remove from it's parent also
	 * @throws TransactionRequiredException
	 * @throws SystemException
	 */
	public static void removeSbbEntity(SbbEntity sbbEntity,
			boolean removeFromParent) throws TransactionRequiredException,
			SystemException {
		// remove entity
		sbbEntity.remove(removeFromParent);
	}

	/**
	 * Removes the specified sbb entity.
	 * @param sbbEntity the sbb entity to remove
	 * @param removeFromParent indicates if the entity should be remove from it's parent also
	 * @throws TransactionRequiredException
	 * @throws SystemException
	 */
	public static void removeSbbEntity(String sbbEntityId,
			boolean removeFromParent) throws TransactionRequiredException,
			SystemException {
		removeSbbEntity(getSbbEntity(sbbEntityId),removeFromParent); 
	}

}