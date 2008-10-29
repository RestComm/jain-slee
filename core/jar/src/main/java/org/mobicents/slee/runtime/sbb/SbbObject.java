package org.mobicents.slee.runtime.sbb;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.TransactionRequiredLocalException;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.SleeContainerUtils;
import org.mobicents.slee.container.component.MobicentsSbbDescriptor;
import org.mobicents.slee.container.deployment.ConcreteSbbGenerator;
import org.mobicents.slee.container.service.Service;
import org.mobicents.slee.runtime.SbbInvocationState;
import org.mobicents.slee.runtime.sbbentity.SbbEntity;

/*
 * Francesco Moggia 
 * M. Ranganathan    Initial version
 * Ralf Siedow       Debugging and hacks
 */

/**
 * SbbEntity implementation object. This is the wrapper object for a concrete
 * SBB instance that the container keeps in memory. This is also used to track
 * things like child relation objects for a sbb instance that has been loaded
 * into the slee.
 * 
 * 
 * @author Francesco Moggia
 * @author M. Ranganathan
 * @author Ralf Siedow
 * @author eduardomartins
 * 
 */

public class SbbObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	transient private SbbObjectState state;

	transient private SbbInvocationState invocationState = SbbInvocationState.NOT_INVOKING;

	transient static private Logger log = Logger.getLogger(SbbEntity.class);

	private SbbID sbbID;

	private ServiceID serviceID = null;

	private transient SbbEntity sbbEntity;

	// My SBB concrete class
	private SbbConcrete sbbConcrete;

	private ArrayList<String> invocationSeq;

	/*
	 * this class contains the information in the deployer descriptor -list of
	 * the received events -list of the intial events these informations may
	 * change at runtime (e.g. An Sbb can modify the event mask using the
	 * SbbContext).
	 */

	private MobicentsSbbDescriptor sbbDescriptor;

	/**
	 * The Sbb context is the object through which the Sbb interacts with the
	 * Slee
	 */
	private SbbContextImpl sbbContext;

	/**
	 * Creates a new instance of SbbObject.
	 * 
	 * @param serviceContainer --
	 *            container where we are installed.
	 * @param sbbDescriptor --
	 *            my descriptor.
	 * 
	 * 
	 */
	public SbbObject(MobicentsSbbDescriptor sbbDescriptor) {

		// my deployment descriptor.
		this.sbbDescriptor = sbbDescriptor;

		this.sbbID = (SbbID) sbbDescriptor.getID();
		this.createConcreteClass();
		this.invocationSeq = new ArrayList<String>();
		
		// set sbb context
		this.sbbContext = new SbbContextImpl(this);
		if (log.isDebugEnabled()) {
			log.debug("---> invoking setSbbContext() for "+sbbDescriptor.getID());
		}
		this.sbbConcrete.setSbbContext(this.sbbContext);
		if (log.isDebugEnabled()) {
			log.debug("<--- invoked setSbbContext() for "+sbbDescriptor.getID());
		}
	}

	public void setSbbEntity(SbbEntity sbbe) {
		this.sbbEntity = sbbe;
		this.sbbConcrete.setSbbEntity(sbbe);

		this.setDefaultUsageParameterSet(sbbe);
		this.setUsageParameterTable(sbbe);

		// Set the named usage parameter set also here.
	}

	public void setServiceID(ServiceID serviceId) {
		this.serviceID = serviceId;

	}

	public ServiceID getServiceID() {
		return this.serviceID;
	}

	public SbbEntity getSbbEntity() throws TransactionRequiredLocalException {
		return sbbEntity;
	}

	/**
	 * @return Returns the state.
	 */
	public SbbObjectState getState() {
		return this.state;
	}

	public SbbInvocationState getInvocationState() {
		return this.invocationState;
	}

	public void setSbbInvocationState(SbbInvocationState state) {
		this.invocationState = state;
	}

	/**
	 * @param state
	 *            The state to set.
	 */
	public void setState(SbbObjectState state) {
		if (log.isDebugEnabled())
			log.debug("setState: current state = " + this.getState()
					+ " new state = " + state);

		this.state = state;
		if (this.sbbConcrete != null)
			this.sbbConcrete.setState(state);
		if (log.isDebugEnabled())
			this.invocationSeq.add(state.toString());
	}

	/**
	 * Getter for property sbbConcrete. The sbb concrete object is an instance
	 * of the concrete class that is instantiated by the deployer.
	 * 
	 * @return Value of property sbbConcrete.
	 * 
	 */
	public Sbb getSbbConcrete() {
		return sbbConcrete;
	}

	/**
	 * Getter for property sbbContextobj.
	 * 
	 * @return Value of property sbbContextobj.
	 * 
	 */
	public SbbContextImpl getSbbContext() {
		return sbbContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.runtime.SbbEntity#getSbbDescriptor()
	 */
	public MobicentsSbbDescriptor getSbbDescriptor() {
		return this.sbbDescriptor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.runtime.SbbEntity#getInitalEventTypes()
	 */
	public Set getInitalEventTypes() {
		return sbbDescriptor.getInitialEventTypes();
	}

	/**
	 * @see javax.slee.Sbb#unsetSbbContext()
	 */
	public void unsetSbbContext() {
		this.sbbContext = null;
		if (this.getState() != SbbObjectState.POOLED) {
			if (log.isDebugEnabled())
				log
						.error("unsetSbbContext: should be called from pooled state current state is "
								+ this.getState());
		}
		if (log.isDebugEnabled())
			log.debug("unsetSbbContext " + this.getSbbID());

		final ClassLoader oldClassLoader = SleeContainerUtils
				.getCurrentThreadClassLoader();

		// FIXME - hat if the sbbDescriptor is null?
		// What if the sbb object is not associated with an entity
		// This is the case if failure occurs in sbbCreate

		try {
			final ClassLoader cl = this.sbbDescriptor.getClassLoader();
			if (SleeContainer.isSecurityEnabled())
				AccessController.doPrivileged(new PrivilegedAction() {
					public Object run() {
						Thread.currentThread().setContextClassLoader(cl);
						return null;

					}
				});
			else
				Thread.currentThread().setContextClassLoader(cl);
			if (this.sbbConcrete != null)
				this.sbbConcrete.unsetSbbContext();

		} finally {
			if (SleeContainer.isSecurityEnabled())
				AccessController.doPrivileged(new PrivilegedAction() {
					public Object run() {
						Thread.currentThread().setContextClassLoader(
								oldClassLoader);
						return null;

					}
				});
			else
				Thread.currentThread().setContextClassLoader(oldClassLoader);

		}

	}

	/**
	 * An SBB object transitions from the Pooled state to the Ready state when
	 * the SLEE selects that SBB object to process an event or to service a
	 * logic object invocation. There are two possible transitions from the
	 * Pooled state to the Ready state: through the sbbCreate and sbbPostCreate
	 * methods, or through the sbbActivate method. The SLEE invokes the
	 * sbbCreate and sbbPostCreate methods when the SBB object is assigned to a
	 * new SBB entity that has just been created explicitly by an invocation of
	 * the create method on a ChildRelation object or implicitly by the SLEE to
	 * process an initial event.
	 * 
	 * @see javax.slee.Sbb#sbbCreate()
	 * 
	 */
	public void sbbCreate() throws CreateException {
		if (this.getState() != SbbObjectState.POOLED) {
			log
					.warn("sbbCreate: should be pooled state was "
							+ this.getState());
		}
		this.invocationState = SbbInvocationState.INVOKING_SBB_CREATE;
		if (log.isDebugEnabled())
			this.invocationSeq.add("CREATE");
		this.sbbConcrete.sbbCreate();
		this.invocationState = SbbInvocationState.NOT_INVOKING;

	}

	/**
	 * This method may throw a javax.slee.CreateException when there is an
	 * application level problem (rather than SLEE or system level problem). The
	 * SLEE will propagate the CreateException unchanged to the caller that
	 * requested the creation of the SBB entity. The caller may be the SLEE or
	 * an SBB object. The throws clause is optional. The SLEE invokes this
	 * method with the transaction context (see Section 9.6) used to invoke the
	 * sbbCreate method. The SBB entity enters the Ready state when
	 * sbbPostCreate returns normally. If sbbPost- Create returns by throwing an
	 * exception, the SBB entity does not become Ready.
	 * 
	 * @see javax.slee.Sbb#sbbPostCreate()
	 * 
	 */
	public void sbbPostCreate() throws CreateException {
		this.invocationState = SbbInvocationState.INVOKING_SBB_POSTCREATE;
		if (log.isDebugEnabled())
			this.invocationSeq.add("POST CREATE");
		this.sbbConcrete.sbbPostCreate();
		this.invocationState = SbbInvocationState.NOT_INVOKING;

	}

	/**
	 * @see javax.slee.Sbb#sbbActivate()
	 */
	public void sbbActivate() {
		if (this.getState() != SbbObjectState.POOLED) {
			log.warn("wrong state -- expected POOLED  was " + this.getState());
		}
		if (log.isDebugEnabled())
			this.invocationSeq.add("ACTIVATE");
		this.sbbConcrete.sbbActivate();
	}

	/**
	 * @see javax.slee.Sbb#sbbPassivate()
	 */
	public void sbbPassivate() {
		if (log.isDebugEnabled())
			this.invocationSeq.add("PASSIVATE");
		this.sbbConcrete.sbbPassivate();
	}

	/**
	 * 
	 * @see javax.slee.Sbb#sbbLoad()
	 */
	public void sbbLoad() throws TransactionRequiredLocalException {
		SleeContainer.getTransactionManager().mandateTransaction();
		if (this.getState() != SbbObjectState.READY) {
			log.warn("sbbLoad called from wrong state should be READY was "
					+ this.getState());
		}
		this.invocationState = SbbInvocationState.INVOKING_SBB_LOAD;
		if (log.isDebugEnabled())
			this.invocationSeq.add("LOAD");
		this.sbbConcrete.sbbLoad();
		this.invocationState = SbbInvocationState.NOT_INVOKING;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbStore()
	 */
	public void sbbStore() {
		SleeContainer.getTransactionManager().mandateTransaction();
		if (this.getState() != SbbObjectState.READY) {
			log.warn("sbbStore called from wrong state should be READY was "
					+ this.getState());

		}

		final ClassLoader oldClassLoader = SleeContainerUtils
				.getCurrentThreadClassLoader();
		if (this.sbbConcrete != null) {
			final ClassLoader cl = this.sbbDescriptor.getClassLoader();
			try {
				if (SleeContainer.isSecurityEnabled())
					AccessController.doPrivileged(new PrivilegedAction() {
						public Object run() {
							Thread.currentThread().setContextClassLoader(cl);
							return null;
						}
					});
				else
					Thread.currentThread().setContextClassLoader(cl);

				if (this.sbbConcrete != null) {
					this.invocationState = SbbInvocationState.INVOKING_SBB_STORE;
					this.sbbConcrete.sbbStore();
					this.invocationState = SbbInvocationState.NOT_INVOKING;
					if (log.isDebugEnabled()) {
						this.invocationSeq.add("STORE");
						log.debug("Called sbbStore!");
					}
				} else {
					log.error("sbbStore not called: concrete sbb is null");
				}

			} finally {
				if (SleeContainer.isSecurityEnabled())
					AccessController.doPrivileged(new PrivilegedAction() {
						public Object run() {
							Thread.currentThread().setContextClassLoader(
									oldClassLoader);
							return null;
						}
					});
				else
					Thread.currentThread()
							.setContextClassLoader(oldClassLoader);

			}

		}
	}

	public void sbbExceptionThrown(Exception exception, Object eventObject,
			ActivityContextInterface activityContextInterface) {
		getSbbContext().setRollbackOnly();

		ClassLoader oldClassLoader = Thread.currentThread()
				.getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(
					this.sbbDescriptor.getClassLoader());

			this.sbbConcrete.sbbExceptionThrown(exception, eventObject,
					activityContextInterface);
			if (log.isDebugEnabled())
				this.invocationSeq.add("EXCEPTION THROWN");
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}

	}

	/**
	 * @return Returns the sbbID.
	 */
	public SbbID getSbbID() {
		return (SbbID) this.sbbDescriptor.getID();
	}

	/**
	 * @param sbbID
	 *            The sbbID to set.
	 */
	public void setSbbID(SbbID sbbID) {
		this.sbbID = sbbID;
	}

	public void sbbRolledBack(RolledBackContext sbbRolledBackContext) {
		ClassLoader oldClassLoader = Thread.currentThread()
				.getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(
					this.sbbDescriptor.getClassLoader());

			this.sbbConcrete.sbbRolledBack(sbbRolledBackContext);
			if (log.isDebugEnabled())
				this.invocationSeq.add("ROLLED BACK");
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}

	}

	public void sbbRemove() {
		final ClassLoader oldClassLoader = SleeContainerUtils
				.getCurrentThreadClassLoader();
		try {

			final ClassLoader cl = this.sbbDescriptor.getClassLoader();
			if (SleeContainer.isSecurityEnabled())
				AccessController.doPrivileged(new PrivilegedAction() {
					public Object run() {
						Thread.currentThread().setContextClassLoader(cl);
						return null;
					}
				});
			else
				Thread.currentThread().setContextClassLoader(cl);

			if (this.sbbConcrete != null) {
				this.sbbConcrete.sbbRemove();
				if (log.isDebugEnabled()) {
					this.invocationSeq.add("REMOVE");
					log.debug("Called sbbRemove");
				}
			} else {
				if (log.isDebugEnabled())
					log.debug("sbbRemove not called: concrete sbb is null");
			}

		} finally {
			if (SleeContainer.isSecurityEnabled())
				AccessController.doPrivileged(new PrivilegedAction() {
					public Object run() {
						Thread.currentThread().setContextClassLoader(
								oldClassLoader);
						return null;
					}
				});
			else
				Thread.currentThread().setContextClassLoader(oldClassLoader);

		}

	}

	/**
	 * Set a pointer to the default usage parameter table in the sbb object.
	 * 
	 * @param sbbe --
	 *            the sbb entity to which we are assigned.
	 * 
	 */
	private void setDefaultUsageParameterSet(SbbEntity sbbe) {
		// Set a pointer to the usage parameter set in the generated class
		if (sbbe != null) {
			try {
				Object usageParameterSet = sbbe
						.getDefaultSbbUsageParameterSet();
				if (usageParameterSet != null) {

					String usageParameterInterfaceName = this
							.getSbbDescriptor().getUsageParametersInterface();
					Class usageParameterInterfaceClass = SleeContainerUtils
							.getCurrentThreadClassLoader().loadClass(
									usageParameterInterfaceName);
					Class concreteClass = this.sbbConcrete.getClass();
					Method setter = concreteClass
							.getMethod(
									ConcreteSbbGenerator.DEFAULT_USAGE_PARAMETER_SETTER,
									new Class[] { usageParameterInterfaceClass });
					setter.invoke(this.sbbConcrete,
							new Object[] { usageParameterSet });
				}
			} catch (Exception ex) {
				throw new RuntimeException(
						"Unexpected Exception while setting default usage parameters",
						ex);
			}
		}
	}

	/**
	 * Set a pointer to the usage parameter hashmap in the sbbobject
	 * 
	 * @param sbbe --
	 *            the sbb entity to which we are assigned.
	 * 
	 */
	private void setUsageParameterTable(SbbEntity sbbe) {

		Map usageParameterTable = null;
		if (sbbe != null)
			usageParameterTable = Service.getUsageParameterTable(sbbe
					.getServiceId());
		sbbConcrete.sbbSetNamedUsageParameterTable(usageParameterTable);

	}

	private void createConcreteClass() {
		try {

			// logger.debug(sbbDescriptor.getConcreteSbbClass());
			// Concrete class of the Sbb. the concrete sbb class is the
			// class that implements the Sbb methods. This is obtained
			// from the deployment descriptor and the abstract sbb class.
			this.sbbConcrete = (SbbConcrete) sbbDescriptor
					.getConcreteSbbClass().newInstance();

		} catch (Exception ex) {

			ex.printStackTrace();
			log.error("unexpected exception creating concrete class!", ex);
			throw new RuntimeException(
					"Unexpected exception creating concrete class for "
							+ this.sbbID, ex);
		}

	}

	/**
	 * @return Returns the invocationSeq.
	 */
	public void printInvocationSeq() {
		if (log.isDebugEnabled()) {
			Iterator i = invocationSeq.iterator();
			String str = new String();
			while (i.hasNext())
				str += i.next().toString() + "\n";

			log.debug("INVOCATION OF LIFECYCLE METHOD FOR COMPONENT: " + sbbID
					+ "\n" + str);
		}

	}

}