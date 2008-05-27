/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
/*
 * DefaultProfileManagementInterceptor.java
 * 
 * Created on Oct 4, 2004
 *
 */
package org.mobicents.slee.container.deployment.interceptors;

import java.beans.Introspector;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;
import javax.slee.Address;
import javax.slee.AddressPlan;
import javax.slee.InvalidStateException;
import javax.slee.SLEEException;
import javax.slee.management.ManagementException;
import javax.slee.profile.ProfileID;
import javax.slee.profile.ProfileImplementationException;
import javax.slee.profile.ProfileManagement;
import javax.slee.profile.ProfileVerificationException;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.SleeContainerUtils;
import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.deployment.ClassUtils;
import org.mobicents.slee.container.deployment.ConcreteClassGeneratorUtils;
import org.mobicents.slee.container.profile.SleeProfileManager;
import org.mobicents.slee.runtime.ActivityContextInterfaceImpl;
import org.mobicents.slee.runtime.DeferredEvent;
import org.mobicents.slee.runtime.facilities.ProfileAddedEventImpl;
import org.mobicents.slee.runtime.facilities.ProfileTableActivityContextInterfaceFactoryImpl;
import org.mobicents.slee.runtime.facilities.ProfileTableActivityImpl;
import org.mobicents.slee.runtime.facilities.ProfileUpdatedEventImpl;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;
import org.mobicents.slee.runtime.transaction.TransactionManagerImpl;

/**
 * @author DERUELLE Jean <a
 *         href="mailto:jean.deruelle@gmail.com">jean.deruelle@gmail.com </a>
 *  
 */
