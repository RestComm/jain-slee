/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2014, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 * This file incorporates work covered by the following copyright contributed under the GNU LGPL : Copyright 2007-2011 Red Hat.
 */

package org.mobicents.slee.container.management;

import org.apache.log4j.Logger;
import org.jboss.as.naming.context.NamespaceContextSelector;
import org.mobicents.slee.container.AbstractSleeContainerModule;
import org.mobicents.slee.container.component.ComponentRepository;
import org.mobicents.slee.container.component.classloading.ReplicationClassLoader;
import org.mobicents.slee.container.component.common.EnvEntryDescriptor;
import org.mobicents.slee.container.component.ratype.ResourceAdaptorTypeComponent;
import org.mobicents.slee.container.component.sbb.EjbRefDescriptor;
import org.mobicents.slee.container.component.sbb.ResourceAdaptorEntityBindingDescriptor;
import org.mobicents.slee.container.component.sbb.ResourceAdaptorTypeBindingDescriptor;
import org.mobicents.slee.container.component.sbb.SbbComponent;
import org.mobicents.slee.container.component.service.ServiceComponent;
import org.mobicents.slee.container.deployment.SbbClassCodeGenerator;
import org.mobicents.slee.container.jndi.JndiManagement;
import org.mobicents.slee.container.resource.ResourceAdaptorEntity;
import org.mobicents.slee.container.transaction.SleeTransactionManager;
import org.mobicents.slee.container.transaction.TransactionalAction;
import org.mobicents.slee.runtime.facilities.SbbAlarmFacilityImpl;
import org.mobicents.slee.runtime.sbb.SbbObjectPoolImpl;
import org.mobicents.slee.runtime.sbb.SbbObjectPoolManagementImpl;

import javax.naming.*;
import javax.rmi.PortableRemoteObject;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.facilities.AlarmFacility;
import javax.slee.facilities.Level;
import javax.slee.management.DeploymentException;
import javax.transaction.SystemException;
import java.lang.reflect.Method;

//import javax.naming.InitialContext;

/**
 * Manages sbbs in container
 * 
 * @author martins
 * 
 */
@SuppressWarnings("deprecation")
public class SbbManagementImpl extends AbstractSleeContainerModule implements SbbManagement {

	private static final Logger logger = Logger.getLogger(SbbManagementImpl.class);
	
	private SbbObjectPoolManagementImpl sbbPoolManagement;
	
