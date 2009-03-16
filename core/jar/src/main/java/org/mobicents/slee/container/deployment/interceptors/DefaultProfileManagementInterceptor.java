/**
 * Start time:09:19:21 2009-03-16<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.deployment.interceptors;

import java.beans.Introspector;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.slee.Address;
import javax.slee.AddressPlan;
import javax.slee.InvalidStateException;
import javax.slee.SLEEException;
import javax.slee.management.ManagementException;
import javax.slee.profile.ProfileID;
import javax.slee.profile.ProfileImplementationException;

import javax.slee.profile.ProfileVerificationException;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.deployment.ClassUtils;
import org.mobicents.slee.container.profile.ProfileConcrete;
import org.mobicents.slee.container.profile.ProfileLocalObjectConcrete;
import org.mobicents.slee.container.profile.ProfileObject;
import org.mobicents.slee.container.profile.ProfileTableConcreteImpl;
import org.mobicents.slee.container.profile.SleeProfileManagement;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.activity.ActivityContextInterfaceImpl;
import org.mobicents.slee.runtime.eventrouter.DeferredEvent;
import org.mobicents.slee.runtime.facilities.profile.ProfileAddedEventImpl;
import org.mobicents.slee.runtime.facilities.profile.ProfileTableActivityContextInterfaceFactoryImpl;
import org.mobicents.slee.runtime.facilities.profile.ProfileTableActivityHandle;
import org.mobicents.slee.runtime.facilities.profile.ProfileTableActivityImpl;
import org.mobicents.slee.runtime.facilities.profile.ProfileUpdatedEventImpl;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

/**
 * Start time:07:19:21 2009-03-16<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class DefaultProfileManagementInterceptor implements ProfileManagementInterceptor {

	private static final Logger logger = Logger.getLogger(ProfileManagementInterceptor.class);
	public static final String COMMIT_METHOD_NAME = "commitChanges";
	/**
	 * Object for which we intercept - concrete impl of CMP interface
	 */
	private Object profile = null;
	/**
	 * Profile Object for which this interceptor works.
	 */
	private ProfileObject profileObject = null;
	//Commit this to seee hwere JPA need to be plugged
	private Object profileTransientState = null;
	private Object profileContext = null;
	private SleeProfileManagement sleeProfileManagement = null;
	private String profileName = null;
	private String profileTableName = null;
	private ProfileSpecificationComponent profileSpecificationComponent = null;

	private Map<String, Field> fieldsMap = null;

	/**
	 * Flag indicating that profile has been modified, if so, we should store.
	 */
	private boolean modified = false;

	/**
	 * This flag points to ProfileMBean. it is used to determine if profiel has
	 * been edited.
	 */
	private boolean writeable = false;

	private boolean profileInBackEndStorage = false;

	public void setProfileInBackEndStorage(boolean profileInBackEndStorage) {
		this.profileInBackEndStorage = profileInBackEndStorage;
	}

	public Object getProfile() {
		return this.profile;
	}

	public ProfileObject getProfileObject() {
		return profileObject;
	}

	public void setProfileObject(ProfileObject profileObject) {
		this.profileObject = profileObject;
	}

	public SleeProfileManagement getProfileManagement() {
		return this.sleeProfileManagement;
	}

	public String getProfileName() {
		return this.profileName;
	}

	public String getProfileTableName() {
		return this.profileTableName;
	}

	public void setProfile(Object profile) {
		this.profile = profile;
	}

	public void setProfileManager(SleeProfileManagement profileManagement) {
		this.sleeProfileManagement = profileManagement;

	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;

	}

	public void setProfileSpecificationComponent(ProfileSpecificationComponent profileSpecificationComponent) {
		this.profileSpecificationComponent = profileSpecificationComponent;

	}

	public ProfileSpecificationComponent getProfileSpecificationComponent() {
		return this.profileSpecificationComponent;
	}

	public void setProfileTableName(String profileTableName) {
		this.profileTableName = profileTableName;

	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		SleeTransactionManager txManager = this.sleeProfileManagement.getSleeContainer().getTransactionManager();

		boolean createdTransaction = false;
		boolean rollback = true;
		try {
			createdTransaction = txManager.requireTransaction();
			Object result = processInvoke(proxy, method, args);
			rollback = false;
			return result;
		} finally {
			if (createdTransaction) {
				if (rollback) {
					txManager.rollback();
				} else {
					txManager.commit();
				}
			}
		}
	}

	/**
	 * @param proxy
	 * @param method
	 * @param args
	 * @return Object result of the invocation
	 * @throws Throwable
	 */
	private Object processInvoke(Object proxy, Method method, Object[] args) throws Throwable {

		// FIXME: should we change CL here? in case of call from MBean we
		// propably wont have to. however if its from Sbb ?
		Thread runningThread = Thread.currentThread();
		ClassLoader oldClassLoader = runningThread.getContextClassLoader();
		runningThread.setContextClassLoader(this.profileSpecificationComponent.getClassLoader());
		SleeTransactionManager transactionManager = this.sleeProfileManagement.getSleeContainer().getTransactionManager();
		try {
			if (profileTransientState == null) {

				// This should be legal?
				Class profileTransientStateClass = this.profileSpecificationComponent.getProfilePersistanceTransientStateConcreteClass();
				profileTransientState = profileTransientStateClass.newInstance();
				// Put all the fields into a hashmap for easier retrieval
				populateFieldsMap();

			}

			String methodName = method.getName();
			// lets run through methods
			if (logger.isDebugEnabled()) {
				logger.debug("Profile Management. Method: " + methodName + " args:" + (args == null ? null : Arrays.toString(args)) + ", state: " + this);
			}

			/*
			 * 10.26.3.6 isProfileDirty method
			 */
			if (method.getName().equals("isProfileDirty")) {
				/*
				 * The isProfileDirty method returns true if the Profile MBean
				 * object is in the read-write state and the dirty flag of the
				 * Profile Management object that caches the persistent state of
				 * the Profile returns true. This method returns false under any
				 * other situation.
				 */
				if (logger.isDebugEnabled())
					logger.debug("isProfileDirty called (profile=" + profileTableName + "/" + profileName + ")");
				if (this.writeable && this.modified)
					return new Boolean(true);
				else
					return new Boolean(false);
			}
			/*
			 * 10.26.3.5 isProfileWriteable method
			 */
			if (method.getName().equals("isProfileWriteable")) {
				/*
				 * The isProfileWriteable method returns true if the Profile
				 * MBean object is in the read-write state, or false if in the
				 * read-only state.
				 */
				if (logger.isDebugEnabled())
					logger.debug("isProfileWriteable called (profile =" + profileTableName + "/" + profileName + ")");
				return new Boolean(this.writeable);
			}
			/*
			 * 10.26.3.2 commitProfile method
			 */
			if (method.getName().equals("commitProfile")) {
				commitProfile();
				// commit profile does not return a value
				return null;
			}
			/*
			 * 10.26.3.3 restoreProfile method
			 */
			if (method.getName().equals("restoreProfile")) {
				/*
				 * The Administrator invokes the restoreProfile method if the
				 * Administrator wishes to discard changes made to the Profile
				 * Management object that caches the persistent state of a
				 * Profile. The implementation of this method rolls back any
				 * changes that have been made to the Profile Management object
				 * since the Profile MBean object entered the read-write state
				 * and moves the Profile MBean object to the read-only state. If
				 * the Profile MBean object was returned by the createProfile
				 * method (see Section 14.11), then no new Profile is created
				 * since the transaction will not commit The execution of this
				 * method must begin in the same transaction context as that
				 * begun by the createProfile or editProfile invocation that
				 * initiated the editing session, but must roll back the
				 * transaction before returning.
				 */
				if (logger.isDebugEnabled()) {
					logger.debug("restoreProfile called (profile =" + profileTableName + "/" + profileName + ")");
					logger.debug("profileWriteable " + this.writeable);
					logger.debug("dirtyFlag " + this.modified);
				}
				/*
				 * The restoreProfile method must throw a
				 * javax.slee.InvalidStateException if the Profile MBean object
				 * is not in the read-write state.
				 */
				if (!this.writeable)
					throw new InvalidStateException();
				// rollback everything
				// sleeProfileManager.rollbackTransaction(profileKey);
				transactionManager.rollback();
				transactionManager.begin();
				// and then restore the values that were last comitted into the
				// transient state class
				profileLoad();
				this.writeable = false;
				if (logger.isDebugEnabled()) {
					logger.debug("profileWriteable " + this.writeable);
					logger.debug("dirtyFlag " + this.modified);
					logger.debug("restoreProfile call ended");
				}
				return null;
			}
			/*
			 * SLEE 1.1 spec, 10.26.3.4 closeProfile method
			 */
			if (method.getName().equals("closeProfile")) {
				/*
				 * The Administrator invokes the closeProfile method when the
				 * Administrator no longer requires access to the Profile MBean
				 * object. The implementation of this method is free to
				 * deregister the Profile MBean object from the MBean Server. (
				 * but if you do this then test # 4386 will fail! )
				 */
				if (logger.isDebugEnabled()) {
					logger.debug("closeProfile called (profile =" + profileTableName + "/" + profileName + ")");
					logger.debug("profileWriteable " + this.writeable);
					logger.debug("dirtyFlag " + this.modified);
				}
				/*
				 * The closeProfile method must throw a
				 * javax.slee.InvalidStateException if the Profile MBean object
				 * is in the read-write state.
				 */
				if (this.writeable)
					throw new InvalidStateException();

				// Jean -- Should close imply unregister ? I think not.
				// sleeProfileManager.unregisterProfileMBean(profileKey);
				if (logger.isDebugEnabled()) {
					logger.debug("profileWriteable " + this.writeable);
					logger.debug("dirtyFlag " + this.modified);
					logger.debug("closeProfile call ended");
				}
				return null;
			}
			// Check the Profile MBean Life Cycle in the JAIN SLEE spec 1.1
			/*
			 * 10.26.3.1 editProfile method
			 */
			if (method.getName().equals("editProfile")) {
				/*
				 * The Administrator invokes the editProfile method to obtain
				 * read-write access to the Profile MBean object (if the
				 * Administrator currently has read-only access to the Profile
				 * MBean object).
				 */
				if (logger.isDebugEnabled()) {
					logger.debug("editProfile called (profile =" + profileTableName + "/" + profileName + ")");
					logger.debug("profileWriteable " + this.writeable);
					logger.debug("dirtyFlag " + this.modified);
				}

				if (!this.writeable) {
					if (logger.isDebugEnabled())
						logger.debug("starting new Transaction and editing profile");
					/*
					 * The implementation of this method should start a new
					 * transaction for the editing session, or perform the
					 * equivalent function.
					 */
					// sleeProfileManager.startTransaction(profileKey);
					// sleeProfileManager.startTransaction();
					// boolean b = txManager.requireTransaction();
					this.writeable = true;
					this.profileObject.profileLoad();
					// if ( b ) txManager.commit();
				}
				/*
				 * If the Profile MBean object is already in the read-write
				 * state when this method is invoked, this method has no further
				 * effect and returns silently.
				 */
				else {
					logger.debug("profile already in the read/write state");
				}
				if (logger.isDebugEnabled()) {
					logger.debug("profileWriteable " + this.writeable);
					logger.debug("dirtyFlag " + this.modified);
					logger.debug("editProfile call ended");
				}
				return null;
			}
			// ProfileManagement Methods
			if (method.getName().equals("markProfileDirty")) {
				if (logger.isDebugEnabled()) {
					logger.debug("markProfileDirty called (profile =" + profileTableName + "/" + profileName + ")");
					logger.debug("profileWriteable " + this.writeable);
					logger.debug("dirtyFlag " + this.modified);
				}

				this.modified = true;

				if (logger.isDebugEnabled()) {
					logger.debug("profileWriteable " + this.writeable);
					logger.debug("dirtyFlag " + this.modified);
					logger.debug("markProfileDirty call ended");
				}

				return null;
			}

			if (methodName.compareTo("setProfileContext") == 0) {
				// XXX: npe check is done when ProfileObject is created.

				this.profileContext = args[0];
				return null;
			}
			if (methodName.compareTo("getProfileContext") == 0) {
				return this.profileContext;

			}
			if (methodName.compareTo("profileInitialize") == 0) {
				profileInitialize(proxy, method, args);
				this.modified = true;

				return null;

			}

			if (methodName.compareTo("profilePostCreate") == 0) {
				// FIXME: is there anything we should do? this is called once
				// state is copied from default profile for newwly created
				// profile. for now we do nothing, but just in case lets clear
				return null;
			}

			// This is called when ProfileObject changes its "affinity", meaning
			// it possibly can change profile for which it , we need to load,
			// load MUST run in transaction so.
			if (methodName.compareTo("profileActivate") == 0) {
				// This method is invoked with an unspecified transaction
				// context. The Profile object cannot access its persistent CMP
				// state or invoke mandatory transactional methods during this
				// method invocation. FIXME: how can we ensure that no TX
				// methods are called ?

				// FIXME: should we call load here from PO?
				this.modified = false;
				this.profileStore();
				return null;
			}

			if (methodName.compareTo("profilePassivate") == 0) {
				// This method is invoked with an unspecified transaction
				// context. The Profile object cannot access its persistent CMP
				// state or invoke mandatory transactional methods during this
				// method invocation. FIXME: how can we ensure that no TX
				// methods are called ?

				if (this.modified) {
					this.modified = false;
					this.profileStore();
				}
				return null;
			}

			if (methodName.compareTo("profileLoad") == 0) {

				// FIXME: mandate transaction
				try {
					transactionManager.mandateTransaction();
				} catch (Exception e) {
					throw new SLEEException("There is no runnign transaction", e);
				}

				profileLoad();

				return null;
			}

			if (methodName.compareTo("profileStore") == 0) {

				// FIXME: mandate transaction
				try {
					transactionManager.mandateTransaction();
				} catch (Exception e) {
					throw new SLEEException("There is no runnign transaction", e);
				}

				profileStore();

				return null;
			}

			if (methodName.compareTo("profileRemove") == 0) {

				// FIXME: mandate transaction
				try {
					transactionManager.mandateTransaction();
				} catch (Exception e) {
					throw new SLEEException("There is no runnign transaction", e);
				}
				this.modified = false;
				profileRemove();

				return null;
			}

			if (methodName.compareTo("profileVerify") == 0) {

				// FIXME: mandate transaction
				try {
					transactionManager.mandateTransaction();
				} catch (Exception e) {
					throw new SLEEException("There is no runnign transaction", e);
				}
				// FIXME: profileStore() must be invoked withing the same
				// transaction??
				this.profileObject.profileStore();
				profileVerify();

				return null;
			}

			if (methodName.compareTo(this.COMMIT_METHOD_NAME) == 0) {
				try {
					transactionManager.mandateTransaction();
					commitChanges();
				} catch (Exception e) {
					throw new SLEEException("There is no runnign transaction", e);
				}

				return null;

			}

			if (isAccessor(proxy, method, args)) {

				if (this.profileObject.isCanAccessCMP()) {
					throw new IllegalStateException("Can not access CMP state at this time.");
				}

				if (method.getName().startsWith(ClassUtils.GET_PREFIX)) {

					final String fieldName = Introspector.decapitalize(method.getName().substring(3));
					Field field;
					if (SleeContainer.isSecurityEnabled())
						field = (Field) AccessController.doPrivileged(new PrivilegedAction() {
							public Object run() {
								try {
									return profileTransientState.getClass().getDeclaredField(fieldName);
								} catch (Exception e) {
									throw new RuntimeException(e);
								}
							}
						});
					else
						field = profileTransientState.getClass().getDeclaredField(fieldName);

					if (logger.isDebugEnabled())
						logger.debug("accessor " + method.getName().substring(3) + " called, value=" + field.get(profileTransientState));

					return field.get(profileTransientState);

				} else {

					//
					if (!this.writeable && this.profileObject.isManagementView())
						throw new InvalidStateException();
					// if a sbb tries to set a value, it is not authorized
					if (!this.profileObject.isManagementView() && this.profileObject.isProfileWriteable())
						throw new UnsupportedOperationException();
					if (logger.isDebugEnabled()) {
						logger.debug(methodName + " value " + args[0]);
					}
					final String fieldName = Introspector.decapitalize(method.getName().substring(3));
					Field field;
					if (SleeContainer.isSecurityEnabled())
						field = (Field) AccessController.doPrivileged(new PrivilegedAction() {
							public Object run() {
								try {
									return profileTransientState.getClass().getDeclaredField(fieldName);
								} catch (Exception e) {
									throw new RuntimeException(e);
								}
							}
						});
					else
						field = profileTransientState.getClass().getDeclaredField(fieldName);

					if (logger.isDebugEnabled()) {
						logger.debug("setValue" + args[0]);
					}
					field.set(profileTransientState, args[0]);
					this.modified = true;
					return null;
				}
			}

			// FIXME: is this even allowed ?
			Class[] parameters = new Class[args.length];
			for (int i = 0; i < args.length; i++)
				parameters[i] = args[i].getClass();
			if (method.getName().startsWith("set")) {
				if (this.profileObject.isCanAccessCMP()) {
					throw new IllegalStateException("Can not access CMP state at this time.");
				}
				if (!this.writeable && this.profileObject.isManagementView())
					throw new InvalidStateException();
				// if a sbb tries to set a value, it is not authorized
				if (!this.profileObject.isManagementView() && this.profileObject.isProfileWriteable())
					throw new UnsupportedOperationException();
				this.modified = true;
			}
			try {
				return callSuperMethod(profile, methodName, args);
			} catch (Exception e) {
				throw new ProfileImplementationException(e);
			}

		} finally {
			runningThread.setContextClassLoader(oldClassLoader);
		}
	}

	// ##################
	// # Helper methods #
	// ##################
	private void profileVerify() {
		// TODO Auto-generated method stub

	}

	private void profileRemove() {
		// TODO Auto-generated method stub

	}

	private void profileStore() {

	}

	/**
	 * This method loads profile transient state from backend storage
	 */
	private void profileLoad() {
		// TODO Auto-generated method stub

	}

	public void commitChanges() {
		// TODO Auto-generated method stub
		if(this.modified)
		{
			//FIXME: add update DB
		}
		this.modified = false;

	}

	
	
	/**
	 * 
	 * @param object
	 * @param methodName
	 * @param args
	 * @return @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws ProfileImplementationException
	 */
	protected Object callSuperMethod(Object object, String methodName, Object[] args) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, ProfileImplementationException {

		Class[] parameters = new Class[args.length];
		for (int i = 0; i < args.length; i++)
			if (!args[i].getClass().isPrimitive())
				parameters[i] = getPrimitiveTypeFromClass(args[i].getClass());
			else
				parameters[i] = getPrimitiveTypeFromClass(args[i].getClass());
		Method superMethod = object.getClass().getSuperclass().getMethod(methodName, parameters);
		Object result = superMethod.invoke(object, args);
		return result;
	}

	/**
	 * 
	 * @param argumentType
	 * @return
	 */
	public static Class getPrimitiveTypeFromClass(Class argumentType) {
		if (argumentType.equals(Integer.class))
			return int.class;
		if (argumentType.equals(Boolean.class))
			return boolean.class;
		if (argumentType.equals(Byte.class))
			return byte.class;
		if (argumentType.equals(Character.class))
			return char.class;
		if (argumentType.equals(Double.class))
			return double.class;
		if (argumentType.equals(Float.class))
			return float.class;
		if (argumentType.equals(Long.class))
			return long.class;
		if (argumentType.equals(Short.class))
			return short.class;
		return argumentType;
	}

	/**
	 * @param proxy
	 * @param method
	 * @param args
	 * @return
	 */
	private boolean isAccessor(Object proxy, Method method, Object[] args) {
		String fieldName = method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4);

		if (logger.isDebugEnabled()) {
			logger.debug("Looking up field for method: " + method.getName() + ", deducted field name: " + fieldName + ", field map: " + this.fieldsMap);
		}

		return this.fieldsMap.containsKey(fieldName);
	}

	/**
	 * The execution of the commitProfile method (and profileVerify callback
	 * methods of the Profile Management object) must run in the same
	 * transaction context as that begun by the edit- Profile invocation that
	 * initiated the editing session. The transaction should only be committed
	 * if the commitProfile method returns successfully, i.e. without throwing
	 * an exception.
	 * 
	 * The commitProfile method must throw a javax.slee.InvalidStateException if
	 * the Profile MBean object is not in the read-write state.
	 * 
	 * @throws InvalidStateException
	 * 
	 */
	private void commitProfile() throws InvalidStateException, ProfileVerificationException, ManagementException {
		SleeTransactionManager txManager = this.sleeProfileManagement.getSleeContainer().getTransactionManager();
		boolean b = false;

		b = txManager.requireTransaction();

		try {
			if (!this.writeable)
				throw new InvalidStateException();

			if (logger.isDebugEnabled()) {
				logger.debug("commitProfile called (profile =" + profileTableName + "/" + profileName + ")");
				logger.debug("profileWriteable " + this.writeable);
				logger.debug("dirtyFlag " + this.modified);
			}

			// not storing default profile
			// getting last commited profile in case of update
			ProfileLocalObjectConcrete profileBeforeUpdate = null;
			if (this.isProfileInBackEndStorage()) {
				try {
					ProfileTableConcreteImpl profileTable = (ProfileTableConcreteImpl) this.sleeProfileManagement.getProfileTable(this.getProfileTableName(), this.getProfileSpecificationComponent()
							.getProfileSpecificationID());
					profileBeforeUpdate = (ProfileLocalObjectConcrete) profileTable.find(this.getProfileName(), true);
					profileBeforeUpdate.setSnapshot();

				} catch (Exception e1) {
					throw new ManagementException("Failed instantiateLastCommittedProfile ", e1);
				}
			}
			/*
			 * The implementation of this method must also verify that the
			 * constraints specified by the Profile Specification?s deployment
			 * descriptor, such as the uniqueness constraints placed on indexed
			 * attributes. The SLEE verifies these constraints after it invokes
			 * the profileVerify method of the Profile Management object. If any
			 * constraint is violated, then this method throws a javax.slee.
			 * profile.ProfileVerificationException, the commit attempt fails,
			 * and the Profile MBean object must remain in the read-write state.
			 */

			try {
				this.profileObject.profileStore();
			} catch (Exception e) {
				e.printStackTrace();
				if (e instanceof ProfileVerificationException)
					throw (ProfileVerificationException) e;
				throw new ManagementException(e.getMessage());
			}

			/*
			 * The implementation of this method must invoke the profileVerify
			 * method of the Profile Management object that caches the
			 * persistent state of the Profile, and only commit the changes if
			 * the profileVerify method returns without throwing an exception.
			 * If the profileVerify method throws a
			 * javax.slee.profile.ProfileVerificationException, the commit
			 * attempt should fail, the exception is forwarded back to the
			 * management client, and the Profile MBean object must remain in
			 * the read-write state.
			 */
			// FIXME: add different check?
			if (this.getProfileName() != null || this.profileObject.isSbbInvoked())
				try {
					this.profileObject.profileVerify();
				} catch (Exception e) {
					throw new ProfileVerificationException(e.getMessage());
				}
			/*
			 * if(profileVerificationException!=null){ removeException=true;
			 * throw profileVerificationException; }
			 */

			try {
				// persist transient state
				commitChanges();
			} catch (Exception e3) {
				logger.error("Failed commitProfile, profileStore()", e3);
				if (e3 instanceof ProfileVerificationException)
					throw (ProfileVerificationException) e3;
				else
					throw new ManagementException("Failed commitProfile, profileStore()", e3);
			}

			// Fire a Profile Added or Updated Event
			SleeContainer serviceContainer = SleeContainer.lookupFromJndi();

			Address profileAddress = new Address(AddressPlan.SLEE_PROFILE, profileTableName + "/" + profileName);
			ProfileTableActivityContextInterfaceFactoryImpl profileTableActivityContextInterfaceFactory;

			profileTableActivityContextInterfaceFactory = serviceContainer.getProfileTableActivityContextInterfaceFactory();
			if (profileTableActivityContextInterfaceFactory == null) {
				final String s = "got NULL ProfileTable ACI Factory";
				logger.error(s);
				throw new ManagementException(s);
			}

			ProfileTableActivityImpl profileTableActivity = new ProfileTableActivityImpl(new ProfileTableActivityHandle(profileTableName));
			ActivityContextInterfaceImpl activityContextInterface;
			try {
				activityContextInterface = (ActivityContextInterfaceImpl) profileTableActivityContextInterfaceFactory.getActivityContextInterface(profileTableActivity);
			} catch (Exception e1) {
				throw new ManagementException("Failed committing profile", e1);
			}
			if (!this.isProfileInBackEndStorage()) {
				// Fire the added event only when the transaction commits
				ProfileAddedEventImpl profileAddedEvent = new ProfileAddedEventImpl(profileAddress, new ProfileID(profileAddress), profile, activityContextInterface,
						profileTableActivityContextInterfaceFactory);
				ActivityContext ac = activityContextInterface.getActivityContext();
				ac.fireEvent(new DeferredEvent(profileAddedEvent.getEventTypeID(), profileAddedEvent, ac, profileAddress));

				if (logger.isDebugEnabled()) {
					logger.debug("Queued following profile added event:" + profileAddedEvent.getEventTypeID() + ",:" + activityContextInterface.getActivityContext().getActivityContextId());
				}

				this.setProfileInBackEndStorage(true);
			} else {
				// Fire the updated event only when the transaction commits
				ProfileUpdatedEventImpl profileUpdatedEvent = new ProfileUpdatedEventImpl(profileAddress, new ProfileID(profileAddress), profileBeforeUpdate, profile, activityContextInterface,
						profileTableActivityContextInterfaceFactory);
				ActivityContext ac = activityContextInterface.getActivityContext();
				ac.fireEvent(new DeferredEvent(profileUpdatedEvent.getEventTypeID(), profileUpdatedEvent, ac, profileAddress));
				if (logger.isDebugEnabled()) {
					logger.debug("Queued following updated event: "

					+ profileUpdatedEvent.getEventTypeID() + ",:" + activityContextInterface.getActivityContext().getActivityContextId());
				}
				this.setProfileInBackEndStorage(true);
			}

			// so far so good, time to commit the tx so that the profile is
			// visible in the SLEE
			try {
				if (b)
					txManager.commit();
			}
			/*
			 * If a commit fails due to a system-level failure, the
			 * implementation of this method should throw a
			 * javax.slee.management.ManagementException to report the
			 * exceptional situation to the management client. The Profile MBean
			 * object should continue to remain in the read-write state.
			 */
			catch (Exception e) {
				logger.error("Failed committing profile", e);
				try {
					txManager.rollback();
				} catch (SystemException e2) {
					logger.error("Failed rolling back profile: " + profileTableName + "/" + profileName, e2);
					throw new ManagementException(e.getMessage());
				}
				throw new ManagementException(e.getMessage());
			}
			/*
			 * If a commit succeeds, the Profile MBean object should move to the
			 * read-only state. The SLEE must also fire a Profile Updated Event
			 * if a Profile has been updated (see Section 1.1). The dirty flag
			 * in the Profile Management object must also be set to false upon a
			 * successful commit.
			 */
			this.writeable = false;
			this.modified = false;

			if (logger.isDebugEnabled()) {
				logger.debug("profileWriteable " + this.writeable);
				logger.debug("dirtyFlag " + this.modified);
				logger.debug("commitProfile call ended");

			}

		} finally {
			try {
				// if the tx was not completed by now, then there was an
				// exception and it should roll back
				if (b && txManager.getTransaction() != null) {
					txManager.rollback();
				}
			} catch (SystemException se) {
				logger.error("Failed completing profile commit due to TX access problem. Profile : " + profileTableName + "/" + profileName, se);
				throw new ManagementException(se.getMessage());
			}
		}
	}

	/**
	 * This mehod must be called within context of profile spec loader. It
	 * copies default profile state via reflrection. FIXME: consider direct JPA
	 * access, would save few CPU cycles.
	 * 
	 * @param defaultProfileObject
	 *            = profile object which is serving default profile.
	 */
	public void copyStateFromDefaultProfile(ProfileObject defaultProfileObject) throws ManagementException {

		// FIXME: or should we use JPA directly here?
		if (profileTransientState == null) {

			try {

				this.profileTransientState = this.profileSpecificationComponent.getProfilePersistanceTransientStateConcreteClass().newInstance();
				// copy state from default profile
				populateFieldsMap();
				ProfileConcrete defaultConcrete = defaultProfileObject.getProfileConcrete();
				for (String name : this.fieldsMap.keySet()) {
					String methodName = ClassUtils.GET_PREFIX + name.replaceFirst(name.charAt(0) + "", Character.toUpperCase(name.charAt(0)) + "");
					Method m = defaultConcrete.getClass().getMethod(methodName, null);
					Object fieldValue = m.invoke(defaultConcrete, null);
					this.fieldsMap.get(name).set(profileTransientState, fieldValue);
				}
				populateFieldsMap();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new ManagementException("State cannot be copied from the default Profile.");
			}
		}
	}

	/**
	 * Determine if profile is in back end storage, for now it returns local
	 * flag, as Im not sure how to determine that for profiles that use
	 * something other than JPA
	 * 
	 * @return
	 */
	private boolean isProfileInBackEndStorage() {
		return this.profileInBackEndStorage;
	}

	/**
	 * Creates fields map - cmp field name ---> java.lang.Field
	 */
	private void populateFieldsMap() {
		// Put all Profile attributes into a hashmap for easier retrieval
		if (fieldsMap == null) {
			fieldsMap = new HashMap<String, Field>();
		}
		fieldsMap = new HashMap();
		Field[] fields = (Field[]) AccessController.doPrivileged(new PrivilegedAction() {
			public Object run() {
				return profileTransientState.getClass().getDeclaredFields();
			}
		});
		for (int i = 0; i < fields.length; i++) {
			fieldsMap.put(fields[i].getName(), fields[i]);
		}
	}

	/**
	 * Handle the call to the method profileInitialize made on the proxy class
	 * generated by the SLEE. This method will look through the profile
	 * transient state class generated by the slee and initialize the fields of
	 * this class.
	 * 
	 * @param proxy
	 *            the proxy class on which has been called the method
	 * @param method
	 *            the method that has been called on the proxy class
	 * @param args
	 *            the args values of the method call
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	private void profileInitialize(Object proxy, Method method, Object[] args) throws IllegalArgumentException, IllegalAccessException {

		Field[] fields;
		if (SleeContainer.isSecurityEnabled())
			fields = (Field[]) AccessController.doPrivileged(new PrivilegedAction() {
				public Object run() {
					return profileTransientState.getClass().getDeclaredFields();
				}
			});
		else
			fields = profileTransientState.getClass().getDeclaredFields();
		if (fields != null) {
			for (int i = 0; i < fields.length; i++) {
				fields[i].set(profileTransientState, getDefautValue(fields[i].getType()));
			}
		}
	}

	/**
	 * Get the default value set for a profile attribute depending of his type
	 * 
	 * @param fieldType
	 *            the profile attribute type
	 * @return the default valu
	 */
	private Object getDefautValue(Class fieldType) {
		// Handle all primitives types
		if (fieldType.equals(int.class))
			return new Integer(0);
		if (fieldType.equals(long.class))
			return new Long(0);
		if (fieldType.equals(double.class))
			return new Double(0);
		if (fieldType.equals(short.class))
			return new Short(new Integer(0).shortValue());
		if (fieldType.equals(float.class))
			return new Float(0);
		if (fieldType.equals(char.class))
			return new Character(' ');
		if (fieldType.equals(boolean.class))
			return new Boolean(false);
		if (fieldType.equals(byte.class))
			return new Byte(new Integer(0).byteValue());

		return null;
	}

}