public class DefaultProfileManagementInterceptor implements
        ProfileManagementInterceptor {
    private static String tcache = TransactionManagerImpl.PROFILE_CACHE;

    private boolean dirtyFlag = false;

    private boolean profileWriteable = true;

    private boolean isSbbProfile = false;

    private SleeProfileManager sleeProfileManager;

    private static Logger logger = Logger
            .getLogger(DefaultProfileManagementInterceptor.class);

    private String profileKey;

    private Object profileTransientState = null;

    private Map fieldsMap = null;

    private Object profile;

    private boolean profileInitialized;

    private boolean profileInBackEndStorage;

    private String profileTableName;

    private String profileName;

    /**
     *  
     */
    public DefaultProfileManagementInterceptor() {

    }

    /**
     * @param isSbbProfile
     */
    public DefaultProfileManagementInterceptor(boolean isSbbProfile) {
        this.isSbbProfile = isSbbProfile;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.container.deployment.interceptors.ProfileManagementInterceptor#invoke(java.lang.Object,
     *      java.lang.reflect.Method, java.lang.Object[])
     */
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
    	    	
        SleeTransactionManager txManager = sleeProfileManager
                .getTransactionManager();
        
        boolean createdTransaction = false;
        boolean rollback = true;        
        try {        	
            createdTransaction = txManager.requireTransaction();            
            Object result = processInvoke(proxy, method, args);
            rollback = false;
            return result;             
        } finally {
           if (createdTransaction) {        	   
        	   if(rollback) {
        		   txManager.rollback();
        	   }
        	   else {
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
    private Object processInvoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        if (profileTransientState == null) {
            String name = proxy.getClass().getName();
            String profileCMPInterfaceName = name
                    .substring(
                            ConcreteClassGeneratorUtils.PROFILE_CONCRETE_CLASS_NAME_PREFIX
                                    .length(),
                            name.length()
                                    - ConcreteClassGeneratorUtils.PROFILE_CONCRETE_CLASS_NAME_SUFFIX
                                            .length());
            String profileTransientStateClassName = ConcreteClassGeneratorUtils.PROFILE_TRANSIENT_CLASS_NAME_PREFIX
                    + profileCMPInterfaceName
                    + ConcreteClassGeneratorUtils.PROFILE_TRANSIENT_CLASS_NAME_SUFFIX;
            final ClassLoader cl = SleeContainerUtils
                    .getCurrentThreadClassLoader();
            Class profileTransientStateClass = cl
                    .loadClass(profileTransientStateClassName);
            profileTransientState = profileTransientStateClass.newInstance();
            //Put all the fields into a hashmap for easier retrieval
            populateFieldsMap();
        }

        SleeTransactionManager txManager = sleeProfileManager
                .getTransactionManager();

        /*
         * if(removeException){ removeException=false;
         * profileVerificationException=null; }
         */
        String methodName = method.getName();

        //javax.slee.profile.ProfileMBean interface Methods
        /*
         * 10.15.3.6 isProfileDirty method
         */
        if (method.getName().equals("isProfileDirty")) {
            /*
             * The isProfileDirty method returns true if the Profile MBean
             * object is in the read-write state and the dirty flag of the
             * Profile Management object that caches the persistent state of the
             * Profile returns true. This method returns false under any other
             * situation.
             */
            if (logger.isDebugEnabled())
                logger.debug("isProfileDirty called (profileKey=" + profileKey
                        + ")");
            if (profileWriteable && dirtyFlag)
                return new Boolean(true);
            else
                return new Boolean(false);
        }
        /*
         * 10.15.3.5 isProfileWriteable method
         */
        if (method.getName().equals("isProfileWriteable")) {
            /*
             * The isProfileWriteable method returns true if the Profile MBean
             * object is in the read-write state, or false if in the read-only
             * state.
             */
            if (logger.isDebugEnabled())
                logger.debug("isProfileWriteable called (profileKey="
                        + profileKey + ")");
            return new Boolean(profileWriteable);
        }
        /*
         * 10.15.3.2 commitProfile method
         */
        if (method.getName().equals("commitProfile")) {
            commitProfile();
            // commit profile does not return a value
            return null;
        }
        /*
         * 10.15.3.3 restoreProfile method
         */
        if (method.getName().equals("restoreProfile")) {
            /*
             * The Administrator invokes the restoreProfile method if the
             * Administrator wishes to discard changes made to the Profile
             * Management object that caches the persistent state of a Profile.
             * The implementation of this method rolls back any changes that
             * have been made to the Profile Management object since the Profile
             * MBean object entered the read-write state and moves the Profile
             * MBean object to the read-only state. If the Profile MBean object
             * was returned by the createProfile method (see Section 14.11),
             * then no new Profile is created since the transaction will not
             * commit The execution of this method must begin in the same
             * transaction context as that begun by the createProfile or
             * editProfile invocation that initiated the editing session, but
             * must roll back the transaction before returning.
             */
            if (logger.isDebugEnabled()) {
                logger.debug("restoreProfile called (profileKey=" + profileKey
                        + ")");
                logger.debug("profileWriteable " + profileWriteable);
                logger.debug("dirtyFlag " + dirtyFlag);
            }
            /*
             * The restoreProfile method must throw a
             * javax.slee.InvalidStateException if the Profile MBean object is
             * not in the read-write state.
             */
            if (!profileWriteable)
                throw new InvalidStateException();
            //rollback everything
            //sleeProfileManager.rollbackTransaction(profileKey);
            txManager.setRollbackOnly();
            //and then restore the values that were last comitted into the
            // transient state class
            profileLoad();
            profileWriteable = false;
            if (logger.isDebugEnabled()) {
                logger.debug("profileWriteable " + profileWriteable);
                logger.debug("dirtyFlag " + dirtyFlag);
                logger.debug("restoreProfile call ended");
            }
            return null;
        }
        /*
         * SLEE 1.0 spec, 10.15.3.4 closeProfile method
         */
        if (method.getName().equals("closeProfile")) {
            /*
             * The Administrator invokes the closeProfile method when the
             * Administrator no longer requires access to the Profile MBean
             * object. The implementation of this method is free to deregister
             * the Profile MBean object from the MBean Server. ( but if you do
             * this then test # 4386 will fail! )
             */
            if (logger.isDebugEnabled()) {
                logger.debug("closeProfile called (profileKey=" + profileKey
                        + ")");
                logger.debug("profileWriteable " + profileWriteable);
                logger.debug("dirtyFlag " + dirtyFlag);
            }
            /*
             * The closeProfile method must throw a
             * javax.slee.InvalidStateException if the Profile MBean object is
             * in the read-write state.
             */
            if (profileWriteable)
                throw new InvalidStateException();

            // Jean -- Should close imply unregister ? I think not.
            //sleeProfileManager.unregisterProfileMBean(profileKey);
            if (logger.isDebugEnabled()) {
                logger.debug("profileWriteable " + profileWriteable);
                logger.debug("dirtyFlag " + dirtyFlag);
                logger.debug("closeProfile call ended");
            }
            return null;
        }
        //Check the Profile MBean Life Cycle in the JAIN SLEE spec 1.0, section
        // 10.16.2
        /*
         * 10.15.3.1 editProfile method
         */
        if (method.getName().equals("editProfile")) {
            /*
             * The Administrator invokes the editProfile method to obtain
             * read-write access to the Profile MBean object (if the
             * Administrator currently has read-only access to the Profile MBean
             * object).
             */
            if (logger.isDebugEnabled()) {
                logger.debug("editProfile called (profileKey=" + profileKey
                        + ")");
                logger.debug("profileWriteable " + profileWriteable);
                logger.debug("dirtyFlag " + dirtyFlag);
            }

            if (!profileWriteable) {
                if (logger.isDebugEnabled())
                    logger
                            .debug("starting new Transaction and editing profile");
                /*
                 * The implementation of this method should start a new
                 * transaction for the editing session, or perform the
                 * equivalent function.
                 */
                //sleeProfileManager.startTransaction(profileKey);
                //sleeProfileManager.startTransaction();
                // boolean b = txManager.requireTransaction();
                profileWriteable = true;
                ((ProfileManagement) profile).profileLoad();
                //if ( b ) txManager.commit();
            }
            /*
             * If the Profile MBean object is already in the read-write state
             * when this method is invoked, this method has no further effect
             * and returns silently.
             */
            else {
                logger.debug("profile already in the read/write state");
            }
            if (logger.isDebugEnabled()) {
                logger.debug("profileWriteable " + profileWriteable);
                logger.debug("dirtyFlag " + dirtyFlag);
                logger.debug("editProfile call ended");
            }
            return null;
        }
        //ProfileManagement Methods
        if (method.getName().equals("markProfileDirty")) {
            if (logger.isDebugEnabled()) {
                logger.debug("markProfileDirty called (profileKey="
                        + profileKey + ")");
                logger.debug("profileWriteable " + profileWriteable);
                logger.debug("dirtyFlag " + dirtyFlag);
            }

            dirtyFlag = true;

            if (logger.isDebugEnabled()) {
                logger.debug("profileWriteable " + profileWriteable);
                logger.debug("dirtyFlag " + dirtyFlag);
                logger.debug("markProfileDirty call ended");
            }

            return null;
        }
        if (method.getName().equals("profileInitialize")) {

            if (logger.isDebugEnabled()) {
                logger.debug("profileInitialize called (profileKey="
                        + profileKey + ")");
                logger.debug("profileWriteable " + profileWriteable);
                logger.debug("dirtyFlag " + dirtyFlag);
            }

            profileInitialize(proxy, method, args);
            dirtyFlag = true;
            profileInitialized = true;

            if (logger.isDebugEnabled()) {
                logger.debug("profileWriteable " + profileWriteable);
                logger.debug("dirtyFlag " + dirtyFlag);
                logger.debug("profileInitialize call ended");
            }
            return null;
        }
        if (method.getName().equals("profileStore")) {
            if (logger.isDebugEnabled()) {
                logger.debug("profileStore called (profileKey=" + profileKey
                        + ")");
                logger.debug("profileWriteable " + profileWriteable);
                logger.debug("dirtyFlag " + dirtyFlag);
            }

            try {
                profileStore();
            }
            /*
             * catch(ProfileVerificationException pve){
             * profileVerificationException=pve;
             * sleeProfileManager.rollbackTransaction(); dirtyFlag=false; throw
             * pve; }
             */
            catch (Exception e) {
                txManager.setRollbackOnly();
                dirtyFlag = false;
                throw e;
            }
            dirtyFlag = false;

            if (logger.isDebugEnabled()) {
                logger.debug("profileWriteable " + profileWriteable);
                logger.debug("dirtyFlag " + dirtyFlag);
                logger.debug("profileStore call ended");
            }

            return null;
        }
        if (method.getName().equals("profileLoad")) {

            if (logger.isDebugEnabled()) {
                logger.debug("profileLoad called (profileKey=" + profileKey
                        + ")");
                logger.debug("profileWriteable " + profileWriteable);
                logger.debug("dirtyFlag " + dirtyFlag);
            }

            profileLoad();

            if (logger.isDebugEnabled()) {
                logger.debug("profileWriteable " + profileWriteable);
                logger.debug("dirtyFlag " + dirtyFlag);
                logger.debug("profileLoad call ended");
            }

            return null;
        }
        if (method.getName().equals("profileVerify")) {
            profileVerify();
            return null;
        }
        if (method.getName().equals("isProfileValid")) {

            if (logger.isDebugEnabled()) {
                logger.debug("isProfileValid called (profileKey=" + profileKey
                        + ")");
                logger.debug("profileWriteable " + profileWriteable);
                logger.debug("dirtyFlag " + dirtyFlag);
            }

            if (args[0] == null) {
                throw new NullPointerException();
            }
            ProfileID profileID = (ProfileID) args[0];
            try {
                boolean profileExist = sleeProfileManager
                        .profileExist(profileID);

                if (logger.isDebugEnabled()) {
                    logger.debug("profileWriteable " + profileWriteable);
                    logger.debug("dirtyFlag " + dirtyFlag);
                    logger.debug("isProfileValid call ended");
                }

                return new Boolean(profileExist);
            } catch (Exception e) {
                e.printStackTrace();
                throw new SLEEException(
                        "Profile cannot be found due to a system-level failure");
            }
        }

        if (logger.isDebugEnabled())
            logger.debug("before accessor " + method.getName().substring(3)
                    + " called");

        if (isAccessor(proxy, method, args)) {

            if (logger.isDebugEnabled()) {
                logger.debug("accessor " + method.getName().substring(3)
                        + " called");
                logger.debug("profileWriteable " + profileWriteable);
                logger.debug("dirtyFlag " + dirtyFlag);
            }

            if (method.getName().startsWith(ClassUtils.GET_PREFIX)) {
                if (isSbbProfile) {
                    String attributeName = Introspector.decapitalize(method
                            .getName().substring(3));
                    return sleeProfileManager.getProfileAttributeValue(
                            profileKey, attributeName);
                } else {
                    final String fieldName = Introspector.decapitalize(method
                            .getName().substring(3));
                    Field field;
                    if (SleeContainer.isSecurityEnabled())
                        field = (Field) AccessController
                                .doPrivileged(new PrivilegedAction() {
                                    public Object run() {
                                        try {
                                            return profileTransientState
                                                    .getClass()
                                                    .getDeclaredField(fieldName);
                                        } catch (Exception e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                });
                    else
                        field = profileTransientState.getClass()
                                .getDeclaredField(fieldName);

                    if (logger.isDebugEnabled())
                        logger.debug("accessor "
                                + method.getName().substring(3)
                                + " called, value="
                                + field.get(profileTransientState));

                    return field.get(profileTransientState);
                }
            } else {
                if (!profileWriteable)
                    throw new InvalidStateException();
                //if a sbb tries to set a value, it is not authorized
                if (isSbbProfile)
                    throw new UnsupportedOperationException();
                if(logger.isDebugEnabled()) {
                	logger.debug(methodName + " value " + args[0]);
                }
                final String fieldName = Introspector.decapitalize(method
                        .getName().substring(3));
                Field field;
                if (SleeContainer.isSecurityEnabled())
                    field = (Field) AccessController
                            .doPrivileged(new PrivilegedAction() {
                                public Object run() {
                                    try {
                                        return profileTransientState.getClass()
                                                .getDeclaredField(fieldName);
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            });
                else
                    field = profileTransientState.getClass().getDeclaredField(
                            fieldName);

                if(logger.isDebugEnabled()) {
                	logger.debug("setValue" + args[0]);
                }
                field.set(profileTransientState, args[0]);
                dirtyFlag = true;
                return null;
            }
        }
        //logger.info(profile.getClass().getName());
        //if none of the methods above can only be a super class
        Class[] parameters = new Class[args.length];
        for (int i = 0; i < args.length; i++)
            parameters[i] = args[i].getClass();
        if (method.getName().startsWith("set")) {
            if (!profileWriteable)
                throw new InvalidStateException();
            //if a sbb tries to set a value, it is not authorized
            if (isSbbProfile)
                throw new UnsupportedOperationException();
        }
        try {
            return callSuperMethod(profile, methodName, args);
        } catch (Exception e) {
            throw new ProfileImplementationException(e);
        }
    }

    /**
     * Default implementation for profileVerify(). See SLEE 1.0 Spec #10.9.6 and
     * #10.15.3.2
     * 
     * @throws ProfileVerificationException
     *             if the verification fails
     * @throws SystemException
     */
    protected void profileVerify() throws ProfileVerificationException,
            SystemException {

        if (logger.isDebugEnabled()) {
            logger
                    .debug("profileVerify called (profileKey=" + profileKey
                            + ")");
            logger.debug("profileWriteable " + profileWriteable);
            logger.debug("dirtyFlag " + dirtyFlag);
        }

        // Verify uniqueness constraint
        Map indexes = sleeProfileManager
                .getProfileIndexesSpec(profileTableName);
        Iterator iter = indexes.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Boolean isUnisuqAttribute = (Boolean) entry.getValue();
            if (isUnisuqAttribute.booleanValue()) {
                // this attribute has to be verified for uniqueness
                String attributeName = (String) entry.getKey();
                verifyAttributeForUniqueness(attributeName);
            }
        }

        if (profileInitialized) {
            profileWriteable = false;
            profileInitialized = false;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("profileWriteable " + profileWriteable);
            logger.debug("dirtyFlag " + dirtyFlag);
            logger.debug("profileVerify call ended");
        }
    }

    /**
     * Verify if the given profile attribute is unique as described in SLEE 1.0
     * Spec #3.3.4. An indexed attribute can only appear at most once in the
     * Profile Table. If the Java type of the indexed attribute is an array and
     * the unique attribute is “True”, a particular value stored in an array
     * element can also only appear once in the Profile Table, i.e. no two array
     * elements of the same indexed attribute stored in the same Profile or
     * different Profiles within the same Profile Table can have the same value.
     * If not specified, the default value is “False”. The null value is exempt
     * from these uniqueness constraints and may appear multiple times as a
     * value of this attribute in multiple Profiles within the Profile Table.
     * 
     * @throws ProfileVerificationException
     */
    private void verifyAttributeForUniqueness(String attributeName)
            throws ProfileVerificationException {
        Field field = (Field) fieldsMap.get(attributeName);

        try {
            Object value = field.get(profileTransientState);
            if (value == null)
                return;
            // verify uniqueness of values within an array attribute
            if (field.getType().isArray()) {
                Set valueSet = new HashSet();
                Object[] values = (Object[]) value;
                for (int i = 0; i < values.length; i++) {
                    if (!valueSet.add(values[i]))
                        throw new ProfileVerificationException(
                                "Non-unique value ("
                                        + values[i]
                                        + ") for unique indexed profile attribute ("
                                        + attributeName + ")");
                }
            }

            // verify uniqueness of attribute value among all profiles
            /*
             * (Ivelin) FIXME: the following check does not behave well.
             * tests/profiles/profileverification shows the issue Collection
             * profiles =
             * sleeProfileManager.getProfilesByIndexedAttribute(profileTableName,
             * attributeName, value, true); if (!profiles.isEmpty()) throw new
             * ProfileVerificationException("Non-unique value (" + value + ")
             * for unique indexed profile attribute (" + attributeName + "). The
             * given profile value matches an existing profile attribute
             * value");
             */

        } catch (Exception e) {
            if (e instanceof ProfileVerificationException)
                throw (ProfileVerificationException) e;
            String err = "Failed verifyAttributeForUniqueness(" + attributeName
                    + ")";
            logger.warn(err, e);
            throw new ProfileVerificationException(err, e);
        }

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
    private void commitProfile() throws InvalidStateException,
            ProfileVerificationException, ManagementException {
        SleeTransactionManager txManager = sleeProfileManager
                .getTransactionManager();
        boolean b = false;

        b = txManager.requireTransaction();

        try {
            if (!profileWriteable)
                throw new InvalidStateException();

            if (logger.isDebugEnabled()) {
                logger.debug("commitProfile called (profileKey=" + profileKey
                        + ")");
                logger.debug("profileWriteable " + profileWriteable);
                logger.debug("dirtyFlag " + dirtyFlag);
            }

            //not storing default profile
            if (profileName != null) {
                ProfileID profileID = new ProfileID(profileTableName,
                        profileName);
                try {
                    txManager.putObject(tcache, getProfileKey(), "profileID",
                            profileID);
                } catch (Exception e4) {
                    logger.error(
                            "Failed profile commit, createNode() exception. Profile key: "
                                    + getProfileKey(), e4);
                    throw new ManagementException(e4.getMessage());
                }
            }
            //getting last commited profile in case of update
            ProfileManagement profileBeforeUpdate = null;
            if (profileInBackEndStorage) {
                try {
                    profileBeforeUpdate = sleeProfileManager
                            .instantiateLastCommittedProfile(profileTableName,
                                    profileName);
                } catch (Exception e1) {
                    throw new ManagementException(
                            "Failed instantiateLastCommittedProfile ", e1);
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
                ((ProfileManagement) profile).profileStore();
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
            try {
                ((ProfileManagement) profile).profileVerify();
            } catch (Exception e) {
                throw new ProfileVerificationException(e.getMessage());
            }
            /*
             * if(profileVerificationException!=null){ removeException=true;
             * throw profileVerificationException; }
             */

            try {
                // persist transient state
                persistProfile();
            } catch (Exception e3) {
                logger.error("Failed commitProfile, profileStore()", e3);
                if (e3 instanceof ProfileVerificationException)
                    throw (ProfileVerificationException) e3;
                else
                    throw new ManagementException(
                            "Failed commitProfile, profileStore()", e3);
            }

            //Fire a Profile Added or Updated Event
            SleeContainer serviceContainer = SleeContainer.lookupFromJndi();

            Address profileAddress = new Address(AddressPlan.SLEE_PROFILE,
                    profileTableName + "/" + profileName);
            ProfileTableActivityContextInterfaceFactoryImpl profileTableActivityContextInterfaceFactory;

            try {
                profileTableActivityContextInterfaceFactory = (ProfileTableActivityContextInterfaceFactoryImpl) SleeContainer
                        .getProfileTableActivityContextInterfaceFactory();
            } catch (NamingException ex) {
                logger.error("Error retrieving facility", ex);
                throw new ManagementException("Unexpected Naming Exception", ex);
            }
            ProfileTableActivityImpl profileTableActivity = new ProfileTableActivityImpl(
                    profileTableName);
            ActivityContextInterfaceImpl activityContextInterface;
            try {
                activityContextInterface = (ActivityContextInterfaceImpl) profileTableActivityContextInterfaceFactory
                        .getActivityContextInterface(profileTableActivity);
            } catch (Exception e1) {
                throw new ManagementException("Failed committing profile", e1);
            }
            if (!profileInBackEndStorage) {
                //Fire the added event only when the transaction commits
                ProfileAddedEventImpl profileAddedEvent = new ProfileAddedEventImpl(
                        profileAddress, new ProfileID(profileAddress), profile,
                        activityContextInterface,
                        profileTableActivityContextInterfaceFactory);
                int eventID = serviceContainer.getEventLookupFacility()
                        .getEventID(
                                new ComponentKey(
                                        "javax.slee.profile.ProfileAddedEvent",
                                        "javax.slee", "1.0"));
                try {
                       new DeferredEvent(
                        eventID,
                        profileAddedEvent,
                        activityContextInterface.getActivityContext(),
                        profileAddress);
                } catch (SystemException e2) {
                    throw new ManagementException("Failed committing profile",
                            e2);
                }
               
                if (logger.isDebugEnabled()) {
                    logger.debug("Queued following profile added event:"
                            + profileAddedEvent.getEventTypeID()
                            + ",:"
                            + activityContextInterface
                                    .retrieveActivityContextID() + ",eventId="
                            + eventID);
                }

                profileInBackEndStorage = true;
            } else {
                //Fire the updated event only when the transaction commits
                ProfileUpdatedEventImpl profileUpdatedEvent = new ProfileUpdatedEventImpl(
                        profileAddress, new ProfileID(profileAddress),
                        profileBeforeUpdate, profile, activityContextInterface,
                        profileTableActivityContextInterfaceFactory);
                int eventID = serviceContainer
                        .getEventLookupFacility()
                        .getEventID(
                                new ComponentKey(
                                        "javax.slee.profile.ProfileUpdatedEvent",
                                        "javax.slee", "1.0"));
                
                try {
                      new DeferredEvent(
                        eventID,
                        profileUpdatedEvent,
                        activityContextInterface.getActivityContext(),
                        profileAddress);
                } catch (SystemException e2) {
                    throw new ManagementException("Failed committing profile",
                            e2);
                }
                
                if(logger.isDebugEnabled()) {
                	logger.debug("Queued following updated event: "
                
                        + profileUpdatedEvent.getEventTypeID() + ",:"
                        + activityContextInterface.retrieveActivityContextID()
                        + ",eventId=" + eventID);
                }
                profileInBackEndStorage = true;
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
                    logger.error("Failed rolling back profile: "
                            + getProfileKey(), e2);
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
            profileWriteable = false;
            dirtyFlag = false;

            if (logger.isDebugEnabled()) {
                logger.debug("profileWriteable " + profileWriteable);
                logger.debug("dirtyFlag " + dirtyFlag);
                logger.debug("commitProfile call ended");

            }

        } finally {
            try {
                // if the tx was not completed by now, then there was an
                // exception and it should roll back
                if (b && txManager.isInTx()) {
                    txManager.rollback();
                }
            } catch (SystemException se) {
                logger.error(
                        "Failed completing profile commit due to TX access problem. Profile key: "
                                + getProfileKey(), se);
                throw new ManagementException(se.getMessage());
            }
        }
    }

    /**
     * 
     * @param object
     * @param methodName
     * @param args
     * @return @throws
     *         SecurityException
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ProfileImplementationException
     */
    protected Object callSuperMethod(Object object, String methodName,
            Object[] args) throws SecurityException, NoSuchMethodException,
            IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, ProfileImplementationException {

        Class[] parameters = new Class[args.length];
        for (int i = 0; i < args.length; i++)
            if (!args[i].getClass().isPrimitive())
                parameters[i] = getPrimitiveTypeFromClass(args[i].getClass());
            else
                parameters[i] = getPrimitiveTypeFromClass(args[i].getClass());
        Method superMethod = object.getClass().getSuperclass().getMethod(
                methodName, parameters);
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
        String accessorName = method.getName().substring(3, 4).toLowerCase()
                + method.getName().substring(4);

        //logger.debug("isAccessor "+accessorName);
        Field[] fields;
        if (SleeContainer.isSecurityEnabled())
            fields = (Field[]) AccessController
                    .doPrivileged(new PrivilegedAction() {
                        public Object run() {
                            return profileTransientState.getClass()
                                    .getDeclaredFields();
                        }
                    });
        else
            fields = profileTransientState.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getName().equals(accessorName))
                return true;
        }
        return false;
    }

    /**
     * Handle the call to the method profileLoad made on the proxy class
     * generated by the SLEE. This method will look through the profile
     * transient state class generated by the slee and load, from the the jboss
     * cache that will look into the DB, the persistent state into the transient
     * fields of the transient class
     * 
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws SystemException
     */
    private void profileLoad() throws IllegalArgumentException,
            IllegalAccessException, SystemException {

        Field[] fields;
        if (SleeContainer.isSecurityEnabled())
            fields = (Field[]) AccessController
                    .doPrivileged(new PrivilegedAction() {
                        public Object run() {
                            return profileTransientState.getClass()
                                    .getDeclaredFields();
                        }
                    });
        else
            fields = profileTransientState.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {

            Object fieldValue = sleeProfileManager.getProfileAttributeValue(
                    this.profileKey, fields[i].getName());
            String debugString = "set " + fields[i].getName() + " to value "
                    + fieldValue;
            if (fieldValue != null)
                debugString += "type:" + fieldValue.getClass().getName();
            if(logger.isDebugEnabled()) {
            	logger.debug(debugString);
            }
            if (fieldValue == null && fields[i].getType().isPrimitive())
                fields[i].set(profileTransientState, getDefautValue(fields[i]
                        .getType()));
            else
                fields[i].set(profileTransientState, fieldValue);
        }
    }

    /**
     * Handle the call to the method profileStore made on the proxy class
     * generated by the SLEE.
     * 
     * By default it does nothing. A custom implementation should update all
     * profile CMP fields from any transient data
     */
    private void profileStore() {
        // NOP
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
    private void profileInitialize(Object proxy, Method method, Object[] args)
            throws IllegalArgumentException, IllegalAccessException {

        Field[] fields;
        if (SleeContainer.isSecurityEnabled())
            fields = (Field[]) AccessController
                    .doPrivileged(new PrivilegedAction() {
                        public Object run() {
                            return profileTransientState.getClass()
                                    .getDeclaredFields();
                        }
                    });
        else
            fields = profileTransientState.getClass().getDeclaredFields();
        if (fields != null) {
            for (int i = 0; i < fields.length; i++) {
                fields[i].set(profileTransientState, getDefautValue(fields[i]
                        .getType()));
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

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.container.deployment.interceptors.ProfileManagementInterceptor#setProfileManager(gov.nist.slee.container.profile.SleeProfileManager)
     */
    public void setProfileManager(SleeProfileManager sleeProfileManager) {
        this.sleeProfileManager = sleeProfileManager;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.container.deployment.interceptors.ProfileManagementInterceptor#getProfileManager()
     */
    public SleeProfileManager getProfileManager() {
        return sleeProfileManager;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.container.deployment.interceptors.ProfileManagementInterceptor#setProfileKey(java.lang.String)
     */
    public void setProfileKey(String profileKey) {
        this.profileKey = profileKey;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.container.deployment.interceptors.ProfileManagementInterceptor#getProfileKey()
     */
    public String getProfileKey() {
        return this.profileKey;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.container.deployment.interceptors.ProfileManagementInterceptor#copyStateFromDefaultProfile(java.lang.String)
     */
    public void copyStateFromDefaultProfile(String profileCMPInterfaceName,
            String defaultProfileKey) throws ManagementException {
        if (profileTransientState == null) {
            String profileTransientStateClassName = profileCMPInterfaceName
                    + "TransientState";
            try {
                ClassLoader cl = SleeContainerUtils
                        .getCurrentThreadClassLoader();
                Class profileTransientStateClass = cl
                        .loadClass(profileTransientStateClassName);
                profileTransientState = profileTransientStateClass
                        .newInstance();
                //copy state from default profile
                Field[] fields;
                if (SleeContainer.isSecurityEnabled())
                    fields = (Field[]) AccessController
                            .doPrivileged(new PrivilegedAction() {
                                public Object run() {
                                    return profileTransientState.getClass()
                                            .getDeclaredFields();
                                }
                            });
                else
                    fields = profileTransientState.getClass()
                            .getDeclaredFields();
                for (int i = 0; i < fields.length; i++) {
                    Object fieldValue = sleeProfileManager
                            .getProfileAttributeValue(defaultProfileKey,
                                    fields[i].getName());
                    fields[i].set(profileTransientState, fieldValue);
                }
                populateFieldsMap();
            } catch (Exception e) {
                logger.error(e);
                throw new ManagementException(
                        "State cannot be copied from the default Profile.");
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.container.deployment.interceptors.ProfileManagementInterceptor#setProfile(java.lang.Object)
     */
    public void setProfile(Object profile) {
        this.profile = profile;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.container.deployment.interceptors.ProfileManagementInterceptor#getProfile()
     */
    public Object getProfile() {
        return this.profile;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.container.deployment.interceptors.ProfileManagementInterceptor#loadStateFromBackendStorage()
     */
    public void loadStateFromBackendStorage(String profileCMPInterfaceName)
            throws Exception {
    	if(logger.isDebugEnabled()) {
    		logger.debug("loading profile data from backend storage");
    	}
        if (profileTransientState == null) {
            String profileTransientStateClassName = ConcreteClassGeneratorUtils.PROFILE_TRANSIENT_CLASS_NAME_PREFIX
                    + profileCMPInterfaceName
                    + ConcreteClassGeneratorUtils.PROFILE_TRANSIENT_CLASS_NAME_SUFFIX;
            ClassLoader cl = SleeContainerUtils.getCurrentThreadClassLoader();
            Class profileTransientStateClass = cl
                    .loadClass(profileTransientStateClassName);
            profileTransientState = profileTransientStateClass.newInstance();

            populateFieldsMap();
        }
        profileLoad();
        profileWriteable = false;
        profileInBackEndStorage = true;
    }

    /**
     *  
     */
    private void populateFieldsMap() {
        //Put all Profile attributes into a hashmap for easier retrieval
        fieldsMap = new HashMap();
        Field[] fields = (Field[]) AccessController
                .doPrivileged(new PrivilegedAction() {
                    public Object run() {
                        return profileTransientState.getClass()
                                .getDeclaredFields();
                    }
                });
        for (int i = 0; i < fields.length; i++) {
            fieldsMap.put(fields[i].getName(), fields[i]);
        }
    }

    public void setProfileTableName(String newProfileTableName) {
        profileTableName = newProfileTableName;
    }

    public String getProfileTableName() {
        return profileTableName;
    }

    public void setProfileName(String newProfileName) {
        profileName = newProfileName;
    }

    public String getProfileName() {
        return profileName;
    }

    /**
     * Persist CMP attributes of a profile. This method will look through the
     * CMP fields in the transient state class generated by the slee and store
     * the fields of this class into the persistent storage.
     * 
     * @param profileTransientState
     * @throws Exception
     */
    public void persistProfile() throws Exception {
        Field[] fields;
        if (SleeContainer.isSecurityEnabled())
            fields = (Field[]) AccessController
                    .doPrivileged(new PrivilegedAction() {
                        public Object run() {
                            return profileTransientState.getClass()
                                    .getDeclaredFields();
                        }
                    });
        else
            fields = profileTransientState.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Object fieldValue = fields[i].get(profileTransientState);
            sleeProfileManager.setProfileAttributeValue(profileKey, fields[i]
                    .getName(), fieldValue);
        }
    }

}