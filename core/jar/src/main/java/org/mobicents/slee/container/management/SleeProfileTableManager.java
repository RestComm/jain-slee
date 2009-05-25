package org.mobicents.slee.container.management;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NamingException;
import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.management.DeploymentException;
import javax.slee.management.ProfileTableNotification;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.profile.ProfileTableAlreadyExistsException;
import javax.slee.profile.UnrecognizedProfileSpecificationException;
import javax.slee.profile.UnrecognizedProfileTableNameException;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.MEnvEntry;
import org.mobicents.slee.container.deployment.profile.SleeProfileClassCodeGenerator;
import org.mobicents.slee.container.deployment.profile.jpa.JPAQueryBuilder;
import org.mobicents.slee.container.management.jmx.TraceMBeanImpl;
import org.mobicents.slee.container.profile.ProfileDataSource;
import org.mobicents.slee.container.profile.ProfileTableImpl;
import org.mobicents.slee.runtime.cache.ProfileManagementCacheData;
import org.mobicents.slee.runtime.facilities.ProfileAlarmFacilityImpl;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

/**
 * 
 * Start time:16:56:21 2009-03-13<br>
 * Project: mobicents-jainslee-server-core<br>
 * Class that manages ProfileSpecification, profile tables, ProfileObjects. It
 * is responsible for setting up profile specification env.
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class SleeProfileTableManager {

	private static final Logger logger = Logger.getLogger(SleeProfileTableManager.class);
	private final static SleeProfileClassCodeGenerator sleeProfileClassCodeGenerator = new SleeProfileClassCodeGenerator();
	private SleeContainer sleeContainer = null;

	// FIXME: Alex this has to be moved into cache structure
	/**
	 * This map contains mapping - profieltable name ---> profile table concrete
	 * object. see 10.2.4 section of JSLEE 1.1 specs - there can be only single
	 * profile profile table in SLEE container
	 * 
	 */
	//private ConcurrentHashMap nameToProfileTableMap = new ConcurrentHashMap();
	private ProfileManagementCacheData nameToProfileTableMap;

	public SleeProfileTableManager(SleeContainer sleeContainer) {
		super();
		if (sleeContainer == null)
			throw new NullPointerException("Parameter must not be null");
		this.sleeContainer = sleeContainer;
		this.nameToProfileTableMap=this.sleeContainer.getCache().getProfileManagementCacheData();

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
	public void installProfileSpecification(ProfileSpecificationComponent component) throws DeploymentException {

		if (logger.isDebugEnabled()) {
			logger.debug("Installing " + component);
		}

		try {
			this.createJndiSpace(component);
			// FIXME: we wont use trace and alarm in 1.0 way wont we?
			ProfileDataSource.INSTANCE.install(component);
			sleeProfileClassCodeGenerator.process(component);
			JPAQueryBuilder queryBuilder = new JPAQueryBuilder(component);
			queryBuilder.parseStaticQueries();
		} catch (DeploymentException de) {
			throw de;
		} catch (Throwable t) {
		  t.printStackTrace();
			throw new SLEEException(t.getMessage(),t);
		}

	}

	public void uninstallProfileSpecification(ProfileSpecificationComponent component) throws UnrecognizedProfileSpecificationException {
		//FIXME: Alex ?
		Collection<String> profileTableNames = getDeclaredProfileTableNames(component.getProfileSpecificationID());
		
		for(String profileTableName:profileTableNames)
		{
			try {
				this.getProfileTable(profileTableName).remove();
			} catch (TransactionRequiredLocalException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SLEEException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnrecognizedProfileTableNameException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		ProfileDataSource.INSTANCE.uninstall(component);		
	}

	/**
	 * This creates
	 */
	private void createJndiSpace(ProfileSpecificationComponent component) throws Exception {
		Context ctx = (Context) new InitialContext().lookup("java:comp");

		if (logger.isDebugEnabled()) {
			logger.debug("Setting up SBB env. Initial context is " + ctx);
		}

		Context envCtx = null;
		try {
			envCtx = ctx.createSubcontext("env");
		} catch (NameAlreadyBoundException ex) {
			envCtx = (Context) ctx.lookup("env");
		}

		Context sleeCtx = null;

		try {
			sleeCtx = envCtx.createSubcontext("slee");
		} catch (NameAlreadyBoundException ex) {
			sleeCtx = (Context) envCtx.lookup("slee");
		}

		Context facilitiesCtx = null;

		try {
			facilitiesCtx = sleeCtx.createSubcontext("facilities");
		} catch (NameAlreadyBoundException ex) {
			facilitiesCtx = (Context) sleeCtx.lookup("facilities");
		}
		
		ProfileAlarmFacilityImpl alarmFacility = new ProfileAlarmFacilityImpl(this.sleeContainer.getAlarmFacility());
		// FIXME: Alexandre: This should be AlarmFacility.JNDI_NAME. Any problem if already bound?
		try
		{
		  facilitiesCtx.bind("alarm", alarmFacility);
		}
		catch (NamingException e) {
      // ignore.
    }
		
		
		for (MEnvEntry mEnvEntry : component.getDescriptor().getEnvEntries()) {
			Class<?> type = null;

			if (logger.isDebugEnabled()) {
				logger.debug("Got an environment entry:" + mEnvEntry);
			}

			try {
				type = Thread.currentThread().getContextClassLoader().loadClass(mEnvEntry.getEnvEntryType());
			} catch (Exception e) {
				throw new DeploymentException(mEnvEntry.getEnvEntryType() + " is not a valid type for an environment entry");
			}
			Object entry = null;
			String s = mEnvEntry.getEnvEntryValue();

			try {
				if (type == String.class) {
					entry = new String(s);
				} else if (type == Character.class) {
					if (s.length() != 1) {
						throw new DeploymentException(s + " is not a valid value for an environment entry of type Character");
					}
					entry = new Character(s.charAt(0));
				} else if (type == Integer.class) {
					entry = new Integer(s);
				} else if (type == Boolean.class) {
					entry = new Boolean(s);
				} else if (type == Double.class) {
					entry = new Double(s);
				} else if (type == Byte.class) {
					entry = new Byte(s);
				} else if (type == Short.class) {
					entry = new Short(s);
				} else if (type == Long.class) {
					entry = new Long(s);
				} else if (type == Float.class) {
					entry = new Float(s);
				}
			} catch (NumberFormatException e) {
				throw new DeploymentException("Environment entry value " + s + " is not a valid value for type " + type);
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Binding environment entry with name:" + mEnvEntry.getEnvEntryName() + " type  " + entry.getClass() + " with value:" + entry + ". Current classloader = "
						+ Thread.currentThread().getContextClassLoader());
			}
			try {
				envCtx.bind(mEnvEntry.getEnvEntryName(), entry);
			} catch (NameAlreadyBoundException ex) {
				logger.error("Name already bound ! ", ex);
			}
		}

	}

	public ProfileTableImpl getProfileTable(String profileTableName) throws NullPointerException, UnrecognizedProfileTableNameException {

		if (profileTableName == null) throw new NullPointerException("profile table name is null");

		ProfileTableImpl ptc = (ProfileTableImpl) this.nameToProfileTableMap.get(profileTableName);
		if (ptc == null)
			throw new UnrecognizedProfileTableNameException();
		else
			return ptc;

	}

	public SleeContainer getSleeContainer() {
		return sleeContainer;
	}

	public ProfileSpecificationComponent getProfileSpecificationComponent(ProfileSpecificationID profileSpecificationId) {
		// FIXME: we posbily dont need this.
		return this.sleeContainer.getComponentRepositoryImpl().getComponentByID(profileSpecificationId);
	}
	
	public Collection<String> getDeclaredProfileTableNames() {
		return Collections.unmodifiableCollection(this.nameToProfileTableMap.getProfileTables());
	}

	public Collection<String> getDeclaredProfileTableNames(ProfileSpecificationID id) throws UnrecognizedProfileSpecificationException {

		if (this.sleeContainer.getComponentRepositoryImpl().getComponentByID(id) == null) {
			throw new UnrecognizedProfileSpecificationException("No such profile specification: " + id);
		}
		ArrayList<String> names = new ArrayList<String>();

		// FIXME: this will fail if done async to change, is this ok ?
		Iterator<String> it = this.getDeclaredProfileTableNames().iterator();
		while (it.hasNext()) {
			String name = it.next();
			if (((ProfileTableImpl) this.nameToProfileTableMap.get(name)).getProfileSpecificationComponent().getProfileSpecificationID().equals(id)) {
				names.add(name);
			}
		}

		return names;
	}

	public void startAllProfileTableActivities() {
		for (Object key : this.getDeclaredProfileTableNames()) {
			ProfileTableImpl pt = (ProfileTableImpl) this.nameToProfileTableMap.get((String)key);
			pt.startActivity();
		}
	}
	
	private ProfileTableImpl createProfileTableInstance(String profileTableName, ProfileSpecificationComponent component) throws SLEEException {
		ProfileTableImpl profileTable = null;
		if (component.getProfileTableConcreteClass() == null) {
			profileTable = new ProfileTableImpl(profileTableName, component, sleeContainer); 
		}
		else {
			try {
				profileTable = (ProfileTableImpl)component.getProfileTableConcreteClass().getConstructor(String.class, ProfileSpecificationComponent.class, SleeContainer.class).newInstance(profileTableName, component, sleeContainer);
			} catch (Throwable e) {
				throw new SLEEException(e.getMessage(),e);
			}
		}
		return profileTable;
	}
	
	/**
	 * 
	 * @param profileTableName
	 * @param component
	 * @return
	 * @throws ProfileTableAlreadyExistsException
	 * @throws SLEEException
	 */
	public ProfileTableImpl addProfileTable(final String profileTableName, ProfileSpecificationComponent component) throws ProfileTableAlreadyExistsException, SLEEException {
		
		try {
			getProfileTable(profileTableName);
			throw new ProfileTableAlreadyExistsException("there is already a profile table named "+profileTableName);
		}
		catch (UnrecognizedProfileTableNameException e) {
			// expected
		}
		// create instance
		final ProfileTableImpl profileTable = createProfileTableInstance(profileTableName, component);
		// map it
		this.nameToProfileTableMap.add(profileTableName, profileTable);
		// register tracer
		try {
			final String tableName = profileTable.getProfileTableName();

			TraceMBeanImpl traceMBeanImpl = sleeContainer.getTraceFacility().getTraceMBeanImpl();
			traceMBeanImpl.registerNotificationSource(new ProfileTableNotification(tableName));

			TransactionalAction action = new TransactionalAction() {
				public void execute() {
					// remove notification sources for profile table
					TraceMBeanImpl traceMBeanImpl = sleeContainer.getTraceFacility().getTraceMBeanImpl();
					traceMBeanImpl.deregisterNotificationSource(new ProfileTableNotification(tableName));
				}
			};
			sleeContainer.getTransactionManager().addAfterRollbackAction(action);
		}
		catch (SystemException e) {
			throw new SLEEException("Failure to register Tracer", e);
		}
		// register usage mbean
		profileTable.registerUsageMBean();
		TransactionalAction action = new TransactionalAction() {
			public void execute() {
				profileTable.unregisterUsageMBean();				
			}
		};
		try {
			sleeContainer.getTransactionManager().addAfterRollbackAction(action);
		} catch (SystemException e) {
			throw new SLEEException(e.getMessage(),e);
		}
		// create object pool
		sleeContainer.getProfileObjectPoolManagement().createObjectPool(profileTable, sleeContainer.getTransactionManager());
		// start activity
		profileTable.startActivity();
		// add default profile
		try {
			profileTable.createDefaultProfile();
		} catch (Throwable e) {
			throw new SLEEException(e.getMessage(),e);
		}
		return profileTable;
	}

	/**
	 * 
	 * @param profileTableName
	 * @throws NullPointerException
	 * @throws UnrecognizedProfileTableNameException
	 */
	public void removeProfileTable(String profileTableName) throws NullPointerException, UnrecognizedProfileTableNameException {
		ProfileTableImpl profileTable = getProfileTable(profileTableName);
		nameToProfileTableMap.remove(profileTableName);
		profileTable.remove();
	}

	/**
	 * 
	 * @param oldProfileTableName
	 * @param newProfileTableName
	 * @throws ProfileTableAlreadyExistsException
	 * @throws NullPointerException
	 * @throws UnrecognizedProfileTableNameException
	 */
	public void renameProfileTable(String oldProfileTableName, String newProfileTableName) throws ProfileTableAlreadyExistsException, NullPointerException, UnrecognizedProfileTableNameException {
		ProfileSpecificationComponent component = getProfileTable(oldProfileTableName).getProfileSpecificationComponent();
		removeProfileTable(oldProfileTableName);
		addProfileTable(newProfileTableName, component);		
	}

}
