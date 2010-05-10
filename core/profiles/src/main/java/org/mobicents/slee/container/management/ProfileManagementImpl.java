package org.mobicents.slee.container.management;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NamingException;
import javax.slee.SLEEException;
import javax.slee.management.DeploymentException;
import javax.slee.profile.ProfileFacility;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.profile.ProfileTableActivityContextInterfaceFactory;
import javax.slee.profile.ProfileTableAlreadyExistsException;
import javax.slee.profile.UnrecognizedProfileSpecificationException;
import javax.slee.profile.UnrecognizedProfileTableNameException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.AbstractSleeContainerModule;
import org.mobicents.slee.container.activity.ActivityContext;
import org.mobicents.slee.container.component.common.EnvEntryDescriptor;
import org.mobicents.slee.container.component.profile.ProfileSpecificationComponent;
import org.mobicents.slee.container.deployment.profile.SleeProfileClassCodeGenerator;
import org.mobicents.slee.container.deployment.profile.jpa.Configuration;
import org.mobicents.slee.container.deployment.profile.jpa.JPAProfileEntityFramework;
import org.mobicents.slee.container.deployment.profile.jpa.JPAProfileTableFramework;
import org.mobicents.slee.container.profile.ProfileObjectPoolManagement;
import org.mobicents.slee.container.profile.ProfileTableImpl;
import org.mobicents.slee.container.profile.entity.ProfileEntityFramework;
import org.mobicents.slee.container.transaction.TransactionContext;
import org.mobicents.slee.container.transaction.TransactionalAction;
import org.mobicents.slee.container.util.JndiRegistrationManager;
import org.mobicents.slee.runtime.facilities.ProfileAlarmFacilityImpl;
import org.mobicents.slee.runtime.facilities.profile.ProfileFacilityImpl;
import org.mobicents.slee.runtime.facilities.profile.ProfileTableActivityContextInterfaceFactoryImpl;

/**
 * 
 * Start time:16:56:21 2009-03-13<br>
 * Project: mobicents-jainslee-server-core<br>
 * Class that manages ProfileSpecification, profile tables, ProfileObjects. It
 * is responsible for setting up profile specification env.
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author martins
 */
public class ProfileManagementImpl extends AbstractSleeContainerModule implements ProfileManagement {

	private static final Logger logger = Logger.getLogger(ProfileManagementImpl.class);
	private final static SleeProfileClassCodeGenerator sleeProfileClassCodeGenerator = new SleeProfileClassCodeGenerator();

	
	/**
	 * This map contains mapping - profieltable name ---> profile table concrete
	 * object. see 10.2.4 section of JSLEE 1.1 specs - there can be only single
	 * profile profile table in SLEE container
	 * 
	 */
	//private ConcurrentHashMap nameToProfileTableMap = new ConcurrentHashMap();
	private ConcurrentHashMap<String, ProfileTableImpl> profileTablesLocalObjects;

	private final Configuration configuration;
	private JPAProfileTableFramework profileTableFramework;
	
	private ProfileObjectPoolManagement objectPoolManagement;
	private ProfileFacility profileFacility;
	private ProfileTableActivityContextInterfaceFactory profileTableActivityContextInterfaceFactory;
	
