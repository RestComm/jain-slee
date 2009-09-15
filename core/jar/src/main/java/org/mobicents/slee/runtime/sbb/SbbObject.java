package org.mobicents.slee.runtime.sbb;

import java.io.Serializable;
import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.ServiceID;
import javax.slee.TransactionRequiredLocalException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.SleeContainerUtils;
import org.mobicents.slee.container.component.SbbComponent;
import org.mobicents.slee.runtime.eventrouter.EventRouterThreadLocals;
import org.mobicents.slee.runtime.eventrouter.SbbInvocationState;
import org.mobicents.slee.runtime.eventrouter.routingtask.EventRoutingTransactionData;
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

	private final static SleeContainer sleeContainer = SleeContainer
			.lookupFromJndi();

	transient private SbbObjectState state;

	transient private SbbInvocationState invocationState = SbbInvocationState.NOT_INVOKING;

	transient static private Logger log = Logger.getLogger(SbbEntity.class);

	/**
	 * the service id assigned to this object
	 */
	private final ServiceID serviceID;

	private transient SbbEntity sbbEntity;

	// My SBB concrete class
	private SbbConcrete sbbConcrete;

	/**
	 * the sbb component
	 */
	private final SbbComponent sbbComponent;

	/**
	 * The Sbb context is the object through which the Sbb interacts with the
	 * Slee
	 */
	private SbbContextImpl sbbContext;

	/**
	 * Creates a new instance of SbbObject.
	 * 
	 * @param sbbComponent .
	 * 
	 * 
	 */
	public SbbObject(ServiceID serviceID, SbbComponent sbbComponent) {

		this.serviceID = serviceID;
		this.sbbComponent = sbbComponent;

		this.createConcreteClass();

		// set sbb context
		this.sbbContext = new SbbContextImpl(this);
		
		if (sbbComponent.getAbstractSbbClassInfo().isInvokeSetSbbContext()) {
			if (log.isDebugEnabled()) {
				log.debug("---> invoking setSbbContext() for " + sbbComponent);
			}
			// before invoking setSbbContext we my need to save the service id in the
			// thread, so the alarm facility can retreive it
			boolean invokingServiceSet = EventRouterThreadLocals
			.getInvokingService() != null;
			if (!invokingServiceSet) {
				EventRouterThreadLocals.setInvokingService(serviceID);
			}
			try {
				this.sbbConcrete.setSbbContext(this.sbbContext);
			} finally {
				if (!invokingServiceSet) {
					EventRouterThreadLocals.setInvokingService(null);
				}
			}
			if (log.isDebugEnabled()) {
				log.debug("<--- invoked setSbbContext() for " + sbbComponent);
			}
		}
		else {
			if (log.isDebugEnabled()) {
				log.debug("Skipping invocation of setSbbContext(), it has no body");
			}
		}
	}

	public void setSbbEntity(SbbEntity sbbe) {
		this.sbbEntity = sbbe;
		this.sbbConcrete.setSbbEntity(sbbe);
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
	 * @see org.mobicents.slee.runtime.SbbEntity#getSbbComponent()
	 */
	public SbbComponent getSbbComponent() {
		return this.sbbComponent;
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

		if (sbbComponent.getAbstractSbbClassInfo().isInvokeUnsetSbbContext()) {

			if (log.isDebugEnabled())
				log.debug("unsetSbbContext " + this.sbbComponent.getSbbID());

			final ClassLoader oldClassLoader = SleeContainerUtils
			.getCurrentThreadClassLoader();

			// FIXME - hat if the sbbDescriptor is null?
			// What if the sbb object is not associated with an entity
			// This is the case if failure occurs in sbbCreate

			try {
				final ClassLoader cl = this.sbbComponent.getClassLoader();
				if (System.getSecurityManager()!=null){

					AccessController.doPrivileged(new PrivilegedAction() {
						public Object run() {
							Thread.currentThread().setContextClassLoader(cl);
							return null;

						}
					});
				}
				else
					Thread.currentThread().setContextClassLoader(cl);
				if (this.sbbConcrete != null)
					this.sbbConcrete.unsetSbbContext();

			} finally {
				if (System.getSecurityManager()!=null)
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
		else {
			if (log.isDebugEnabled()) {
				log.debug("Skipping invocation of unsetSbbContext(), it has no body");
			}
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
		if (sbbComponent.getAbstractSbbClassInfo().isInvokeSbbCreate()) {
			this.invocationState = SbbInvocationState.INVOKING_SBB_CREATE;
			this.sbbConcrete.sbbCreate();
			this.invocationState = SbbInvocationState.NOT_INVOKING;
		}
		else {
			if (log.isDebugEnabled()) {
				log.debug("Skipping invocation of sbbCreate(), it has no body");
			}
		}

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
		if (sbbComponent.getAbstractSbbClassInfo().isInvokeSbbPostCreate()) {
			this.invocationState = SbbInvocationState.INVOKING_SBB_POSTCREATE;
			this.sbbConcrete.sbbPostCreate();
			this.invocationState = SbbInvocationState.NOT_INVOKING;
		}
		else {
			if (log.isDebugEnabled()) {
				log.debug("Skipping invocation of sbbPostCreate(), it has no body");
			}
		}
	}

	/**
	 * @see javax.slee.Sbb#sbbActivate()
	 */
	public void sbbActivate() {

		if (sbbComponent.getAbstractSbbClassInfo().isInvokeSbbActivate()) {
			if (log.isDebugEnabled()) {
				log.debug(this.toString() + ".sbbActivate()");
			}

			if (this.getState() != SbbObjectState.POOLED) {
				log.warn("wrong state -- expected POOLED  was " + this.getState());
			}
			this.sbbConcrete.sbbActivate();
		}
		else {
			if (log.isDebugEnabled()) {
				log.debug("Skipping invocation of sbbActivate(), it has no body");
			}
		}
	}

	/**
	 * @see javax.slee.Sbb#sbbPassivate()
	 */
	public void sbbPassivate() {

		if (sbbComponent.getAbstractSbbClassInfo().isInvokeSbbPassivate()) {
			if (log.isDebugEnabled()) {
				log.debug(this.toString() + ".sbbPassivate()");
			}

			this.sbbConcrete.sbbPassivate();
		}
		else {
			if (log.isDebugEnabled()) {
				log.debug("Skipping invocation of sbbPassivate(), it has no body");
			}
		}
	}

	/**
	 * 
	 * @see javax.slee.Sbb#sbbLoad()
	 */
	public void sbbLoad() throws TransactionRequiredLocalException {

		if (sbbComponent.getAbstractSbbClassInfo().isInvokeSbbLoad()) {
			if (log.isDebugEnabled()) {
				log.debug(this.toString() + ".sbbLoad()");
			}

			sleeContainer.getTransactionManager().mandateTransaction();
			if (this.getState() != SbbObjectState.READY) {
				log.warn("sbbLoad called from wrong state should be READY was "
						+ this.getState());
			}
			this.invocationState = SbbInvocationState.INVOKING_SBB_LOAD;
			this.sbbConcrete.sbbLoad();
			this.invocationState = SbbInvocationState.NOT_INVOKING;
		}
		else {
			if (log.isDebugEnabled()) {
				log.debug("Skipping invocation of sbbLoad(), it has no body");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbStore()
	 */
	public void sbbStore() {

		if (sbbComponent.getAbstractSbbClassInfo().isInvokeSbbStore()) {
			if (log.isDebugEnabled()) {
				log.debug(this.toString() + ".sbbStore()");
			}

			sleeContainer.getTransactionManager().mandateTransaction();

			if (this.getState() != SbbObjectState.READY) {
				log.warn("sbbStore called from wrong state should be READY was "
						+ this.getState());

			}

			final ClassLoader oldClassLoader = SleeContainerUtils
			.getCurrentThreadClassLoader();
			if (this.sbbConcrete != null) {
				final ClassLoader cl = this.sbbComponent.getClassLoader();
				try {
					if (System.getSecurityManager()!=null)
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
							log.debug("Called sbbStore!");
						}
					} else {
						log.error("sbbStore not called: concrete sbb is null");
					}

				} finally {
					if (System.getSecurityManager()!=null)
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
		else {
			if (log.isDebugEnabled()) {
				log.debug("Skipping invocation of sbbStore(), it has no body");
			}
		}
	}

	public void sbbExceptionThrown(Exception exception) {

		boolean invoke = sbbComponent.getAbstractSbbClassInfo().isInvokeSbbExceptionThrown();
		
		Object eventObject = null;
		ActivityContextInterface aci = null;

		if (invoke) {
			EventRoutingTransactionData ertd = EventRoutingTransactionData
			.getFromTransactionContext();
			if (ertd != null) {
				eventObject = ertd.getEventBeingDelivered().getEvent();
				aci = ertd.getAciReceivingEvent();
			}

			if (log.isDebugEnabled()) {
				log.debug(this.toString() + ".sbbExceptionThrown() : exception="
						+ exception + " , eventObject=" + eventObject + " , aci="
						+ aci);
			}
		}
		
		getSbbContext().setRollbackOnly();

		if (invoke) {
			this.sbbConcrete.sbbExceptionThrown(exception, eventObject, aci);
		}
		else {
			if (log.isDebugEnabled()) {
				log.debug("Skipping invocation of sbbExceptionThrown(), it has no body");
			}
		}

	}

	public void sbbRolledBack(RolledBackContext sbbRolledBackContext) {

		if (sbbComponent.getAbstractSbbClassInfo().isInvokeSbbRolledBack()) {
			if (log.isDebugEnabled()) {
				log.debug(this.toString() + ".sbbRolledBack() : rolledBackContext="
						+ sbbRolledBackContext);
			}

			ClassLoader oldClassLoader = Thread.currentThread()
			.getContextClassLoader();
			try {
				Thread.currentThread().setContextClassLoader(
						this.sbbComponent.getClassLoader());

				this.sbbConcrete.sbbRolledBack(sbbRolledBackContext);
			} finally {
				Thread.currentThread().setContextClassLoader(oldClassLoader);
			}
		}
		else {
			if (log.isDebugEnabled()) {
				log.debug("Skipping invocation of sbbRolledBack(), it has no body");
			}
		}

	}

	public void sbbRemove() {

		if (sbbComponent.getAbstractSbbClassInfo().isInvokeSbbRemove()) {
			if (log.isDebugEnabled()) {
				log.debug(this.toString() + ".sbbRemove()");
			}

			final ClassLoader oldClassLoader = SleeContainerUtils
			.getCurrentThreadClassLoader();
			try {

				final ClassLoader cl = this.sbbComponent.getClassLoader();
				if (System.getSecurityManager()!=null)
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
				} else {
					if (log.isDebugEnabled())
						log
						.debug("sbbRemove on the concrete sbb not called: concrete sbb is null");
				}

			} finally {
				if (System.getSecurityManager()!=null)
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
		else {
			if (log.isDebugEnabled()) {
				log.debug("Skipping invocation of sbbRemove(), it has no body");
			}
		}

	}

	private void createConcreteClass() {
		try {

			// logger.debug(sbbDescriptor.getConcreteSbbClass());
			// Concrete class of the Sbb. the concrete sbb class is the
			// class that implements the Sbb methods. This is obtained
			// from the deployment descriptor and the abstract sbb class.
			this.sbbConcrete = (SbbConcrete) sbbComponent.getConcreteSbbClass()
					.newInstance();

		} catch (Exception ex) {

			log.error("unexpected exception creating concrete class!", ex);
			throw new RuntimeException(
					"Unexpected exception creating concrete class for "
							+ this.sbbComponent.getSbbID(), ex);
		}

	}

}