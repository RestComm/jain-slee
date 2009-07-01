package org.mobicents.slee.container.management;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.LinkRef;
import javax.naming.Name;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;
import javax.naming.NameParser;
import javax.naming.NamingException;
import javax.slee.facilities.AlarmFacility;
import javax.slee.facilities.Level;
import javax.slee.management.DeploymentException;
import javax.transaction.SystemException;

import org.jboss.logging.Logger;
import org.jboss.naming.NonSerializableFactory;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ComponentRepositoryImpl;
import org.mobicents.slee.container.component.ResourceAdaptorTypeComponent;
import org.mobicents.slee.container.component.SbbComponent;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.MEnvEntry;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MEjbRef;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MResourceAdaptorEntityBinding;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MResourceAdaptorTypeBinding;
import org.mobicents.slee.container.deployment.SbbClassCodeGenerator;
import org.mobicents.slee.container.service.ServiceActivityContextInterfaceFactoryImpl;
import org.mobicents.slee.container.service.ServiceActivityFactoryImpl;
import org.mobicents.slee.resource.ResourceAdaptorEntity;
import org.mobicents.slee.runtime.facilities.SbbAlarmFacilityImpl;
import org.mobicents.slee.runtime.facilities.TimerFacilityImpl;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

/**
 * Manages sbbs in container
 * 
 * @author martins
 * 
 */
public class SbbManagement {

	private static final Logger logger = Logger.getLogger(SbbManagement.class);

	private final SleeContainer sleeContainer;
	
	public SbbManagement(SleeContainer sleeContainer) {
		this.sleeContainer = sleeContainer;
		
	}

	/**
	 * Deploys an SBB. This generates the code to convert abstract to concrete
	 * class and registers the component in the component table and creates an
	 * object pool for the sbb id.
	 * 
	 * @param mobicentsSbbDescriptor
	 *            the descriptor of the sbb to install
	 * @throws Exception
	 */
	public void installSbb(SbbComponent sbbComponent)
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
			setupSbbEnvironment(sbbComponent);
			// generate class code for the sbb
			new SbbClassCodeGenerator().process(sbbComponent);
			
		//FIXME: this will erase stack trace.	
		//} catch (Exception ex) {
		//	throw ex;
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
				
