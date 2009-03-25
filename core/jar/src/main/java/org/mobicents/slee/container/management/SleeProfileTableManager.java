package org.mobicents.slee.container.management;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import javax.management.ObjectName;
import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.management.DeploymentException;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.profile.ProfileTable;
import javax.slee.profile.UnrecognizedProfileSpecificationException;
import javax.slee.profile.UnrecognizedProfileTableNameException;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.deployment.profile.SleeProfileClassCodeGenerator;
import org.mobicents.slee.container.profile.ProfileTableConcrete;
import org.mobicents.slee.container.profile.ProfileTableConcreteImpl;
import org.mobicents.slee.runtime.cache.ProfileTableCacheData;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

import EDU.oswego.cs.dl.util.concurrent.ConcurrentHashMap;

/**
 * 
 * Start time:16:56:21 2009-03-13<br>
 * Project: mobicents-jainslee-server-core<br>
 * Class that manages ProfileSpecification, profile tables, ProfileObjects. It
 * is responsible for setting up profile specification env.
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class SleeProfileTableManager {

	private static final Logger logger = Logger.getLogger(SleeProfileTableManager.class);
	private final static SleeProfileClassCodeGenerator sleeProfileClassCodeGenerator = new SleeProfileClassCodeGenerator();
	private SleeContainer sleeContainer = null;
	private SleeTransactionManager sleeTransactionManager = null;

	
	//FIXME: Alex this has to be moved into cache structure
	/**
	 * This map contains mapping - profieltable name ---> profile table concrete
	 * object. see 10.2.4 section of JSLEE 1.1 specs - there can be only single
	 * profile profile table in SLEE container 
	 * 
	 */
	private ConcurrentHashMap nameToProfileTableMap = new ConcurrentHashMap();

	public SleeProfileTableManager(SleeContainer sleeContainer) {
		super();
		if (sleeContainer == null)
			throw new NullPointerException("Parameter must not be null");
		this.sleeContainer = sleeContainer;
		this.sleeTransactionManager = this.sleeContainer.getTransactionManager();

	}

	/**
	 * Installs profile into container, creates default profile and reads
	 * backend storage to search for other profiles - it creates MBeans for all
	 * 
	 * @param component
	 * @throws DeploymentException
	 *             - this exception is thrown in case of error FIXME: on check
	 *             to profile - if we have one profile table active and we
	 *             encounter another, what shoudl happen? is there auto init for
	 *             all in back end memory?
	 */
	public void installProfile(ProfileSpecificationComponent component) throws DeploymentException {

		if (logger.isDebugEnabled()) {
			logger.debug("Installing " + component);
		}

		// FIXME:
		this.sleeTransactionManager.mandateTransaction();
		final SleeTransactionManager sleeTransactionManager = sleeContainer.getTransactionManager();
		sleeTransactionManager.mandateTransaction();

		// change classloader
		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(component.getClassLoader());

			this.sleeProfileClassCodeGenerator.process(component);

			this.createJndiSpace();
		} catch (DeploymentException de) {
			throw de;
		} catch (Throwable t) {
			throw new DeploymentException("Bad throwable, possible bug - this should be handled properly.", t);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}

	}

	public void uninstallProfile(ProfileSpecificationComponent component) {
		// TODO Auto-generated method stub

	}

	/**
	 * This creates
	 */
	private void createJndiSpace() throws DeploymentException {

	}

	public ProfileTableConcrete getProfileTable(String profileTableName) throws SLEEException, UnrecognizedProfileTableNameException {

		try {
			ProfileTableConcrete ptc = (ProfileTableConcrete) this.nameToProfileTableMap.get(profileTableName);
			if (ptc == null)
				throw new UnrecognizedProfileTableNameException();

			// FIXME: add activity check to see if we are beeing removed ?

			return ptc;
		} catch (UnrecognizedProfileTableNameException e) {
			throw e;
		} catch (Exception e) {
			throw new SLEEException("Failed to fetch ProfileTable due to unknown error, please report.", e);
		}
	}

	public SleeContainer getSleeContainer() {
		return sleeContainer;
	}

	public ProfileSpecificationComponent getProfileSpecificationComponent(ProfileSpecificationID profileSpecificationId) {
		// FIXME: we posbily dont need this.
		return this.sleeContainer.getComponentRepositoryImpl().getComponentByID(profileSpecificationId);
	}

	public ProfileTableConcrete addProfileTable(ProfileTableConcrete profileTable, ProfileSpecificationComponent component) throws TransactionRequiredLocalException, SystemException,
			ClassNotFoundException {
		return null;

	}

	public Collection<String> getDeclaredProfileTableNames() {
		return Collections.unmodifiableCollection(this.nameToProfileTableMap.keySet());
	}

	public Collection<String> getDeclaredProfileTableNames(ProfileSpecificationID id) throws UnrecognizedProfileSpecificationException {

		if (this.sleeContainer.getComponentRepositoryImpl().getComponentByID(id) == null) {
			throw new UnrecognizedProfileSpecificationException("No such profile specification: " + id);
		}
		ArrayList<String> names = new ArrayList<String>();

		// FIXME: this will fail if done async to change, is this ok ?
		Iterator<String> it = this.nameToProfileTableMap.keySet().iterator();
		while (it.hasNext()) {
			String name = it.next();
			if (((ProfileTableConcrete) this.nameToProfileTableMap.get(name)).getProfileSpecificationComponent().getProfileSpecificationID().equals(id)) {
				names.add(name);
			}
		}

		return names;
	}

	public void removeProfileTable(ProfileTableConcreteImpl profileTableConcreteImpl) {
		// TODO Auto-generated method stub

	}

	public void startAllProfileTableActivities() {
		// TODO Auto-generated method stub
		
	}

}