	public ProfileManagementImpl(Configuration configuration) {
		this.profileTablesLocalObjects = new ConcurrentHashMap<String, ProfileTableImpl>();
		this.configuration = configuration;
		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.AbstractSleeContainerModule#sleeInit()
	 */
	@Override
	public void sleeStarting() {
		this.objectPoolManagement = new ProfileObjectPoolManagement(sleeContainer);
		this.objectPoolManagement.register();
		this.profileTableActivityContextInterfaceFactory = new ProfileTableActivityContextInterfaceFactoryImpl(sleeContainer,this);
		JndiRegistrationManager.registerWithJndi("slee/facilities",
				ProfileTableActivityContextInterfaceFactoryImpl.JNDI_NAME,
				profileTableActivityContextInterfaceFactory);
		this.profileFacility = new ProfileFacilityImpl(this);		
		JndiRegistrationManager.registerWithJndi("slee/facilities", ProfileFacilityImpl.JNDI_NAME,
				profileFacility);
		// FIXME if it is a framework then it should be passed as arg in the beans xml, and if possible be independent of slee 
		this.profileTableFramework = new JPAProfileTableFramework(this, sleeContainer.getTransactionManager(), configuration);		
	}
	
	/**
	 * @return the objectPoolManagement
	 */
	public ProfileObjectPoolManagement getObjectPoolManagement() {
		return objectPoolManagement;
	}
	
	/**
	 * @return the profileFacility
	 */
	public ProfileFacility getProfileFacility() {
		return profileFacility;
	}
	
	/**
	 * @return the profileTableActivityContextInterfaceFactory
	 */
	public ProfileTableActivityContextInterfaceFactory getProfileTableActivityContextInterfaceFactory() {
		return profileTableActivityContextInterfaceFactory;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.management.ProfileManagement#installProfileSpecification(org.mobicents.slee.core.component.profile.ProfileSpecificationComponent)
	 */
	public void installProfileSpecification(ProfileSpecificationComponent component) throws DeploymentException {

		if (logger.isDebugEnabled()) {
			logger.debug("Installing " + component);
		}

		try {
			this.createJndiSpace(component);
			// FIXME: we wont use trace and alarm in 1.0 way wont we?
			ProfileEntityFramework profileEntityFramework = new JPAProfileEntityFramework(component,configuration,sleeContainer.getTransactionManager());
			profileEntityFramework.install();
			sleeProfileClassCodeGenerator.process(component);
			profileTableFramework.loadProfileTables(component);			
		} catch (DeploymentException de) {
			throw de;
		} catch (Throwable t) {
		  t.printStackTrace();
			throw new SLEEException(t.getMessage(),t);
		}

	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.management.ProfileManagement#uninstallProfileSpecification(org.mobicents.slee.core.component.profile.ProfileSpecificationComponent)
	 */
	public void uninstallProfileSpecification(ProfileSpecificationComponent component) throws UnrecognizedProfileSpecificationException {

		Collection<String> profileTableNames = getDeclaredProfileTableNames(component.getProfileSpecificationID());
		
		for(String profileTableName:profileTableNames) {
			try {
				this.removeProfileTable(profileTableName, true);
			} catch (Throwable e) {
				throw new SLEEException(e.getMessage(),e);
			}
		}
		
		component.getProfileEntityFramework().uninstall();		
	}

	/**
	 * 
	 * @param component
	 * @throws Exception
	 */
	private void createJndiSpace(ProfileSpecificationComponent component) throws Exception {
		Context ctx = (Context) new InitialContext().lookup("java:comp");

		if (logger.isTraceEnabled()) {
			logger.trace("Setting up Profile Spec env. Initial context is " + ctx);
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
		
		ProfileAlarmFacilityImpl alarmFacility = new ProfileAlarmFacilityImpl(this.sleeContainer.getAlarmManagement());
		// FIXME: Alexandre: This should be AlarmFacility.JNDI_NAME. Any problem if already bound?
		try
		{
		  facilitiesCtx.bind("alarm", alarmFacility);
		}
		catch (NamingException e) {
      // ignore.
    }
		
		
		for (EnvEntryDescriptor mEnvEntry : component.getDescriptor().getEnvEntries()) {
			Class<?> type = null;

			if (logger.isTraceEnabled()) {
				logger.trace("Got an environment entry:" + mEnvEntry);
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

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.management.ProfileManagement#getProfileTable(java.lang.String)
	 */
	public ProfileTableImpl getProfileTable(String profileTableName) throws NullPointerException, UnrecognizedProfileTableNameException {

		if (profileTableName == null) throw new NullPointerException("profile table name is null");
		
		ProfileTableImpl profileTable = null;
		if (sleeContainer.getCluster().getMobicentsCache().isLocalMode()) {
			// no replication, table may only exist in local resources
			profileTable = profileTablesLocalObjects.get(profileTableName);
			if (profileTable == null) {
				throw new UnrecognizedProfileTableNameException(profileTableName);
			}
		}
		else {
			if (!profileTableFramework.getConfiguration().isClusteredProfiles()) {
				// profiles are not clustered, table may only exist in local resources
				profileTable = profileTablesLocalObjects.get(profileTableName);
				if (profileTable == null) {
					throw new UnrecognizedProfileTableNameException();
				}
			}
			else {
				// profiles are clustered, table may exist "remotely" and not in local resources, due to runtime creation in another cluster node, we need to go to database first
				final ProfileSpecificationID profileSpecificationID = profileTableFramework.getProfileSpecificationID(profileTableName);
				if (profileSpecificationID != null) {
					// exists in database
					profileTable = profileTablesLocalObjects.get(profileTableName);
					if (profileTable == null) {
						// local resource does not exists, create it
						ProfileSpecificationComponent component = sleeContainer.getComponentRepository().getComponentByID(profileSpecificationID);
						if (component != null) {
							profileTable = addProfileTableLocally(createProfileTableInstance(profileTableName, component),false, false);
						}
					}
				}
				else {
					// does not exists in database, ensure it is not in local objects
					profileTablesLocalObjects.remove(profileTableName);
				}
			}
		}
		
		if (profileTable == null)
			throw new UnrecognizedProfileTableNameException();
		else
			return profileTable;

	}

	private ProfileTableImpl createProfileTableInstance(String profileTableName, ProfileSpecificationComponent component) {
		if (component.getProfileTableConcreteClass() == null) {
			return new ProfileTableImpl(profileTableName, component, this); 
		}
		else {
			try {
				return (ProfileTableImpl)component.getProfileTableConcreteClass().getConstructor(String.class, ProfileSpecificationComponent.class, ProfileManagementImpl.class).newInstance(profileTableName, component, this);
			} catch (Throwable e) {
				throw new SLEEException(e.getMessage(),e);
			}
		}
	}
	
	private ProfileTableImpl addProfileTableLocally(final ProfileTableImpl newProfileTable, boolean startActivity, boolean storeInFramework) throws SLEEException {
		ProfileTableImpl profileTable = profileTablesLocalObjects.putIfAbsent(newProfileTable.getProfileTableName(), newProfileTable);
		if (profileTable == null) {			
			// new profile table object was inserted
			profileTable = newProfileTable;
			newProfileTable.registerTracer();
			final TransactionContext txContext = sleeContainer.getTransactionManager().getTransactionContext();
			// add tx action to remove local object if tx rollbacks
			TransactionalAction action1 = new TransactionalAction() {
				public void execute() {
					profileTablesLocalObjects.remove(newProfileTable.getProfileTableName());				
				}
			};
			txContext.getAfterRollbackActions().add(action1);
						
			// register usage mbean
			newProfileTable.registerUsageMBean();
			TransactionalAction action3 = new TransactionalAction() {
				public void execute() {
					newProfileTable.unregisterUsageMBean();				
				}
			};
			txContext.getAfterRollbackActions().add(action3);
			
			// create object pool
			objectPoolManagement.createObjectPool(newProfileTable, sleeContainer.getTransactionManager());
			
			// the next 2 operations needs to be done before creating default profile, since
			// that operation may set tx as rollback, and if that happens
			// cache data (actually this limitation comes from jboss cache) or jpa
			// can't be initiated
			
			// start activity
			if (startActivity) {
				newProfileTable.startActivity();
			}
			if(storeInFramework) {
				// store it in jpa		
				profileTableFramework.storeProfileTable( profileTable );
			}
			
			// create default profile
			try {
				newProfileTable.createDefaultProfile();
			} catch (Throwable e) {
				throw new SLEEException(e.getMessage(),e);
			}
		}
		return profileTable;
	}
	
	/**
	 * 
	 * @return
	 */
	public Collection<String> getDeclaredProfileTableNames() {
		return profileTableFramework.getProfileTableNames();
	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws UnrecognizedProfileSpecificationException
	 */
	public Collection<String> getDeclaredProfileTableNames(ProfileSpecificationID id) throws UnrecognizedProfileSpecificationException {

		if (this.sleeContainer.getComponentRepository().getComponentByID(id) == null) {
			throw new UnrecognizedProfileSpecificationException("No such profile specification: " + id);
		}
		
		return profileTableFramework.getProfileTableNames(id);	
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.AbstractSleeContainerModule#afterSleeRunning()
	 */
	@Override
	public void afterSleeRunning() {
		super.afterSleeRunning();
		if (sleeContainer.getCluster().isHeadMember()) {
			startAllProfileTableActivities();
		}
	}
	
	/**
	 * 
	 */
	private void startAllProfileTableActivities() {
		if (logger.isDebugEnabled()) {
			logger.debug("starting all profile table activities");
		}
		for (String profileTableName : this.getDeclaredProfileTableNames()) {
			try {
				ProfileTableImpl pt = getProfileTable(profileTableName);
				ActivityContext ac = pt.getActivityContext();
				if (ac == null) {
					pt.startActivity();
				}
			}
			catch (Throwable e) {
				if (logger.isDebugEnabled()){
					logger.debug("Not starting activity for profile table named "+profileTableName+". The profile spec component is not deployed.");
				}
			}
		}
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
		
		return addProfileTableLocally(createProfileTableInstance(profileTableName, component),true,true);
				
	}

	/**
	 * 
	 * @param profileTableName
	 * @throws NullPointerException
	 * @throws UnrecognizedProfileTableNameException
	 */
	public void removeProfileTable(final String profileTableName) throws NullPointerException, UnrecognizedProfileTableNameException {
	  removeProfileTable(profileTableName, false);
	}

    private void removeProfileTable(final String profileTableName, boolean isUninstall) throws NullPointerException, UnrecognizedProfileTableNameException {
      ProfileTableImpl profileTable = getProfileTable(profileTableName);
      TransactionalAction action = new TransactionalAction() {
        public void execute() {
          profileTablesLocalObjects.remove(profileTableName);
        }
      };
      try {
        sleeContainer.getTransactionManager().getTransactionContext().getAfterCommitActions().add(action);
      } catch (IllegalStateException e) {
        throw new SLEEException(e.getMessage(),e);
      }
      profileTable.remove(isUninstall);
      if(!isUninstall) {
        profileTableFramework.removeProfileTable(profileTable.getProfileTableName());
      }
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
	  // Get the old table
	  ProfileTableImpl oldProfileTable = getProfileTable(oldProfileTableName);
	  // Create the new one
	  addProfileTable(newProfileTableName, oldProfileTable.getProfileSpecificationComponent());
	  // Move contents
	  oldProfileTable.rename(newProfileTableName);
	  // Remove the old one - this wont remove profiles/mbeans, since they are already part of new table, this has to be done
	  // above.
	  removeProfileTable(oldProfileTableName);		
	}

	@Override
	public String toString() {
		return "Profile Table Manager: " 
			+ "\n+-- Profile Tables: " + getDeclaredProfileTableNames();
	}

	/**
	 * @param profileTableName
	 * @param component
	 */
	public void loadProfileTableLocally(String profileTableName,
			ProfileSpecificationComponent component) throws IllegalArgumentException {
		if(profileTableFramework.getProfileTableNames(component.getProfileSpecificationID()).contains(profileTableName)) {
			boolean startActivity = sleeContainer.getCluster().isHeadMember();
			addProfileTableLocally(createProfileTableInstance(profileTableName, component), startActivity, false);			
		}
		else {
			throw new IllegalArgumentException("Either profile table named "+profileTableName+" does not exists or its component is not the specified one");
		}
	}

	/**
	 * @return
	 */
	public Configuration getJPAConfiguration() {
		return profileTableFramework.getConfiguration();
	}
}