	@Override
	public void sleeInitialization() {
		sbbPoolManagement = new SbbObjectPoolManagementImpl(sleeContainer);
		sbbPoolManagement.register();
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.management.SbbManagement#serviceInstall(org.mobicents.slee.core.component.service.ServiceComponent)
	 */
	public void serviceInstall(ServiceComponent serviceComponent) {
		// create object pools
		for (SbbID sbbID : serviceComponent.getSbbIDs(sleeContainer.getComponentRepository())) {
			// create the pool for the given SbbID
			sbbPoolManagement.createObjectPool(serviceComponent.getServiceID(), sleeContainer.getComponentRepository().getComponentByID(sbbID),
					sleeContainer.getTransactionManager());
		}
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.management.SbbManagement#serviceUninstall(org.mobicents.slee.core.component.service.ServiceComponent)
	 */
	public void serviceUninstall(ServiceComponent serviceComponent) {
		// remove sbb object pools
		for (SbbID sbbID : serviceComponent.getSbbIDs(sleeContainer.getComponentRepository())) {
			// remove the pool for the given SbbID
			sbbPoolManagement.removeObjectPool(serviceComponent.getServiceID(), sleeContainer.getComponentRepository().getComponentByID(sbbID),
					sleeContainer.getTransactionManager());
		}
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.management.SbbManagement#getObjectPool(javax.slee.ServiceID, javax.slee.SbbID)
	 */
	public SbbObjectPoolImpl getObjectPool(ServiceID serviceID, SbbID sbbID) {
		return sbbPoolManagement.getObjectPool(serviceID, sbbID);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.management.SbbManagement#installSbb(org.mobicents.slee.core.component.sbb.SbbComponent)
	 */
	public void installSbb(final SbbComponent sbbComponent)
			throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("Installing " + sbbComponent);
		}

		final SleeTransactionManager sleeTransactionManager = sleeContainer
				.getTransactionManager();
		sleeTransactionManager.mandateTransaction();

		// change classloader
		ClassLoader oldClassLoader = Thread.currentThread()
				.getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(
					sbbComponent.getClassLoader());
			// Set up the comp/env naming context for the Sbb.
			JndiManagement jndiManagement = sleeContainer.getJndiManagement();
			jndiManagement.componentInstall(sbbComponent);
			jndiManagement.pushJndiContext(sbbComponent);
			try {
				setupSbbEnvironment(sbbComponent);
			} finally {
				jndiManagement.popJndiContext();
			}
			// generate class code for the sbb
			new SbbClassCodeGenerator().process(sbbComponent);
			
		//FIXME: this will erase stack trace.	
		//} catch (Exception ex) {
		//	throw ex;
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
				
		// Set 1.0 Trace to off
		sleeContainer.getTraceManagement().getTraceFacility().setTraceLevelOnTransaction(
				sbbComponent.getSbbID(), Level.OFF);
		sleeContainer.getAlarmManagement().registerComponent(
				sbbComponent.getSbbID());
		// if we are in cluster mode we need to add the sbb class loader domain to the replication class loader
		if (!sleeContainer.getCluster().getMobicentsCache().isLocalMode()) {
			final ReplicationClassLoader replicationClassLoader = sleeContainer.getReplicationClassLoader();
			replicationClassLoader.addDomain(sbbComponent.getClassLoaderDomain());
			TransactionalAction action = new TransactionalAction() {
				public void execute() {
					replicationClassLoader.removeDomain(sbbComponent.getClassLoaderDomain());
				}
			};
			sleeTransactionManager.getTransactionContext().getAfterRollbackActions().add(action);
		}
		
	}

	private void setupSbbEnvironment(SbbComponent sbbComponent) throws Exception {		
		
		//Context ctx = (Context) new InitialContext().lookup("java:comp");
		Context ctx = NamespaceContextSelector.getCurrentSelector().getContext("comp");

		if (logger.isTraceEnabled()) {
			logger.trace("Setting up SBB env. Initial context is " + ctx);
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

		// Do all the context binding stuff just once during init and
		// just do the linking here.

		Context newCtx;

		/*
		String containerName = "java:slee/container/Container";
		try {
			newCtx = sleeCtx.createSubcontext("container");
		} catch (NameAlreadyBoundException ex) {

		} finally {
			newCtx = (Context) sleeCtx.lookup("container");
		}

		try {
			newCtx.bind("Container", new LinkRef(containerName));
		} catch (NameAlreadyBoundException ex) {

		}*/

		try {
			newCtx = sleeCtx.createSubcontext("nullactivity");
		} catch (NameAlreadyBoundException ex) {
			newCtx = (Context) sleeCtx.lookup("nullactivity");
		}
		try {
			newCtx.bind("activitycontextinterfacefactory", sleeContainer.getNullActivityContextInterfaceFactory());
		} catch (NameAlreadyBoundException ex) {
		}
		try {
			newCtx.bind("factory", sleeContainer.getNullActivityFactory());
		} catch (NameAlreadyBoundException ex) {
		}

		//String serviceActivityContextInterfaceFactory = "java:slee/serviceactivity/activitycontextinterfacefactory";
		//String serviceActivityFactory = "java:slee/serviceactivity/factory";
		try {
			newCtx = sleeCtx.createSubcontext("serviceactivity");
		} catch (NameAlreadyBoundException ex) {
			newCtx = (Context) sleeCtx.lookup("serviceactivity");
		}		
		try {
			newCtx.bind("activitycontextinterfacefactory",
					sleeContainer.getServiceManagement().getServiceActivityContextInterfaceFactory());
		} catch (NameAlreadyBoundException ex) {
		}
		try {
			newCtx.bind("factory", sleeContainer.getServiceManagement().getServiceActivityFactory());
		} catch (NameAlreadyBoundException ex) {
		}
		
		try {
			newCtx = sleeCtx.createSubcontext("facilities");
		} catch (NameAlreadyBoundException ex) {
			newCtx = (Context) sleeCtx.lookup("facilities");
		}
		try {
			newCtx.bind("timer", sleeContainer.getTimerFacility());
		} catch (NameAlreadyBoundException ex) {
		}

		try {
			newCtx.bind("activitycontextnaming",
					sleeContainer.getActivityContextNamingFacility());
		} catch (NameAlreadyBoundException ex) {
		}

		try {
			newCtx.bind("trace",
					sleeContainer.getTraceManagement().getTraceFacility());
		} catch (NameAlreadyBoundException ex) {
		}

		try {
			newCtx.bind("profile",
					sleeContainer.getSleeProfileTableManager().getProfileFacility());
		} catch (NameAlreadyBoundException ex) {
		}

		try {
			newCtx.bind("profiletableactivitycontextinterfacefactory",
					sleeContainer.getSleeProfileTableManager().getProfileTableActivityContextInterfaceFactory());
		} catch (NameAlreadyBoundException ex) {
		}

		try {
			//This has to be checked, to be sure sbb have it under correct jndi binding
			// previously "alarm" was pointing to the alarmmbeanimpl
			AlarmFacility sbbAlarmFacility = new SbbAlarmFacilityImpl(sbbComponent.getSbbID(),sleeContainer.getAlarmManagement());
			newCtx.bind("alarm", sbbAlarmFacility);
			sbbComponent.setAlarmFacility(sbbAlarmFacility);
		} catch (NameAlreadyBoundException ex) {
		}

		// profiles currently not supported
		/*
		try {
			newCtx.bind("profile", sleeContainer.getSleeProfileTableManager().getProfileFacility());
		} catch (NameAlreadyBoundException ex) {
		}
		
		try {
			newCtx.bind("profiletableactivitycontextinterfacefactory",
					sleeContainer.getSleeProfileTableManager().getProfileTableActivityContextInterfaceFactory());
		} catch (NameAlreadyBoundException ex) {

		}*/

		// For each resource that the Sbb references, bind the implementing
		// object name to its comp/env

		if (logger.isTraceEnabled()) {
			logger.trace("Number of Resource Bindings:"
					+ sbbComponent.getDescriptor().getResourceAdaptorTypeBindings());
		}
		ComponentRepository componentRepository = sleeContainer.getComponentRepository();
		for (ResourceAdaptorTypeBindingDescriptor raTypeBinding : sbbComponent.getDescriptor().getResourceAdaptorTypeBindings()) {
			
			ResourceAdaptorTypeComponent raTypeComponent = componentRepository.getComponentByID(raTypeBinding.getResourceAdaptorTypeRef());
			
			for (ResourceAdaptorEntityBindingDescriptor raEntityBinding : raTypeBinding.getResourceAdaptorEntityBinding()) {

				String raObjectName = raEntityBinding.getResourceAdaptorObjectName();
				String linkName = raEntityBinding.getResourceAdaptorEntityLink();
				/*
				 * The Deployment descriptor specifies Zero or more
				 * resource-adaptor-entity-binding elements. Each
				 * resource-adaptor-entity-binding element binds an object that
				 * implements the resource adaptor interface of the resource
				 * adaptor type into the JNDI comp onent environment of the SBB
				 * (see Section 6.13.3). Each resource- adaptorentity- binding
				 * element contains the following sub-elements: A description
				 * element. This is an optional informational element. A
				 * resource-adaptor-object?name element. This element specifies
				 * the location within the JNDI component environment to which
				 * the object that implements the resource adaptor interface
				 * will be bound. A resource-adaptor-entity-link element. This
				 * is an optional element. It identifies the resource adaptor
				 * entity that provides the object that should be bound into the
				 * JNDI component environment of the SBB. The identified
				 * resource adaptor entity must be an instance of a resource
				 * adaptor whose resource adaptor type is specified by the
				 * resourceadaptor- type-ref sub-element of the enclosing
				 * resource-adaptortype- binding element.
				 */
				ResourceManagement resourceManagement = sleeContainer.getResourceManagement();
				ResourceAdaptorEntity raEntity = resourceManagement
						.getResourceAdaptorEntity(resourceManagement
								.getResourceAdaptorEntityName(linkName));
				
				if (raEntity == null)
					throw new Exception(
							"Could not find Resource adaptor Entity for Link Name: ["
									+ linkName + "] of RA Type ["
									+ raTypeComponent + "]");

				NameParser parser = ctx.getNameParser("");
				Name name = parser.parse("java:comp/env/"+raObjectName);
				int tokenCount = name.size();

				// ctx is Context for "java:comp"
				Context subContext = ctx;

				// skip 0 = "java:comp"
				for (int i = 0; i < tokenCount - 1; i++) {
					String nextTok = name.get(i);
					try {
						subContext = (Context) subContext.createSubcontext(nextTok);
					} catch (NameAlreadyBoundException nfe) {
						subContext = (Context) subContext.lookup(nextTok);
					}
				}
				// Bind the resource adaptor instance to where the Sbb expects
				// to find it.
				if (logger.isDebugEnabled()) {
					logger
							.debug("Binding RA entity named "+raEntity.getName()+" sbb interface for "+raTypeBinding.getResourceAdaptorTypeRef()+", to JNDI context "+name);
				}
				try {
					Object raSbbInterface = raEntity.getResourceAdaptorInterface(raTypeBinding.getResourceAdaptorTypeRef());
					if (raSbbInterface != null) {
						subContext.bind(name.get(tokenCount-1),ResourceAdaptorSbbInterfaceObjectFactory.getReference(raSbbInterface, raTypeBinding.getResourceAdaptorTypeRef(), linkName));
					}
					else {
						throw new DeploymentException("Unable to retrieve the RA interface for RA entity "+raEntity.getName()+" and RAType " +raTypeBinding.getResourceAdaptorTypeRef());
					}					
				} catch (NameAlreadyBoundException e) {
					logger.warn(
							"Unable to bind a JNDI reference to sbb interface of "+raTypeBinding.getResourceAdaptorTypeRef(), e);
				}
			}

			String localFactoryName = raTypeBinding.getActivityContextInterfaceFactoryName();
			if (localFactoryName != null) {
				NameParser parser = ctx.getNameParser("");
				Name local = parser.parse(localFactoryName);
				int nameSize = local.size();
				Context tempCtx = envCtx;

				for (int a = 0; a < nameSize - 1; a++) {
					String temp = local.get(a);
					try {
						tempCtx.lookup(temp);
					} catch (NameNotFoundException ne) {
						tempCtx.createSubcontext(temp);

					} finally {
						tempCtx = (Context) tempCtx.lookup(temp);
					}
				}
				if (logger.isDebugEnabled()) {
					logger
							.debug(
						"Binding a JNDI reference to aci factory interface of "+raTypeBinding.getResourceAdaptorTypeRef());
				}
				String factoryRefName = local.get(nameSize - 1);
				try {
					tempCtx
							.bind(factoryRefName,raTypeComponent.getActivityContextInterfaceFactory());
				} catch (NameAlreadyBoundException e) {
					logger.warn(
							"Unable to bind a JNDI reference to aci factory interface of "+raTypeBinding.getResourceAdaptorTypeRef(), e);
				}
			}

		}
		

		/*
		 * Bind the ejb-refs
		 */
		try {
			envCtx.createSubcontext("ejb");
		} catch (NameAlreadyBoundException ex) {
			//envCtx = (Context)envCtx.lookup("ejb");
			//6.13.4.1.1 - The SLEE specification recommends, but does not require, that all references to other EJBs be organized
			//in the ejb subcontext of the SBB's environment
			//so it can be any :/
			envCtx.lookup("ejb");
		}

		for (EjbRefDescriptor ejbRef : sbbComponent.getDescriptor().getEjbRefs()) {
		
			String jndiName = ejbRef.getEjbRefName();
						
			
			/*
			 * Validate the ejb reference has the correct type and classes as
			 * specified in deployment descriptor
			 */
			  
			String home = ejbRef.getHome();
			String remote = ejbRef.getRemote();
			//EJBs are bound to global name space.
			//Object obj = new InitialContext().lookup("java:comp/env/" + ejbRef.getEjbRefName());
			Object obj = new InitialContext().lookup( jndiName);

			Object homeObject = null;
			try {

				Class homeClass = Thread.currentThread().getContextClassLoader().loadClass(home);

				homeObject = PortableRemoteObject.narrow(obj, homeClass);
		
				if (!homeClass.isInstance(homeObject)) {
					throw new DeploymentException("Looked up ejb home is not an instanceof " + home);
				}
			} catch (ClassNotFoundException e) {
				throw new DeploymentException("Failed to load class " + home,e);
			} catch (ClassCastException e) {
				throw new DeploymentException("Failed to lookup ejb reference using jndi name " + jndiName,e);
			}

			try {
				Method m = homeObject.getClass().getMethod("create", null);
				Object ejbObject = m.invoke(homeObject, null);

				Class ejbClass = Thread.currentThread().getContextClassLoader().loadClass(remote);
				if (!ejbClass.isInstance(ejbObject)) {
					throw new DeploymentException("Looked up ejb object is not an instanceof " + remote);
				}
			} catch (ClassNotFoundException e) {
				throw new DeploymentException("Failed to load class " + remote,e);
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Binding ejb: " + ejbRef.getEjbRefName()
					+ " with link to " + jndiName);
			}
	
			try {
				String name = ejbRef.getEjbRefName();
				NameParser parser = ctx.getNameParser("");
				Name local = parser.parse(name);
				int nameSize = local.size();
				Context tempCtx = createSubContext(envCtx,local);
				tempCtx.bind(local.get(nameSize-1), new LinkRef(jndiName));
			
			} catch (NameAlreadyBoundException ex) {
				//should not happen, JIC.
			}

			/*
			 * A note on the <ejb-link> link. The semantics of ejb-link when
			 * used to reference a remote ejb are not defined in the SLEE spec.
			 * In J2EE it is defined to mean a reference to an ejb deployed in
			 * the same J2EE application whose <ejb-name> is the same as the
			 * link (optionally the ejb-jar) file is also specifed. In SLEE
			 * there is no J2EE application and ejbs cannot be deployed in the
			 * SLEE container, therefore we do nothing with <ejb-link> since I
			 * am not sure what should be done with it anyway! - Tim
			 */

		}

		/* Set the environment entries */
		for (EnvEntryDescriptor mEnvEntry : sbbComponent.getDescriptor().getEnvEntries()) {
			Class<?> type = null;

			if (logger.isTraceEnabled()) {
				logger.trace("Got an environment entry:" + mEnvEntry);
			}

			try {
				type = Thread.currentThread().getContextClassLoader()
						.loadClass(mEnvEntry.getEnvEntryType());
			} catch (Exception e) {
				throw new DeploymentException(mEnvEntry.getEnvEntryType()
						+ " is not a valid type for an environment entry");
			}
			Object entry = null;
			String s = mEnvEntry.getEnvEntryValue();

			try {
				if (type == String.class) {
					entry = new String(s);
				} else if (type == Character.class) {
					if (s.length() != 1) {
						throw new DeploymentException(
								s
										+ " is not a valid value for an environment entry of type Character");
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
				throw new DeploymentException("Environment entry value " + s
						+ " is not a valid value for type " + type);
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Binding environment entry with name:"
						+ mEnvEntry.getEnvEntryName() + " type  " + entry.getClass()
						+ " with value:" + entry + ". Current classloader = "
						+ Thread.currentThread().getContextClassLoader());
			}
			try {
				envCtx.bind(mEnvEntry.getEnvEntryName(), entry);
			} catch (NameAlreadyBoundException ex) {
				logger.error("Name already bound ! ", ex);
			}
		}
	}
	private static Context createSubContext(Context ctx,Name local) throws Exception
	{
	
		int nameSize = local.size();
		Context tempCtx = ctx;

		for (int a = 0; a < nameSize - 1; a++) {
			String temp = local.get(a);
			try {
				tempCtx = (Context) tempCtx.lookup(temp);
			} catch (NameNotFoundException ne) {
				tempCtx = tempCtx.createSubcontext(temp);
			}
		}
		return tempCtx;
	}
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.management.SbbManagement#uninstallSbb(org.mobicents.slee.core.component.sbb.SbbComponent)
	 */
	public void uninstallSbb(final SbbComponent sbbComponent)
			throws SystemException, Exception, NamingException {

		final SleeTransactionManager sleeTransactionManager = sleeContainer
				.getTransactionManager();
		sleeTransactionManager.mandateTransaction();

		if (logger.isDebugEnabled())
			logger.debug("Uninstalling "+sbbComponent);

		// remove sbb from trace and alarm facilities
		sleeContainer.getTraceManagement().getTraceFacility().unSetTraceLevel(
				sbbComponent.getSbbID());
		sleeContainer.getAlarmManagement().unRegisterComponent(
				sbbComponent.getSbbID());
		
		if (logger.isDebugEnabled()) {
			logger.debug("Removed SBB " + sbbComponent.getSbbID()
					+ " from trace and alarm facilities");
		}
		
		sleeContainer.getJndiManagement().componentUninstall(sbbComponent);
		
		// if we are in cluster mode we need to remove the sbb class loader domain from the replication class loader
		if (!sleeContainer.getCluster().getMobicentsCache().isLocalMode()) {
			final ReplicationClassLoader replicationClassLoader = sleeContainer.getReplicationClassLoader();
			TransactionalAction action2 = new TransactionalAction() {
				public void execute() {
					replicationClassLoader.removeDomain(sbbComponent.getClassLoaderDomain());
				}
			};
			sleeTransactionManager.getTransactionContext().getAfterCommitActions().add(action2);
		}

	}
	
}