		// Set 1.0 Trace to off
		sleeContainer.getTraceFacility().setTraceLevelOnTransaction(
				sbbComponent.getSbbID(), Level.OFF);
		sleeContainer.getAlarmFacility().registerComponent(
				sbbComponent.getSbbID());
		
	}

	private void setupSbbEnvironment(SbbComponent sbbComponent) throws Exception {

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

		// Do all the context binding stuff just once during init and
		// just do the linking here.

		Context newCtx;

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

		}

		String nullAciFactory = "java:slee/nullactivity/nullactivitycontextinterfacefactory";
		String nullActivityFactory = "java:slee/nullactivity/nullactivityfactory";

		try {
			newCtx = sleeCtx.createSubcontext("nullactivity");
		} catch (NameAlreadyBoundException ex) {

		} finally {
			newCtx = (Context) sleeCtx.lookup("nullactivity");
		}

		try {
			newCtx.bind("activitycontextinterfacefactory", new LinkRef(
					nullAciFactory));
		} catch (NameAlreadyBoundException ex) {

		}

		try {
			newCtx.bind("factory", new LinkRef(nullActivityFactory));
		} catch (NameAlreadyBoundException ex) {

		}

		String serviceActivityContextInterfaceFactory = "java:slee/serviceactivity/"
				+ ServiceActivityContextInterfaceFactoryImpl.JNDI_NAME;
		String serviceActivityFactory = "java:slee/serviceactivity/"
				+ ServiceActivityFactoryImpl.JNDI_NAME;
		try {
			newCtx = sleeCtx.createSubcontext("serviceactivity");
		} catch (NameAlreadyBoundException ex) {

		} finally {
			newCtx = (Context) sleeCtx.lookup("serviceactivity");

		}
		try {
			newCtx.bind(ServiceActivityContextInterfaceFactoryImpl.JNDI_NAME,
					new LinkRef(serviceActivityContextInterfaceFactory));
		} catch (NameAlreadyBoundException ex) {

		}
		try {
			newCtx.bind(ServiceActivityFactoryImpl.JNDI_NAME, new LinkRef(
					serviceActivityFactory));
		} catch (NameAlreadyBoundException ex) {

		}

		String timer = "java:slee/facilities/" + TimerFacilityImpl.JNDI_NAME;
		String aciNaming = "java:slee/facilities/activitycontextnaming";

		try {
			newCtx = sleeCtx.createSubcontext("facilities");
		} catch (NameAlreadyBoundException ex) {

		} finally {
			newCtx = (Context) sleeCtx.lookup("facilities");
		}
		try {
			newCtx.bind("timer", new LinkRef(timer));
		} catch (NameAlreadyBoundException ex) {

		}

		try {
			newCtx.bind("activitycontextnaming", new LinkRef(aciNaming));
		} catch (NameAlreadyBoundException ex) {
		}

		String trace = "java:slee/facilities/trace";
		try {
			newCtx.bind("trace", new LinkRef(trace));
		} catch (NameAlreadyBoundException ex) {
		}

		String alarm = "java:slee/facilities/alarm";
		try {
			//This has to be checked, to be sure sbb have it under correct jndi binding
			AlarmFacility sbbAlarmFacility = new SbbAlarmFacilityImpl(sbbComponent.getSbbID(),sleeContainer.getAlarmFacility());
			newCtx.bind("alarm", sbbAlarmFacility);
		} catch (NameAlreadyBoundException ex) {
		}

		String profile = "java:slee/facilities/profile";
		try {
			newCtx.bind("profile", new LinkRef(profile));
		} catch (NameAlreadyBoundException ex) {
		}
		String profilteTableAciFactory = "java:slee/facilities/profiletableactivitycontextinterfacefactory";
		try {
			newCtx.bind("profiletableactivitycontextinterfacefactory",
					new LinkRef(profilteTableAciFactory));
		} catch (NameAlreadyBoundException ex) {

		}

		// For each resource that the Sbb references, bind the implementing
		// object name to its comp/env

		if (logger.isDebugEnabled()) {
			logger.debug("Number of Resource Bindings:"
					+ sbbComponent.getDescriptor().getResourceAdaptorTypeBindings());
		}
		ComponentRepositoryImpl componentRepository = sleeContainer.getComponentRepositoryImpl();
		for (MResourceAdaptorTypeBinding raTypeBinding : sbbComponent.getDescriptor().getResourceAdaptorTypeBindings()) {
			
			ResourceAdaptorTypeComponent raTypeComponent = componentRepository.getComponentByID(raTypeBinding.getResourceAdaptorTypeRef());
			
			for (MResourceAdaptorEntityBinding raEntityBinding : raTypeBinding.getResourceAdaptorEntityBinding()) {

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
				Name local = parser.parse(raObjectName);
				int tokenCount = local.size();

				Context subContext = envCtx;

				for (int i = 0; i < tokenCount - 1; i++) {
					String nextTok = local.get(i);
					try {
						subContext.lookup(nextTok);
					} catch (NameNotFoundException nfe) {
						subContext.createSubcontext(nextTok);
					} finally {
						subContext = (Context) subContext.lookup(nextTok);
					}
				}
				String lastTok = local.get(tokenCount - 1);
				// Bind the resource adaptor instance to where the Sbb expects
				// to find it.
				if (logger.isDebugEnabled()) {
					logger
							.debug("setupSbbEnvironment: Binding a JNDI reference to sbb interface of "+raTypeBinding.getResourceAdaptorTypeRef());
				}
				try {
					NonSerializableFactory.rebind(subContext, lastTok, raEntity.getResourceAdaptorInterface(raTypeBinding.getResourceAdaptorTypeRef()));
					//subContext.bind(lastTok, raEntity.getResourceAdaptorInterface(raTypeBinding.getResourceAdaptorTypeRef()));
				} catch (NameAlreadyBoundException e) {
					logger.warn(
							"setupSbbEnvironment: Unable to bind a JNDI reference to sbb interface of "+raTypeBinding.getResourceAdaptorTypeRef(), e);
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
						"setupSbbEnvironment: Binding a JNDI reference to aci factory interface of "+raTypeBinding.getResourceAdaptorTypeRef());
				}
				String factoryRefName = local.get(nameSize - 1);
				try {
					tempCtx
							.bind(factoryRefName,raTypeComponent.getActivityContextInterfaceFactory());
				} catch (NameAlreadyBoundException e) {
					logger.warn(
							"setupSbbEnvironment: Unable to bind a JNDI reference to aci factory interface of "+raTypeBinding.getResourceAdaptorTypeRef(), e);
				}
			}

		}

		/*
		 * Bind the ejb-refs
		 */
		try {
			envCtx.createSubcontext("ejb");
		} catch (NameAlreadyBoundException ex) {
			envCtx.lookup("ejb");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Created ejb local context");
		}

		for (MEjbRef ejbRef : sbbComponent.getDescriptor().getEjbRefs()) {
		
			String jndiName = ejbRef.getEjbRefName();
			
			if (logger.isDebugEnabled()) {
				logger.debug("Binding ejb: " + ejbRef.getEjbRefName()
						+ " with link to " + jndiName);
			}

			try {
				envCtx.bind(ejbRef.getEjbRefName(), new LinkRef(jndiName));
			} catch (NameAlreadyBoundException ex) {
			}

			/*
			 * Validate the ejb reference has the correct type and classes as
			 * specified in deployment descriptor
			 */

			/*
			 * TODO I think I know the problem here. It seems the ejb is loaded
			 * AFTER the sbb is loaded, hence the validation fails here since it
			 * cannot locate the ejb. We need to force the ejb to be loaded
			 * before the sbb
			 */

			/*
			 * Commented out for now
			 * 
			 * 
			 * Object obj = new InitialContext().lookup("java:comp/env/" +
			 * ejbRef.getEjbRefName());
			 * 
			 * Object homeObject = null; try { Class homeClass =
			 * Thread.currentThread().getContextClassLoader().loadClass(home);
			 * 
			 * homeObject = PortableRemoteObject.narrow(obj, homeClass);
			 * 
			 * if (!homeClass.isInstance(homeObject)) { throw new
			 * DeploymentException("Looked up ejb home is not an instanceof " +
			 * home); } } catch (ClassNotFoundException e) { throw new
			 * DeploymentException("Failed to load class " + home); } catch
			 * (ClassCastException e) { throw new DeploymentException("Failed to
			 * lookup ejb reference using jndi name " + jndiName); }
			 * 
			 * Object ejb = null; try { Method m =
			 * homeObject.getClass().getMethod("create", null); Object ejbObject =
			 * m.invoke(home, null);
			 * 
			 * Class ejbClass =
			 * Thread.currentThread().getContextClassLoader().loadClass(remote);
			 * if (!ejbClass.isInstance(ejbObject)) { throw new
			 * DeploymentException("Looked up ejb object is not an instanceof " +
			 * remote); } } catch (ClassNotFoundException e) { throw new
			 * DeploymentException("Failed to load class " + remote); }
			 * 
			 */

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
		for (MEnvEntry mEnvEntry : sbbComponent.getDescriptor().getEnvEntries()) {
			Class type = null;

			if (logger.isDebugEnabled()) {
				logger.debug("Got an environment entry:" + mEnvEntry);
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
	
	public void uninstallSbb(SbbComponent sbbComponent)
			throws SystemException, Exception, NamingException {

		final SleeTransactionManager sleeTransactionManager = sleeContainer
				.getTransactionManager();
		sleeTransactionManager.mandateTransaction();

		if (logger.isDebugEnabled())
			logger.debug("Uninstalling "+sbbComponent);

		// remove sbb from trace and alarm facilities
		sleeContainer.getTraceFacility().unSetTraceLevel(
				sbbComponent.getSbbID());
		sleeContainer.getAlarmFacility().unRegisterComponent(
				sbbComponent.getSbbID());
		
		if (logger.isDebugEnabled()) {
			logger.debug("Removed SBB " + sbbComponent.getSbbID()
					+ " from trace and alarm facilities");
		}

	}
	
}
